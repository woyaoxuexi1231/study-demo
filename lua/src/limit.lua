local core = require("apisix.core")
local limit_count = require("resty.limit.count")
local apisix_plugin = require("apisix.plugin")
local cjson = require "cjson"
local http = require("socket.http")

local ngx = ngx
local ngx_time = ngx.time
local ngx_today = ngx.today
local ipairs = ipairs
local setmetatable = setmetatable

local plugin_name = "custom-limit"

-- 创建一个新的 LRU（Least Recently Used）缓存对象，主要用于插件中的数据缓存和管理。
local lrucache = core.lrucache.new({
    type = 'plugin', serial_creating = true,
})

-- 定义插件的基本信息
local _M = {
    -- 插件版本。
    version = 0.6,
    -- 插件优先级，值越高优先级越高。
    priority = 1001,
    name = plugin_name,
    schema = {}
}

local mt = { __index = _M }

local function set_endtime(self, id_number, key, time_window)
    local end_time = ngx_time() + time_window
    local success, err = self.limits[id_number].dict:set(key, end_time, time_window)
    if not success then
        core.log.error("dict set key ", key, " error: ", err)
    end
    local reset = time_window
    return reset
end

local function read_reset(self, id_number, key)
    local end_time = (self.limits[id_number].dict:get(key) or 0)
    local reset = end_time - ngx_time()
    if reset < 0 then
        reset = 0
    end
    return reset
end

local function gen_limit_key(id_number, conf, ctx)
    local conf_type = ctx.conf_type_without_consumer or ctx.conf_type
    local conf_id = ctx.conf_id_without_consumer or ctx.conf_id
    local new_key = conf_type .. conf_id .. ':' .. apisix_plugin.conf_version(conf)
            .. ':' .. id_number
    if conf._vid then
        return new_key .. ':' .. conf._vid
    end
    return new_key
end

local function create_new(id_number, count, daily_visited_num, conf, ctx)
    local self = { limits = {} }
    local key = gen_limit_key(id_number, conf, ctx)
    core.log.warn("custom-limit-plugin initialing: id_number: ", id_number, " count: ", count, " key: ", key, " daily_visited_num: ", daily_visited_num)

    local lim, err = limit_count.new("plugin-" .. plugin_name, count, 100)
    self.limits[id_number] = {
        limit_count = lim,
        count = count,
        daily_visited_num = daily_visited_num,
        dict = ngx.shared["plugin-" .. plugin_name .. "-reset-header"]
    }

    core.log.warn("custom-limit-plugin successful: id_number: ", id_number, " count: ", count, " key: ", key, " daily_visited_num: ", daily_visited_num)
    return setmetatable(self, mt)
end

local function gen_limit_obj(id_number, count, daily_visited_num, conf, ctx)
    return core.lrucache.plugin_ctx(lrucache, ctx, plugin_name, create_new, id_number, count, daily_visited_num, conf, ctx)
end

local function get_today_date()
    -- 获取当前的日期字符串，格式为 YYYY-MM-DD
    return ngx_today()
end

function _M.incoming(self, id_number, count, key, commit, conf, cost)
    local delay, remaining = self.limits[id_number].limit_count:incoming(key, commit, cost)
    local reset
    if remaining == count - cost then
        reset = set_endtime(self, id_number, key, 100)
    else
        reset = read_reset(self, id_number, key)
    end
    return delay, remaining, reset
end

function _M.access(conf, ctx)
    local token = core.request.header(ctx, "token")
    if not token then
        core.log.error("token header is missing")
        return 401, { error_msg = "token header required" }
    end

    local route_name = ctx.matched_route and ctx.matched_route.value and ctx.matched_route.value.name
    if route_name then
        core.log.warn("Current request route name: ", route_name)
    else
        core.log.warn("Route name not found")
    end

    local url = "http://localhost:10090/decode-token?token=" .. token .. "&path=" .. route_name;
    local response, status = http.request(url)

    if status == 200 then
        core.log.warn("response: ", response)
        local rsp_table = cjson.decode(response)
        local id_number = rsp_table.data.idNumber
        local is_admin = rsp_table.data.isAdmin
        local visited_freq = tonumber(rsp_table.data.visitedFreq)
        local daily_visited_num = tonumber(rsp_table.data.dailyVisitedNum)

        core.log.warn("visited_freq: ", visited_freq)
        core.log.warn("daily_visited_num: ", daily_visited_num)

        if not is_admin then
            if visited_freq == 0 then
                return 401
            end

            local lim, err = gen_limit_obj(id_number, visited_freq, daily_visited_num, conf, ctx)
            if not lim then
                core.log.error("failed to fetch limit.count object: ", err)
                if conf.allow_degradation then
                    return
                end
                return 500
            end

            local key = gen_limit_key(id_number, conf, ctx)
            core.log.warn("limit-key: ", key)

            local delay, remaining, reset
            delay, remaining, reset = lim:incoming(id_number, visited_freq, key, true, conf, 1)

            core.log.warn("custom-limit-plugin access delay: ", delay)
            core.log.warn("custom-limit-plugin access remaining: ", remaining)
            core.log.warn("custom-limit-plugin access reset: ", reset)

            if delay == nil then
                local err = remaining
                if err == "rejected" then
                    return 503
                end
                core.log.error("failed to limit count: ", err)
                if conf.allow_degradation then
                    return
                end
                return 500, { error_msg = "failed to limit count" }
            end

            -- 获取每日访问计数
            local daily_key = "daily-" .. id_number .. "-" .. get_today_date()
            local daily_count = ngx.shared.daily_limits:get(daily_key) or 0

            core.log.warn("daily_count: ", daily_count)

            -- 检查每日访问限制
            if daily_count >= daily_visited_num then
                return 429, { error_msg = "Daily visit limit reached" }
            end

            -- 更新每日访问计数
            local new_daily_count, err = ngx.shared.daily_limits:incr(daily_key, 1, 0, 86400)
            if not new_daily_count then
                core.log.error("failed to increment daily visit count: ", err)
                return 500, { error_msg = "failed to increment daily visit count" }
            end
        end

    else
        core.log.error("HTTP request failed with status code: " .. status)
        return 500
    end
end

return _M

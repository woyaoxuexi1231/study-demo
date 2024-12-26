local core = require("apisix.core")
local limit_count = require("resty.limit.count")
local apisix_plugin = require("apisix.plugin")
local cjson = require "cjson"
local http = require("socket.http")

local ngx = ngx
local ngx_time = ngx.time
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
    version = 0.5,
    -- 插件优先级，值越高优先级越高。
    priority = 1001,
    name = plugin_name,
    schema = {
        type = "object",
        properties = {
            data = {
                type = "array",
                items = {
                    type = "object",
                    properties = {
                        name = { type = "string" },
                        count = { type = "integer", minimum = 1 },
                        time = { type = "integer", minimum = 1 }
                    },
                    required = { "name", "count", "time" }
                }
            }
        },
        required = { "data" }
    }
}

local mt = { __index = _M }

local function parse_token(token)
    -- 假设 Token 格式为 "Bearer user_name"
    local user_name = string.match(token, "Bearer%s+(%w+)")
    return user_name
end

local function set_endtime(self, user_name, key, time_window)
    -- set an end time
    local end_time = ngx_time() + time_window
    -- save to dict by key
    local success, err = self.limits[user_name].dict:set(key, end_time, time_window)

    if not success then
        core.log.error("dict set key ", key, " error: ", err)
    end

    local reset = time_window
    return reset
end

local function read_reset(self, user_name, key)
    local end_time = (self.limits[user_name].dict:get(key) or 0)
    local reset = end_time - ngx_time()
    if reset < 0 then
        reset = 0
    end
    return reset
end

local function gen_limit_key(user_name, conf, ctx)

    local conf_type = ctx.conf_type_without_consumer or ctx.conf_type
    local conf_id = ctx.conf_id_without_consumer or ctx.conf_id
    local new_key = conf_type .. conf_id .. ':' .. apisix_plugin.conf_version(conf)
            .. ':' .. user_name
    if conf._vid then
        -- conf has _vid means it's from workflow plugin, add _vid to the key
        -- so that the counter is unique per action.
        return new_key .. ':' .. conf._vid
    end

    return new_key
end

local function create_new(conf, ctx)

    local self = {
        -- 具体的插件配置项，这个内部会保存一个 OpenResty 的限流器，以及对应的各项限流参数
        limits = {},
        -- nginx共享内存，这个会用于计算目前的限流次数
        --dict = ngx.shared["plugin-" .. plugin_name]
    }

    -- 循环初始化
    for _, user_config in ipairs(conf.data) do

        -- 这三个参数是插件配置的参数
        local user_name = user_config.name
        local count = user_config.count
        local time_window = user_config.time

        -- 根据用户名和插件本身的信息来获取一个
        local key = gen_limit_key(user_name, conf, ctx)

        core.log.warn("custom-limit-plugin initialing: user_name: ", user_name, " count: ", count, " time_window: ", time_window, " key: ", key)

        -- 创建限流器 TODO 这里限流器的名称真的是这个吗？？这样不是每个名称都一样了吗，但是我不设置这个，又会报错 nginx 的共享字典内存不存在
        local lim, err = limit_count.new("plugin-" .. plugin_name, count, time_window)

        -- 将限流器和其他配置项存入 self.limits
        self.limits[user_name] = {
            limit_count = lim,
            -- key = key,
            -- TODO 如果这里保存了后续使用会出问题，初始化阶段只有一次，如果要支持动态，这种方式就不行
            -- count = count,
            -- time_window = time_window,
            dict = ngx.shared["plugin-" .. plugin_name .. "-reset-header"]
        }

        core.log.warn("custom-limit-plugin successful: user_name: ", user_name, " count: ", count, " time_window: ", time_window, " key: ", key)
    end

    return setmetatable(self, mt)
end

local function gen_limit_obj(conf, ctx)
    -- 用于管理插件上下文缓存的函数，是一个用于管理插件上下文缓存的函数。它主要用于在插件上下文中创建或获取缓存对象，以确保缓存项的高效利用和一致性。
    -- core.lrucache.plugin_ctx(lrucache, ctx, extra_key, create_func, ...)
    -- lrucache：LRU 缓存对象，这是由 core.lrucache.new 创建的缓存实例。
    -- ctx：插件上下文对象，包含当前请求的相关信息。这个对象用于在请求的生命周期内存储和共享数据。
    -- extra_key：一个附加键，用于唯一标识缓存项。这个键通常基于插件的配置和请求上下文生成，以确保每个缓存项在特定上下文中是唯一的。
    -- create_func：一个函数，用于创建缓存项。如果缓存中没有找到对应的项，会调用这个函数来创建新项。这个函数通常包含创建对象所需的逻辑。
    -- ...（可变参数）：传递给 create_func 的其他参数。这些参数通常包含创建缓存项所需的额外信息。
    return core.lrucache.plugin_ctx(lrucache, ctx, plugin_name, create_new, conf, ctx)
end

local function find_user_config(conf, user_name)
    for _, user_config in ipairs(conf.data) do
        if user_config.name == user_name then
            return user_config.count, user_config.time
        end
    end
    return nil, nil  -- 如果未找到匹配的用户，返回 nil
end

function _M.incoming(self, user_name, key, commit, conf, cost)

    -- 获取config对应用户的限流信息
    local count, time_window = find_user_config(conf, user_name)
    if count and time_window then
        core.log.warn("custom-limit-plugin incoming find_user_config: ", user_name, " Count: ", count, " Time: ", time_window)
    else
        core.log.error("custom-limit-plugin incoming can not find_user_config")
    end

    -- 这里调用 OpenResty 的 incoming 方法
    -- incoming 方法的作用是记录一次请求并检查当前请求是否超出限流阈值。
    -- 入参：
    -- - key：表示限流键，用于标识请求来源。通常可以使用用户 ID 或 IP 地址。
    -- - commit：布尔值，指示是否实际增加计数。如果为 true，则增加计数；如果为 false，则只是模拟计数不实际增加。
    -- - cost：增加的计数值
    -- 出参：
    -- - delay: 延迟时间
    -- - remaining: 剩余请求次数
    local delay, remaining = self.limits[user_name].limit_count:incoming(key, commit, cost)
    local reset

    if remaining == count - cost then
        reset = set_endtime(self, user_name, key, time_window)
    else
        reset = read_reset(self, user_name, key)
    end

    return delay, remaining, reset
end


-- access 方法是 apisix 的插件规范内的方法，http请求触发后，如果启动了该插件，会按照插件顺序依次调用插件的 access 方法
function _M.access(conf, ctx)

    -- 获取 token 参数
    local token = core.request.header(ctx, "token")
    if not token then
        core.log.error("token header is missing")
        return 401, { error_msg = "token header required" }
    end


    -- TODO 这里应该调用 共享服务的 http 接口根据 token 获取具体的用户名和目录信息
    -- token 解析的结果是 统一认证号:用户名:用户部门id:用户部门名称:ip限制:过期时间
    local url = "http://localhost:10090/decode-token?token=" .. token;

    local response, status = http.request(url)
    if status == 200 then

        core.log.warn("response: ", response)

        local rsp_table = cjson.decode(response)

        core.log.warn("token: ", rsp_table.token)
        core.log.warn("isAdmin: ", rsp_table.isAdmin)

        if (rsp_table.isAdmin ~= true) then

            local user_info = rsp_table.token
            local delimiter = ":"
            local result = {}

            -- 使用 string.gmatch 拆解字符串
            for match in (user_info .. delimiter):gmatch("(.-)" .. delimiter) do
                table.insert(result, match)
            end

            local user_name = result[1]

            local count, time_window = find_user_config(conf, user_name)

            if count == nil and time_window == nil then
                return 401
            end

            -- 获取限流器对象，这里其实拿到的是缓存中生成的这个对象自己
            local lim, err = gen_limit_obj(conf, ctx)
            if not lim then
                core.log.error("failed to fetch limit.count object: ", err)
                if conf.allow_degradation then
                    return
                end
                return 500
            end

            -- 获取一个限流 key
            local key = gen_limit_key(user_name, conf, ctx)
            core.log.warn("limit-key: ", key)

            local delay, remaining, reset
            -- 这里调用的是本对象的 incoming
            delay, remaining, reset = lim:incoming(user_name, key, true, conf, 1)

            core.log.warn("custom-limit-plugin access delay: ", delay)
            core.log.warn("custom-limit-plugin access remaining: ", remaining)
            core.log.warn("custom-limit-plugin access reset: ", reset)

            if delay == nil then
                local err = remaining
                if err == "rejected" then
                    return 503;
                end

                core.log.error("failed to limit count: ", err)
                if conf.allow_degradation then
                    return
                end
                return 500, { error_msg = "failed to limit count" }
            end
        end

    else
        core.log.error("HTTP request failed with status code: " .. status)
        return 500;
    end


end

return _M

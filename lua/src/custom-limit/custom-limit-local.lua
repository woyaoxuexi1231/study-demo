--
-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
local core = require("apisix.core")
local apisix_plugin = require("apisix.plugin")
local tab_insert = table.insert
local ipairs = ipairs
local pairs = pairs
local redis_schema = require("apisix.utils.redis-schema")
local policy_to_additional_properties = redis_schema.schema

local limit_local_new
do
    local local_src = "apisix.plugins.limit-count.limit-count-local"
    limit_local_new = require(local_src).new
end

local lrucache = core.lrucache.new({
    type = 'plugin', serial_creating = true,
})
local group_conf_lru = core.lrucache.new({
    type = 'plugin',
})

local schema = {
    type = "object",
    properties = {
        name = {type = "string", minLength = 1},
        count = {type = "integer", minimum = 1},
        time = {type = "integer", minimum = 1},
    },
    required = {"name", "count", "time"}
}

local schema_copy = core.table.deepcopy(schema)

local _M = {
    schema = schema
}


local function group_conf(conf)
    return conf
end



local function create_limit_obj(conf, plugin_name)
    core.log.info("create new " .. plugin_name .. " plugin instance")

    if not conf.policy or conf.policy == "local" then
        return limit_local_new("plugin-" .. plugin_name, conf.count,
                conf.time_window)
    end

    if conf.policy == "redis" then
        return limit_redis_new("plugin-" .. plugin_name,
                conf.count, conf.time_window, conf)
    end

    if conf.policy == "redis-cluster" then
        return limit_redis_cluster_new("plugin-" .. plugin_name, conf.count,
                conf.time_window, conf)
    end

    return nil
end


local function gen_limit_key(conf, ctx, key)
    if conf.group then
        return conf.group .. ':' .. key
    end

    -- here we add a separator ':' to mark the boundary of the prefix and the key itself
    -- Here we use plugin-level conf version to prevent the counter from being resetting
    -- because of the change elsewhere.
    -- A route which reuses a previous route's ID will inherits its counter.
    local conf_type = ctx.conf_type_without_consumer or ctx.conf_type
    local conf_id = ctx.conf_id_without_consumer or ctx.conf_id
    local new_key = conf_type .. conf_id .. ':' .. apisix_plugin.conf_version(conf)
            .. ':' .. key
    if conf._vid then
        -- conf has _vid means it's from workflow plugin, add _vid to the key
        -- so that the counter is unique per action.
        return new_key .. ':' .. conf._vid
    end

    return new_key
end


local function gen_limit_obj(conf, ctx, plugin_name)
    core.log.info("limit-count: conf.group: ", conf.group)

    if conf.group then
        return lrucache(conf.group, "", create_limit_obj, conf, plugin_name)
    end

    core.log.info("limit-count: conf._vid: ", conf._vid)
    core.log.info("limit-count: conf.policy: ", conf.policy)

    local extra_key
    if conf._vid then
        extra_key = conf.policy .. '#' .. conf._vid
    else
        extra_key = conf.policy
    end

    return core.lrucache.plugin_ctx(lrucache, ctx, extra_key, create_limit_obj, conf, plugin_name)
end

function _M.rate_limit(conf, ctx, name, cost)

    core.log.info("limit-count: conf: ", conf)
    core.log.info("limit-count: ctx: ", ctx)
    core.log.info("limit-count: name: ", name)
    core.log.info("limit-count: cost: ", cost)

    local lim, err = gen_limit_obj(conf, ctx, name)

    if not lim then
        core.log.error("failed to fetch limit.count object: ", err)
        if conf.allow_degradation then
            return
        end
        return 500
    end

    local conf_key = conf.key
    local key
    if conf.key_type == "var_combination" then
        local err, n_resolved
        key, err, n_resolved = core.utils.resolve_var(conf_key, ctx.var)
        if err then
            core.log.error("could not resolve vars in ", conf_key, " error: ", err)
        end

        if n_resolved == 0 then
            key = nil
        end
    elseif conf.key_type == "constant" then
        key = conf_key
    else
        key = ctx.var[conf_key]
    end

    if key == nil then
        core.log.info("The value of the configured key is empty, use client IP instead")
        -- When the value of key is empty, use client IP instead
        key = ctx.var["remote_addr"]
    end

    key = gen_limit_key(conf, ctx, key)
    core.log.info("limit key: ", key)

    local delay, remaining, reset
    if not conf.policy or conf.policy == "local" then
        delay, remaining, reset = lim:incoming(key, true, conf, cost)
    else
        delay, remaining, reset = lim:incoming(key, cost)
    end

    if not delay then
        local err = remaining
        if err == "rejected" then
            -- show count limit header when rejected
            if conf.show_limit_quota_header then
                core.response.set_header("X-RateLimit-Limit", conf.count,
                        "X-RateLimit-Remaining", 0,
                        "X-RateLimit-Reset", reset)
            end

            if conf.rejected_msg then
                return conf.rejected_code, { error_msg = conf.rejected_msg }
            end
            return conf.rejected_code
        end

        core.log.error("failed to limit count: ", err)
        if conf.allow_degradation then
            return
        end
        return 500, {error_msg = "failed to limit count"}
    end

    if conf.show_limit_quota_header then
        core.response.set_header("X-RateLimit-Limit", conf.count,
                "X-RateLimit-Remaining", remaining,
                "X-RateLimit-Reset", reset)
    end
end


return _M

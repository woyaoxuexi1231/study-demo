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


-- 从 apisix.secret 模块中引入 fetch_secrets 函数，用于获取保密配置。
local fetch_secrets = require("apisix.secret").fetch_secrets
-- 从 apisix.plugins.limit-count.init 模块中引入 limit_count，这是限流插件的主要模块。
local limit_count = require("apisix.plugins.limit-count.init")

-- 定义插件名称 limit-count。
local plugin_name = "limit-count"

-- 定义插件元表，包含以下信息：
local _M = {
    -- 插件版本。
    version = 0.5,
    -- 插件优先级，值越高优先级越高。
    priority = 1002,
    -- 插件名称。
    name = plugin_name,
    -- 插件配置的 schema，由 limit_count 模块提供。
    schema = limit_count.schema,
}

-- 定义一个函数，用于校验插件的配置是否符合 schema。调用 limit_count 模块的 check_schema 函数进行校验。
function _M.check_schema(conf)
    return limit_count.check_schema(conf)
end


-- 定义一个函数，用于处理访问阶段的逻辑。
function _M.access(conf, ctx)
    -- 调用 fetch_secrets 函数获取保密配置，并更新 conf。
    conf = fetch_secrets(conf)
    -- 调用 limit_count 模块的 rate_limit 函数，执行限流逻辑，参数包含配置 conf、上下文 ctx、插件名称 plugin_name 和限流单位数 1。
    return limit_count.rate_limit(conf, ctx, plugin_name, 1)
end

-- 将插件元表 _M 返回，以便在 APISIX 中加载和使用。
return _M

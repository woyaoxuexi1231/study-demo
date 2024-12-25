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

-- OpenResty 提供的计数器限流库，用于管理和判断限流逻辑。
local limit_count = require("resty.limit.count")

local ngx = ngx
local ngx_time = ngx.time

local assert = assert
local setmetatable = setmetatable
local core = require("apisix.core")

local _M = {}

local mt = {
    __index = _M
}

local function set_endtime(self, key, time_window)
    -- set an end time
    local end_time = ngx_time() + time_window
    -- save to dict by key
    local success, err = self.dict:set(key, end_time, time_window)

    if not success then
        core.log.error("dict set key ", key, " error: ", err)
    end

    local reset = time_window
    return reset
end

local function read_reset(self, key)
    -- read from dict
    local end_time = (self.dict:get(key) or 0)
    local reset = end_time - ngx_time()
    if reset < 0 then
        reset = 0
    end
    return reset
end

-- 限流对象的创建
function _M.new(plugin_name, limit, window)
    assert(limit > 0 and window > 0)

    local self = {
        -- 使用 resty.limit.count 初始化一个计数器实例，记录特定键的访问次数及时间窗口。
        limit_count = limit_count.new(plugin_name, limit, window),
        -- 使用共享内存字典存储时间窗口的结束时间（reset 值）。
        dict = ngx.shared[plugin_name .. "-reset-header"]
    }

    return setmetatable(self, mt)
end

function _M.incoming(self, key, commit, conf, cost)
    -- 调用 resty.limit.count 的 incoming 方法
    -- 入参:
    --  key - 限流的唯一标识，例如客户端 IP 或上下文变量。
    --  commit：是否立即提交计数（true 提交，false 仅测试）。
    --  cost：每次访问消耗的计数，默认为 1。
    -- 返回值:
    --  delay：延迟时间，通常为 0（在计数器限流中意义不大）。
    --
    local delay, remaining = self.limit_count:incoming(key, commit, cost)
    local reset

    if remaining == conf.count - cost then
        reset = set_endtime(self, key, conf.time_window)
    else
        reset = read_reset(self, key)
    end

    return delay, remaining, reset
end

return _M

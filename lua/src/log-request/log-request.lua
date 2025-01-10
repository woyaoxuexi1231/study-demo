local plugin_name = "log-request"
local core = require("apisix.core")

-- 定义插件参数的 schema
local schema = {
    type = "object",
    properties = {
        data = {
            type = "array",
            items = {
                type = "object",
                properties = {
                    count = {type = "integer", minimum = 1},
                    name = {type = "string", minLength = 1},
                    time = {type = "integer", minimum = 1},
                },
                required = {"count", "name", "time"}
            }
        }
    },
    required = {"data"}
}

local _M = {
    version = 0.1,
    priority = 10,  -- 优先级值，值越高优先级越高
    name = plugin_name,
    schema = schema
}

-- 校验插件参数的函数
function _M.check_schema(conf)
    return core.schema.check(schema, conf)
end

-- access阶段处理函数
function _M.access(conf, ctx)
    -- 遍历 data 数组
    for _, plugin_conf in ipairs(conf.data) do
        core.log.info("Processing config for user: ", plugin_conf.name)
        core.log.info("Count: ", plugin_conf.count, ", Time: ", plugin_conf.time)

        local request_info = {
            method = core.request.get_method(),
            headers = core.request.headers(ctx),
            body = core.request.get_body(),
        }

        -- 记录请求详细信息
        core.log.info("Request Details: ", core.json.encode(request_info))
    end
end

-- 注册插件
return _M

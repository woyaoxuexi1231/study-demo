## limit-req

参考文章：[limit-req | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/plugins/limit-req/)

`limit-req` 插件使用[漏桶算法](https://baike.baidu.com/item/漏桶算法/8455361)限制单个客户端对服务的请求速率。

主要针对请求速率做限制。

## limit-count

参考文章：[limit-count | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/plugins/limit-count/)

`limit-count` 插件使用固定时间窗口算法，主要用于限制**单个客户端**在指定的时间范围内对服务的总请求数，并且会在 HTTP 响应头中返回剩余可以请求的个数。该插件原理与 [GitHub API 的速率限制](https://docs.github.com/en/rest/reference/rate-limit)类似。

需要配置 policy，根据需要设置为 ["local", "redis", "redis-cluster"] 三种中的一种。

配置为redis时，还需要在插件内配置redis的连接信息。



编辑路由规则第三步会出现插件配置，选择limit-count插件，进行配置

count: 限定时间窗口内请求数量的限制

time_windows：时间窗口的大小（以秒为单位）

key_type: 

key: 

1. **URL 参数**：例如，用户 ID 作为请求的查询参数传递，如 `/api/resource?user_id=12345`。在这种情况下，你可以使用 `key: "arg_user_id"` 来进行限流，其中 `arg_` 是 Nginx 的标准用法，用来访问 URL 查询参数。
2. **HTTP Header**：如果用户 ID 通过 HTTP 请求头传递，例如 `X-User-ID: 12345`，你可以使用 `key: "http_x_user_id"` 来进行限流。
3. **Cookie**：用户 ID 也可以存储在 cookie 中，例如 `user_id=12345`，这时可以使用 `key: "cookie_user_id"`。

```shell
curl "http://192.168.3.233:9080/ip"
curl "http://127.0.0.1:9080/ip"
```






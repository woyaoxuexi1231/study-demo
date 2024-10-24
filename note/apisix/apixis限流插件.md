## limit-req

参考文章：[limit-req | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/plugins/limit-req/)

`limit-req` 插件使用[漏桶算法](https://baike.baidu.com/item/漏桶算法/8455361)限制单个客户端对服务的请求速率。

主要针对请求速率做限制。

## limit-count

参考文章：[limit-count | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/plugins/limit-count/)

`limit-count` 插件使用固定时间窗口算法，主要用于限制**单个客户端**在指定的时间范围内对服务的总请求数，并且会在 HTTP 响应头中返回剩余可以请求的个数。该插件原理与 [GitHub API 的速率限制](https://docs.github.com/en/rest/reference/rate-limit)类似。

需要配置 policy，根据需要设置为 ["local", "redis", "redis-cluster"] 三种中的一种。

配置为redis时，还需要在插件内配置redis的连接信息。








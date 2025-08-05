package org.hulei.demo.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woaixuexi
 * @since 2024/4/23 0:36
 */

@RestController
@SpringBootApplication
public class OAuth2ServerApplication {

    public static void main(String[] args) {
        /*
        Oauth 授权模块
        | 时间   | 模块/库名称                                                                           | 主要作用                                          | 状态                    | 原因/备注                                                              |
        | ---- | ------------------------------------------------------------------------------------ | -------------------------------------------      | ------------------     | ------------------------------------------------------------------ |
        | 2012 | `spring-security-oauth`                                                              | OAuth2 Provider、Client、Resource Server 全部功能  | ❌ 已废弃               | 基于 Spring Security 3/4，架构老旧，难以维护                                   |
        | 2016 | `spring-security-oauth2-autoconfigure`                                               | Boot 项目快速集成 `spring-security-oauth` 的自动配置 | ⚠️ 仍可用但不推荐        | 只适用于 Spring Boot 2.1及以下                                            |
        | 2018 | Spring Security 5.x 引入 `spring-security-oauth2-client`, `oauth2-resource-server`   | 支持 OAuth2 登录、JWT 校验                          | ✅ 官方推荐             | OAuth2 被拆分为职责清晰的组件                                                 |
        | 2020 | `spring-authorization-server` 项目启动                                               | 独立 OAuth2 授权服务器                               | ✅ 新项目，逐步成熟中     | 替代原来的 `spring-security-oauth` 中授权服务功能                              |
        | 2021 | `spring-security-oauth` 正式停止维护                                                  | -                                                 | ❌ 永久弃用             | 官方明确不再更新                                                           |
        | 2022 | Spring Security 6.0 / Spring Boot 3                                                | 完全移除 `spring-security-oauth` 支持                | ✅ Modern OAuth2 架构  | 使用 `spring-authorization-server`, `client`, `resource-server` 三分模型 |
        其中 spring-security-oauth2-authorization-server 其实就是 Spring Authorization Server

        | 类型                        | Starter 依赖（Spring Boot）                           | 实际核心实现库                                       | 状态                   | 推荐使用？ |
        | ------------------------- | ------------------------------------------------- | --------------------------------------------- | -------------------- | ----- |
        | 授权服务器                     | `spring-boot-starter-oauth2-authorization-server` | `spring-security-oauth2-authorization-server` | ✅ 新官方项目              | ✅ 推荐  |
        | 资源服务器                     | `spring-boot-starter-oauth2-resource-server`      | `spring-security-oauth2-resource-server`      | ✅ Spring Security 内置 | ✅ 推荐  |
        | 客户端                       | `spring-boot-starter-oauth2-client`               | `spring-security-oauth2-client`               | ✅ Spring Security 内置 | ✅ 推荐  |
        | 🛑 老版本（OAuth2 all-in-one） | `spring-security-oauth2`                          | 旧版库（已停止维护）                                    | ❌ 停止维护               | ❌ 不推荐 |

        但是 spring-boot-starter-oauth2-authorization-server 最低也需要 springboot 3.x 才支持了
        所以 springboot 2.7.x 只能用 spring-security-oauth2-authorization-server 这个了

        spring-security-oauth2-autoconfigure 最后一个版本是 2.6.8
        支持到 spring boot 版本 2.6.8，使用 2.7.18 版本的 spring boot，包括 spring security时，也已经不被推荐使用了
        此版本的 spring boot 对应的 security 的版本是 5.7.11
        spring boot dependency https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies/2.7.18

        文档介绍 https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/

        http://localhost:9000/oauth2/authorize
        http://localhost:9000/oauth2/token
         */
        SpringApplication.run(OAuth2ServerApplication.class);
    }

    // ccbb9179-6568-41a4-9139-56c184235995

    @GetMapping("/")
    public String home(){
        return "Hello World";
    }
}

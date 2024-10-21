#

[Spring Security 中文文档](https://springdoc.cn/spring-security/servlet/getting-started.html)

## 使用一个完全默认的 Spring Security

在完全没有任何配置的情况下启动 Spring Security,会输出以下日志

```
24-10-15 15:50:12.759 WARN [restartedMain] org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration(89) : 

Using generated security password: 13013766-73b2-4767-82a5-18bd798dbb6c

This generated password is for development use only. Your security configuration must be updated before running your application in production.
```

访问任何接口，都不会通过权限校验。

Spring Boot 和 Spring Security 的默认安排在运行时提供了以下行为：

- 任何端点（包括 Boot 的 /error 端点）都需要一个认证的用户。
- 在启动时用生成的密码 注册一个默认用户（密码被记录到控制台；在前面的例子中，密码是 13013766-73b2-4767-82a5-18bd798dbb6c,
  用户名是 user）。
- 用 BCrypt 以及其他方式保护密码存储。
- 提供基于表单的 登录 和 注销 流程。
- 对 基于表单 的登录以及 HTTP Basic 进行认证。
- 提供内容协商；对于web请求，重定向到登录页面；对于服务请求，返回 401 Unauthorized。
- 减缓 CSRF 攻击。
- 减缓 Session Fixation 攻击。
- 写入 Strict-Transport-Security，以 确保HTTPS。
- 写入 X-Content-Type-Options 以减缓 嗅探攻击。
- 写入保护认证资源的 Cache Control header。
- 写入 X-Frame-Options，以缓解 点击劫持 的情况。
- 与 HttpServletRequest 的认证方法 整合。
- 发布 认证成功和失败的事件。
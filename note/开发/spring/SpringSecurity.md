## 

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

## Spring Security 是如何维护登录状态的

细化一下Spring Security在处理用户登录和会话管理中的工作流程，以及它是如何利用Cookies（特别是`JSESSIONID`），来在不同请求之间维护用户的登录状态的：

### 用户登录过程

1. **用户提交登录请求**：
   用户通过登录表单提交用户名和密码，请求通常被发送到一个预定义的URL（例如：`/login`）。

2. **过滤器拦截登录请求**：
   在Spring Security中，`UsernamePasswordAuthenticationFilter`负责拦截该请求。这个过滤器监听特定的登录URL，一旦捕获到登录请求，就会提取用户名和密码。

3. **创建Authentication对象**：
   使用提取的用户名和密码，创建一个`UsernamePasswordAuthenticationToken`实例。这个实例此时表示一个未认证的认证请求。

4. **认证过程**：
   这个认证令牌（`UsernamePasswordAuthenticationToken`）被传递给`AuthenticationManager`。`AuthenticationManager`
   会委托给配置好的`AuthenticationProvider`，比如`DaoAuthenticationProvider`，以验证用户名和密码是否正确。

5. **认证成功**：
   一旦用户成功认证，`AuthenticationManager`返回一个填充了用户详情（权限、角色等）的已认证`Authentication`对象。此时，用户的登录状态得到确认。

6. **存储Authentication并创建Session**：
   这个已认证的`Authentication`对象被存储在`SecurityContext`中，而`SecurityContext`则被保存在`HttpSession`中。这一步时，如果用户之前没有会话，服务器会创建一个新的HTTP
   Session，并生成一个唯一的`JSESSIONID`作为会话的标识。这个`JSESSIONID`将通过Set-Cookie头部返回给客户端，随后客户端会存储这个Cookie。

### 维持登录状态

1. **后续请求**：
   当用户进行后续的请求时，浏览器会自动附带这个`JSESSIONID` Cookie。服务器接收到请求，通过`JSESSIONID`识别出是哪个会话，并从对应的会话中恢复`SecurityContext`。

2. **安全检查和权限验证**：
   由于`SecurityContext`已恢复，Spring Security可以访问用户的认证信息，包括权限与角色等。这使得Spring Security能对每个请求进行安全检查，如访问控制、方法级安全等。

3. **会话过期与管理**：
   服务器端的会话管理机制还可以处理诸如会话超时、并发会话控制等安全考量。若用户的Session过期或被管理员强制失效，用户的下一个请求将不能再基于`JSESSIONID`获取有效的SecurityContext，用户则需要重新登录。

通过上面的详细解释，你可以看到`JSESSIONID`在Spring
Security的登录和会话管理中扮演了至关重要的角色。它是连接用户和其服务器端会话（包括其中存储的认证信息）的桥梁。与基于Token的身份验证方法（如JWT）不同，Session-ID的机制侧重于在服务器侧维护状态，这在某些场景下为应用带来了方便的会话管理和内置的跨请求安全上下文持续性。

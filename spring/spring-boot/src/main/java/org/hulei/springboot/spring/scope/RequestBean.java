package org.hulei.springboot.spring.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author hulei
 * @since 2025/6/26 13:41
 */

/*
每个 HTTP 请求创建一个实例（仅 Web 应用中可用）。
必须加 proxyMode = ScopedProxyMode.TARGET_CLASS 否则注入报错。

像 request、session 这类作用域的 Bean，在 Web 应用中是 “请求线程绑定” 的，Spring 需要在真正的 Web 请求上下文中才知道该注入哪个具体实例。
如果你不加代理，Spring 在容器初始化时就尝试注入这些 request / session 范围的 Bean。但此时根本没有 HTTP 请求，就会抛出你看到的这个异常：
Caused by: java.lang.IllegalStateException:
No thread-bound request found:
Are you referring to request attributes outside of an actual web request...
加上代理后 Spring 会注入一个 代理对象，在实际请求发生时，才去“懒加载”真正作用域内的实例（比如当前 Session 对应的对象）
 */
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RequestBean {
}

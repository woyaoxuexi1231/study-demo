package mypack;

import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

/**
 * 1. 抽象类: 主要用来创建与协议无关的 Servlet。它实现了 Servlet接口的大部分方法，只留下 service()方法需要子类去实现。
 * 2. 协议无关：用来处理任何类型的请求协议，不仅仅是 HTTP。这使得它比较通用，但通常情况下，Web应用主要用 HTTP协议，因此实际应用中使用较少。
 * 3. 简化实现：提供了 init(), getServletConfig()和 getServletContext()等方法，帮助简化 Servlet实现，使得子类只需要关注请求和响应的处理。
 * 4. 无具体实现：因为没有协议约束，GenericServlet不会提供附加的便利方法用于处理 HTTP特定的请求和响应。
 * <p>
 * 目前来说, 这个实现毫无意义, 因为需要容器的支持, 目前也就支持了 http 协议的 servlet
 *
 * @author hulei
 * @since 2024/10/14 22:21
 */

public class MyGenericServlet extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

    }
}

package mypack;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * HTTP特定：专为 HTTP协议设计，添加了doGet(), doPost(), doPut(), doDelete()等方法，使得处理特定HTTP方法更加方便。
 * 自动支持HTTP协议特性：提供对HTTP请求和响应的全面支持，比如请求参数、Cookie、Session等，可以直接通过相关方法来方便地访问和操作。
 * 广泛使用：HttpServlet在实际Web开发中非常常用，因为大多数的Web应用都依赖HTTP协议来传输数据。
 *
 * @author hulei
 * @since 2024/10/14 22:24
 */


public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        HttpServletRequest用于封装客户端发送到服务器的HTTP请求信息。服务器通过这个接口从请求中获取数据。
        主要功能：
            1. 获取请求信息：包括请求参数、头信息、Cookie、路径、上下文路径等。
            2. 会话管理：可以通过getSession()方法管理用户会话。
            3. 处理输入流：可以使用getInputStream()来读取请求体的数据（通常用于读取POST请求中的数据）。
        一般来说,不能用于服务器返回数据到客户端，它的主要作用是帮助服务器理解和处理来自客户端的请求。
        也有例外: javax.servlet.ServletRequest.setAttribute
            通过 setAttribute 方法可以在请求中存储数据,这些属性可以在整个请求的生命周期中共享和访问
            主要是为了组件之间传递数据(比如传递给下一个Servlet,或者JSP,或者下一个Filter)
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        request.setAttribute("USER", username);
        request.setAttribute("PASSWORD", password);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.printf("name: %s, value: %s, path: %s%n", cookie.getName(), cookie.getValue(), cookie.getPath());
        }

        /*
        HttpServletResponse用于帮助服务器构建和发送对客户端的HTTP响应。它为返回数据提供了接口。
        主要功能：
            1. 设置响应信息：可以设置响应状态码、响应头信息等。
            2. 处理输出流：可以通过getWriter()方法输出响应主体（通常为文本数据）或者通过getOutputStream()输出二进制数据。
            3. 管理重定向：可以使用sendRedirect()方法向客户端发送重定向命令。
            4. 发送Cookie：可以通过addCookie()方法向客户端发送Cookie。
         */
        response.addCookie(new Cookie("helloapp", "123"));
        ServletContext context = getServletContext();

        RequestDispatcher dispatcher = context.getRequestDispatcher("/hello.jsp");
        // 这里就通过在HttpServletRequest设置了属性,然后把请求继续传递给JSP,这样jsp就能访问request里的设置的属性了
        dispatcher.forward(request, response);
    }
}
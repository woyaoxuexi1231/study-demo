package mypack;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends GenericServlet {

    private final String target = "/hello.jsp";

    /**
     * 响应客户请求
     */
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

        //读取表单中的用户名
        String username = request.getParameter("username");
        //读取表单中的口令
        String password = request.getParameter("password");

        //在request对象中添加USER属性
        request.setAttribute("USER", username);
        //在request对象中添加PASSWORD属性
        request.setAttribute("PASSWORD", password);

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        for (Cookie cookie : cookies) {
            // 在HTTP cookie中，path是一个可选参数，用于指定哪些路径下的页面可以访问该cookie。当服务器发送一个cookie给浏览器时，它可以通过指定path来限制cookie只能被该路径下的页面访问。
            System.out.println(String.format("name: %s, value: %s, path: %s", cookie.getName(), cookie.getValue(), cookie.getPath()));
        }

        ((HttpServletResponse)(response)).addCookie(new Cookie("helloapp","123"));

        /*把请求转发给hello.jsp */
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }
}


/****************************************************
 * 作者：孙卫琴                                     *
 * 来源：<<Tomcat与Java Web开发技术详解>>           *
 * 技术支持网址：www.javathinker.net                *
 ***************************************************/

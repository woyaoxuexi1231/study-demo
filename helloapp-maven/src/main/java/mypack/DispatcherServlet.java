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

    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        request.setAttribute("USER", username);
        request.setAttribute("PASSWORD", password);

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(String.format("name: %s, value: %s, path: %s", cookie.getName(), cookie.getValue(), cookie.getPath()));
        }

        ((HttpServletResponse)(response)).addCookie(new Cookie("helloapp","123"));

        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }
}
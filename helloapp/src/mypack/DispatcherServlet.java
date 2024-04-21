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
     * ��Ӧ�ͻ�����
     */
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

        //��ȡ���е��û���
        String username = request.getParameter("username");
        //��ȡ���еĿ���
        String password = request.getParameter("password");

        //��request���������USER����
        request.setAttribute("USER", username);
        //��request���������PASSWORD����
        request.setAttribute("PASSWORD", password);

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        for (Cookie cookie : cookies) {
            // ��HTTP cookie�У�path��һ����ѡ����������ָ����Щ·���µ�ҳ����Է��ʸ�cookie��������������һ��cookie�������ʱ��������ͨ��ָ��path������cookieֻ�ܱ���·���µ�ҳ����ʡ�
            System.out.println(String.format("name: %s, value: %s, path: %s", cookie.getName(), cookie.getValue(), cookie.getPath()));
        }

        ((HttpServletResponse)(response)).addCookie(new Cookie("helloapp","123"));

        /*������ת����hello.jsp */
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }
}


/****************************************************
 * ���ߣ�������                                     *
 * ��Դ��<<Tomcat��Java Web�����������>>           *
 * ����֧����ַ��www.javathinker.net                *
 ***************************************************/

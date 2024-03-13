package mypack;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class DispatcherServlet extends GenericServlet {

    private String target = "/hello.jsp";

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

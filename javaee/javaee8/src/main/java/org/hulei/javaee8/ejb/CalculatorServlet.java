package org.hulei.javaee8.ejb;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <a href="http://localhost:13004/javaee8_Web_exploded/calculator?a=10&a=10&b=5&b=5&operation=add&operation=add">TomEE</a>
 * <a href="http://localhost:13081/javaee8_Web_exploded/calculator?a=10&a=10&b=5&b=5&operation=add&operation=add">Wildfly</a>
 *
 * @author hulei
 * @since 2024/10/20 21:49
 */

@WebServlet("/calculator")
public class CalculatorServlet extends HttpServlet {

    @EJB
    private CalculatorLocal calculator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        String operation = request.getParameter("operation");

        int result;
        if ("add".equals(operation)) {
            result = calculator.add(a, b);
        } else if ("subtract".equals(operation)) {
            result = calculator.subtract(a, b);
        } else {
            result = 0;
        }

        response.getWriter().write("Result: " + result);
    }
}

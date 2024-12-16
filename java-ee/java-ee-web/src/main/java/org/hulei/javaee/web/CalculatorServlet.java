package org.hulei.javaee.web;

import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hulei.javaee.ejb.CalculatorLocal;

import java.io.IOException;

/**
 * @author hulei
 * @since 2024/12/8 2:51
 */

@WebServlet("/calculator")
public class CalculatorServlet extends HttpServlet {

    @EJB
    private CalculatorLocal calculator;

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

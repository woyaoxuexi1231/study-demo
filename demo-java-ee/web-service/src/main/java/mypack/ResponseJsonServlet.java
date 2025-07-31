package mypack;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hulei
 * @since 2024/10/14 22:39
 */

@Slf4j
public class ResponseJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        这里用 HttpServletResponse 来封装返回结果
        1. 设置响应的正文类型
        2. 通过输出流写入我们的结果
         */
        log.info("you will get a hello world");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("hello world!");
        out.close();
    }
}

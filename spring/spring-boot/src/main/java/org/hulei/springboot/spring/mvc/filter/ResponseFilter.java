package org.hulei.springboot.spring.mvc.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.io.PrintWriter;

@Order(2)
// @Component
@Slf4j
public class ResponseFilter implements Filter {
    private static class MyResponseWrapper extends HttpServletResponseWrapper {
        private final MyPrintWriter myPrintWriter = new MyPrintWriter();

        public MyResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return myPrintWriter;
        }

        public String getContent() {
            return myPrintWriter.getContent();
        }
    }

    private static class MyPrintWriter extends PrintWriter {
        private final StringBuilder content = new StringBuilder();

        public MyPrintWriter() {
            super(System.out);
        }

        @Override
        public void write(char[] buf, int off, int len) {
            content.append(buf, off, len);
            super.write(buf, off, len);
        }

        public String getContent() {
            return content.toString();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // MyResponseWrapper responseWrapper = new MyResponseWrapper(httpServletResponse);
        //
        // chain.doFilter(request, responseWrapper);
        //
        // String responseBody = responseWrapper.getContent();
        // // 对响应进行处理
        // // ...
        // log.info("{} : {}", ResponseFilter.class.getSimpleName(), responseBody);
        //
        //
        // // // 向客户端输出处理后的响应
        // // PrintWriter writer = httpServletResponse.getWriter();
        // // writer.write(responseBody);
        // // writer.flush();
        // // writer.close();



        // 确保请求和响应是正确的类型
        if (response instanceof HttpServletResponse) {
            CaptureResponseWrapper responseWrapper = new CaptureResponseWrapper((HttpServletResponse) response);

            // 继续处理请求
            chain.doFilter(request, responseWrapper);

            // 获取捕获的响应内容
            byte[] responseContent = responseWrapper.getCapturedResponse();

            // 将捕获的响应内容转换为字符串
            String responseString = new String(responseContent);
            System.out.println("Captured Response: " + responseString);

            // 将捕获的内容写回原始响应
            response.getOutputStream().write(responseContent);
        } else {
            // 如果不是 HttpServletResponse，直接继续处理
            chain.doFilter(request, response);
        }
    }

    // 其他方法省略...
}
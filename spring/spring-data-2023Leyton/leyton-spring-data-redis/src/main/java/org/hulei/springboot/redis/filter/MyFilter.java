package org.hulei.springboot.redis.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {

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
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        MyResponseWrapper responseWrapper = new MyResponseWrapper(httpServletResponse);

        chain.doFilter(request, responseWrapper);

        String responseBody = responseWrapper.getContent();
        // 对响应进行处理
        // ...

        // 向客户端输出处理后的响应
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(responseBody);
        writer.flush();
        writer.close();
    }

    // 其他方法省略...
}
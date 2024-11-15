package org.hulei.springboot.js.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = {"/greeting"})
// @Component
@Slf4j
public class DataFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String contentType = request.getContentType();
        WrapperedResponse wrapResponse = new WrapperedResponse((HttpServletResponse) response);
        WrapperedRequest wrapRequest;
        // 表单请求数据自行处理解密
        if (contentType == null || !contentType.contains("multipart/form-data")) {
            String requestBody = getRequestBody((HttpServletRequest) request);
            log.info("请求数据：" + requestBody);
			// todo
            // 处理请求报文
            wrapRequest = new WrapperedRequest((HttpServletRequest) request, requestBody);
            chain.doFilter(wrapRequest, wrapResponse);
        } else {
            chain.doFilter(request, wrapResponse);
        }


        byte[] data = wrapResponse.getResponseData();
        String responseBody = new String(data, StandardCharsets.UTF_8);
        log.info("原始返回数据： " + responseBody);
        // 返回报文
        String responseBodyStr = responseBody;
        log.info("处理后返回数据： " + responseBodyStr);
        writeResponse(response, responseBodyStr);
    }

    @Override
    public void destroy() {

    }


    private String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            log.info("请求体读取失败" + e.getMessage());
        }
        return "";
    }

    private void writeResponse(ServletResponse response, String responseString) throws IOException {
        response.setContentLength(-1);
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
        out.close();
    }

}

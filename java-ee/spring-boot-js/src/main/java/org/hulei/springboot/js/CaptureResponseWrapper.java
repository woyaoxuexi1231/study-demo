package org.hulei.springboot.js;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class CaptureResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream capture;
    private PrintWriter writer;

    public CaptureResponseWrapper(HttpServletResponse response) {
        super(response);
        capture = new ByteArrayOutputStream();
        writer = new PrintWriter(capture);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;  // 返回 PrintWriter 用于捕获响应内容
    }

    public byte[] getCapturedResponse() {
        writer.flush();  // 确保所有内容都已经写入流
        return capture.toByteArray();  // 返回捕获的响应内容
    }
}

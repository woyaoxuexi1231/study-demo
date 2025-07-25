package com.hundsun.demo.springcloud.security.controller;

import com.google.code.kaptcha.Producer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @author hulei
 * @since 2025/7/24 21:58
 */

@Controller
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @SneakyThrows
    @GetMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        // 设置 jpeg 返回格式
        response.setContentType("image/jpeg");
        // 创建验证码文本
        String capText = captchaProducer.createText();
        // 将验证码文本设置到 session
        request.getSession().setAttribute("captcha", capText);
        // 创建验证码图片
        BufferedImage image = captchaProducer.createImage(capText);
        // 获取响应输出流
        ServletOutputStream os = response.getOutputStream();
        // 将图片验证码数据写入响应流
        ImageIO.write(image, "jpg", os);
        os.flush();
        os.close();
    }
}

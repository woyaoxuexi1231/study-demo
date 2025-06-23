package org.hulei.springboot.spring.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
/*
专门用于在 Spring MVC 应用程序中测试 Web 层组件
仅加载与指定控制器（MvcControllerTest）相关的上下文，而不是整个应用程序上下文。
 */
@WebMvcTest(MvcController.class)
public class MvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))  // 模拟一个 HTTP GET 请求，访问应用的根路径 /。
                .andExpect(status().isOk())  // 期望服务器返回的 HTTP 状态码是 200 OK，表示请求成功。
                .andExpect(view().name("home"))  // 期望返回的视图（View）名称是 "home"，通常对应一个 Thymeleaf、JSP 或其他模板引擎的视图文件（如 home.html 或 home.jsp）。
                .andExpect(content().string(containsString("Welcome to..."))); // 期望返回的响应内容（HTML、JSON 或其他文本）中包含字符串 "Welcome to..."。
    }
}
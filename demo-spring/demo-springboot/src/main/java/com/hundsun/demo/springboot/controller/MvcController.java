// package com.hundsun.demo.springboot.controller;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.thymeleaf.TemplateEngine;
// import org.thymeleaf.context.Context;
//
// /**
//  * @projectName: study-demo
//  * @package: com.hundsun.demo.springboot.controller
//  * @className: MvcController
//  * @description:
//  * @author: h1123
//  * @createDate: 2023/4/3 22:06
//  */
//
// @Controller
// @Slf4j
// public class MvcController {
//
//     @Autowired
//     TemplateEngine engine;
//
//     @GetMapping("/index")
//     @ResponseBody
//     public String test() {
//         Context context = new Context();
//         context.setVariable("name", "name");
//         return engine.process("index", context);
//     }
// }

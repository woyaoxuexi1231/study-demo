package com.hundsun.demo.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: SimpleController
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:30
 */
@Controller
@Slf4j
public class SimpleController {
    //
    // @Autowired
    // SimpleService simpleService;
    //
    // /**
    //  * 双数据源不使用分布式事务如何保证事务
    //  */
    // @GetMapping("/multiDataSourceSingleTransaction")
    // public ResultDTO<?> multiDataSourceSingleTransaction() {
    //     return simpleService.multiDataSourceSingleTransaction();
    // }
    //
    // @GetMapping("/mysqlSelect")
    // public void mysqlSelect() {
    //     simpleService.mysqlSelect();
    // }
    //
    // @GetMapping("/mysqlUpdate")
    // public void mysqlUpdate() {
    //     simpleService.mysqlUpdate();
    // }
    //
    // @GetMapping("/mybatis")
    // public void mybatis() {
    //     simpleService.mybatis();
    // }
    //
    // @GetMapping("/testMysqlAutoKey")
    // public void testMysqlAutoKey() {
    //     simpleService.testMysqlAutoKey();
    // }

    @GetMapping("/index")
    public String test(Model model){
        Map map = new HashMap();
        map.put("msg","hello, spring boot jsp");
        model.addAllAttributes(map);
        return "index";
    }
}

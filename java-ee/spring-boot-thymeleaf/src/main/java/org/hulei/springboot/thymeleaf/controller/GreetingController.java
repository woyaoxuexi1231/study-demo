package org.hulei.springboot.thymeleaf.controller;

import cn.hutool.core.lang.Snowflake;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.entity.jpa.utils.MemoryDbUtil;
import org.hulei.util.dto.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.com.hundsun.demo.springboot.thymeleaf.controller
 * @className: GreetingController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 13:48
 */

@Controller
public class GreetingController {

    @GetMapping("/employees")
    public String userQueryPage(Model model,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size) {

        model.addAttribute("pageInfo", MemoryDbUtil.getData(page, size));

        return "userQueryPage"; // Thymeleaf模板的名字
    }

}

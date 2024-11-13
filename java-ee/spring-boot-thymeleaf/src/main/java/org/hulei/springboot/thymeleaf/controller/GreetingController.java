package org.hulei.springboot.thymeleaf.controller;

import cn.hutool.core.lang.Snowflake;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.hulei.entity.jpa.pojo.Employee;
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
    /**
     * 内存数据
     */
    List<Employee> data = new ArrayList<>();

    @PostConstruct
    private void init() {
        Faker faker = new Faker(Locale.CHINA);
        for (int i = 0; i < 45; i++) {
            data.add(new Employee()
                    .setId(new Snowflake(0, 0).nextId())
                    .setLastName(faker.name().firstName())
                    .setFirstName(faker.name().firstName())
                    .setExtension(faker.app().name())
                    .setEmail(faker.internet().emailAddress())
                    .setOfficeCode(faker.address().countryCode())
                    .setReportsTo(0)
                    .setJobTitle(faker.job().title()));
        }
    }

    @GetMapping("/employees")
    public String userQueryPage(Model model,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size) {
        if (page < 1) {
            page = 1;
        }

        PageInfo pageInfo = new PageInfo();
        List<Employee> employees = new ArrayList<>();
        for (int i = (page - 1) * size; i < 45 && employees.size() < size; i++) {
            employees.add(data.get(i));
        }

        pageInfo.setPageNum(page);
        pageInfo.setPageSize(size);
        pageInfo.setPages((data.size() / size) + 1);

        pageInfo.setNextPage((page + 1) > ((data.size() / size) + 1) ? page : (page + 1));
        pageInfo.setPrePage(Math.max((page - 1), 1));

        pageInfo.setHasPreviousPage((page - 1) > 0);
        pageInfo.setHasNextPage((page + 1) <= ((data.size() / size) + 1));

        pageInfo.setTotal(data.size());
        pageInfo.setList(employees);


        model.addAttribute("pageInfo", pageInfo);

        return "userQueryPage"; // Thymeleaf模板的名字
    }

    @Data
    static class PageInfo {
        // 当前页
        private int pageNum;
        // 每页的数量
        private int pageSize;
        // 总页数
        private int pages;

        // 前一页
        private int prePage;
        // 下一页
        private int nextPage;

        // 是否有前一页
        private boolean hasPreviousPage = false;
        // 是否有下一页
        private boolean hasNextPage = false;

        // 总记录数
        protected long total;
        // 结果集
        protected List<Employee> list;
    }
}

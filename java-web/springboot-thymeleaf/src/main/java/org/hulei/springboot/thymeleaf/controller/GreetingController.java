package org.hulei.springboot.thymeleaf.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hulei.springboot.thymeleaf.mapper.EmployeeMapper;
import org.hulei.springboot.thymeleaf.model.pojo.EmployeeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    /*
    假设你有一个用户服务来获取分页数据，
    这应该是一个实际的服务类，你需要根据你的应用程序编写它。
    */
    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("/employees")
    public String userQueryPage(Model model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            page = 1;
        }
        PageHelper.startPage(page, size);
        PageInfo<EmployeeDO> pageInfo = new PageInfo<>(employeeMapper.selectAll());
        model.addAttribute("pageInfo", pageInfo);

        return "userQueryPage"; // Thymeleaf模板的名字
    }
}

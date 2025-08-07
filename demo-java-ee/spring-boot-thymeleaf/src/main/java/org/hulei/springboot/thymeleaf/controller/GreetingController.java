package org.hulei.springboot.thymeleaf.controller;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.mybatisplus.domain.BigDataUsers;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataUserMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.com.hundsun.demo.springboot.thymeleaf.controller
 * @className: GreetingController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 13:48
 */

@RequiredArgsConstructor
@Controller
public class GreetingController {

    final BigDataUserMapper bigDataUserMapper;

    @GetMapping("/big-data-user-query")
    public String userQueryPage(Model model,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size) {

        PageHelper.startPage(page, size);
        List<BigDataUsers> list = bigDataUserMapper.selectList(Wrappers.lambdaQuery());
        PageInfo<BigDataUsers> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);

        return "userQueryPage"; // Thymeleaf模板的名字
    }

}

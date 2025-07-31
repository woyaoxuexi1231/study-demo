package org.hulei.springboot.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataUsers;
import org.hulei.entity.mybatisplus.domain.User;
import org.hulei.springboot.mybatisplus.mapper.BigDataUsersMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mybatisplus
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-08-17 10:19
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@RestController
@RequestMapping("/mybatisPlus")
public class MybatisPlusController extends ServiceImpl<BigDataUsersMapperPlus, BigDataUsers> {

    @Resource
    private BigDataUsersMapperPlus bigDataUsersMapperPlus;

    private static final Long id = 1L;

    @GetMapping("/delete")
    public void delete() {
        this.bigDataUsersMapperPlus.deleteById(id);
    }

    /**
     * mybatisPlus插件提供的分页工具 IPage
     */
    @GetMapping("/mybatisPlusIPage")
    public void mybatisPlusIPage() {
        IPage<BigDataUsers> pageFinder = new Page<>(1, 2);
        bigDataUsersMapperPlus.pageList(pageFinder);
    }

    /**
     * QueryWrapper的简单使用
     */
    @GetMapping("/selectWrapper")
    public void selectWrapper() {
        QueryWrapper<BigDataUsers> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "email").orderByAsc("id");
        List<BigDataUsers> users = bigDataUsersMapperPlus.selectList(new QueryWrapper<>());
        users.forEach(System.out::println);
    }

    /**
     * 数据库时区连接串的一些小细节问题
     */
    @PostMapping("/queryWithDate")
    @Transactional
    public void queryWithDate() {
        /*
         * 连接数据库的连接串不指定任何时区, 查出来的时间, 在 java 内存中与 mysql 实际的时间差了 13个小时
         * 但是我发现中部夏令时间(CDT)与北京时间差了正好 13个小时. CDT之前也叫 CST
         *
         * CST 北京时间, China Standard Time,又名中国标准时间 Thu Sep 28 00:53:04 CST 2023
         * GMT 格林尼治标准时间, Greenwich Mean Time Sun, 30 Aug 2020 15:09:23 GMT
         * UTC 国际协调时间, Coordinated Universal Time
         * ISO 标准时间
         *
         * 如果不指定时区, 在查询的时候, 以时间为条件得到的结果可能不正确, 所以有以时间为条件的查询sql, 连接串最好带一个时区
         */
    }

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
}

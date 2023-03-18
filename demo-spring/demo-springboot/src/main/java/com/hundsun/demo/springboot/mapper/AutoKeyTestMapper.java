package com.hundsun.demo.springboot.mapper;

import com.hundsun.demo.springboot.controller.SimpleController;
import com.hundsun.demo.springboot.service.serviceimpl.SimpleServiceImpl;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.mapper
 * @className: AutoKeyTestMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/3/18 15:36
 */

public interface AutoKeyTestMapper extends InsertListMapper<SimpleServiceImpl.AutoKeyTest> {
}

// package com.hundsun.demo.springboot.mybatisplus;
//
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import javax.annotation.Resource;
// import java.util.List;
//
// /**
//  * @ProductName: Hundsun amust
//  * @ProjectName: study-demo
//  * @Package: com.hundsun.demo.springboot.mybatisplus
//  * @Description:
//  * @Author: hulei42031
//  * @Date: 2023-08-17 10:19
//  * @UpdateRemark:
//  * @Version: 1.0
//  * <p>
//  * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
//  */
//
// @Slf4j
// @RestController
// public class UserTest {
//
//     @Resource
//     private UserMapper userMapper;
//
//     private static Long id = 1l;
//
//     @GetMapping("/delete")
//     public void delete() {
//         this.userMapper.deleteById(id);
//     }
//
//     @GetMapping("/selectList")
//     public void selectList() {
//         IPage<User> pageFinder = new Page<>(1, 2);
//         System.out.println(("----- selectAll method test ------"));
//         userMapper.pageList(pageFinder);
//         // userList.forEach(System.out::println);
//     }
//
//     @GetMapping("/selectWrapper")
//     public void selectWrapper() {
//         QueryWrapper<User> wrapper = new QueryWrapper<>();
//         wrapper.select("userId", "userName").orderByAsc("userId");
//         List<User> users = userMapper.selectList(new QueryWrapper<>());
//         users.forEach(System.out::println);
//     }
//
// }

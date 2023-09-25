package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
public class UserTest extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;

    private static Long id = 1l;

    @GetMapping("/delete")
    public void delete() {
        this.userMapper.deleteById(id);
    }

    @GetMapping("/mybatisplustest")
    @Transactional
    public void mybatisplustest() {
        // User user = new User();
        // // insert 方法选择性插入, 没有的字段是不插入的, 但是如果一个字段都没有, 会报错
        // userMapper.insert(user);

        // User update = new User();
        // update.setId(1L);
        // update.setAge(2);
        // LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        // updateWrapper.eq(User::getId, update.getId());
        // // update 方法也是选择性更新
        // userMapper.update(update, updateWrapper);

        List<User> objects = new ArrayList<>();
        // for (int i = 0; i < 10; i++) {
        //     objects.add(new User());
        // }

        // this.saveBatch(objects);

        for (int i = 1; i <= 1000; i++) {
            User build = User.builder().id((long) i).name(System.currentTimeMillis() + "").build();
            // objects.add();
            userMapper.updateById(build);
        }
        this.updateBatchById(objects);
    }


    @GetMapping("/mybatisplustest2")
    @Transactional
    public void mybatisplustest2() {

        List<User> objects = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            objects.add(User.builder().id((long) i).name(System.currentTimeMillis() + "").build());
        }
        // this.updateBatchById(objects);
        // objects.forEach(i -> i.setName(i.getName() + "a"));
        userMapper.updateBatch(objects);
    }
}

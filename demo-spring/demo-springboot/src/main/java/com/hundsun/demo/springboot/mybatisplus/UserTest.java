package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

        User update = new User();
        update.setId(1L);
        update.setName("Jone1");
        update.setAge(21);

        User update2 = new User();
        update2.setId(2L);
        update2.setName("Jack1");
        update2.setAge(22);
        objects.add(update);
        objects.add(update2);

        userMapper.updateBatch(objects);
    }
}

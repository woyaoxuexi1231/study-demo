package com.hundsun.demo.auth.service.hi.service;

import com.hundsun.demo.auth.service.hi.mapper.UserMapper;
import com.hundsun.demo.auth.service.hi.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.service.impl
 * @className: UserService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 21:06
 */

@Service
public class UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserMapper userMapper;

    public User create(User user) {
        String hash = encoder.encode(user.getPassword());
        user.setPassword(hash);
        userMapper.insertSelective(user);
        return user;
    }
}

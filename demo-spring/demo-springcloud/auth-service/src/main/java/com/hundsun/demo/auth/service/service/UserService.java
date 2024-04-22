package com.hundsun.demo.auth.service.service;

import com.hundsun.demo.auth.service.mapper.RoleMapper;
import com.hundsun.demo.auth.service.mapper.UserMapper;
import com.hundsun.demo.auth.service.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.service.impl
 * @className: UserService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 21:06
 */

@Service(value = "userDetailsService")
public class UserService implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(user);
        user.setAuthorities(roleMapper.getRoleByUserId(user.getId()));
        return user;
    }
}

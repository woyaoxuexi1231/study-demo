package org.hulei.common.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hulei.common.security.mapper.RoleMapper;
import org.hulei.common.security.mapper.UserMapper;
import org.hulei.common.security.pojo.User;
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

public class UserService implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUsername, username));
        user.setAuthorities(roleMapper.getRoleByUserId(user.getId()));
        return user;
    }
}

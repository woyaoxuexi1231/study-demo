package org.hulei.common.security.service.impl;

import org.hulei.common.security.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;


/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.service.impl
 * @className: UserService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 21:06
 */

public class MyUserService implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.selectFullInfo(username);
    }
}

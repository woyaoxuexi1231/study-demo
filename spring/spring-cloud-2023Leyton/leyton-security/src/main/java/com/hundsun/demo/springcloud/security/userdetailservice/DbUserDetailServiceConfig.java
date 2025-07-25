package com.hundsun.demo.springcloud.security.userdetailservice;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hundsun.demo.springcloud.security.mapper.RoleMapper;
import com.hundsun.demo.springcloud.security.mapper.UserMapper;
import com.hundsun.demo.springcloud.security.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author h1123
 * @since 2023/5/9 21:06
 */

@Component(value = "userDetailsService")
public class DbUserDetailServiceConfig implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
        这是 UserDetailsService 提供的唯一一个核心方法
        它的职责是根据用户名查找用户信息，并返回一个 UserDetails 对象，包含：
            - 用户名
            - 密码
            - 权限/角色信息
            - 账户状态(是否启用、过期等)

        用户尝试登录时：
            1. Spring Security 调用 UserDetailsService
            2. 通过 loadUserByUsername() 获取用户详情
            3. 将用户提供的密码与返回的 UserDetails 中的密码比对
            4. 验证其他账户状态（是否锁定、过期等）
         */
        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(Wrappers.lambdaQuery(user));
        user.setAuthorities(roleMapper.getRoleByUserId(user.getId()));
        return user;
    }
}

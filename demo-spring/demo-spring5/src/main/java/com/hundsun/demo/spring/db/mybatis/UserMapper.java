package com.hundsun.demo.spring.db.mybatis;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.db.mybatis
 * @className: UserMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/10/15 19:12
 */

public interface UserMapper {
    /**
     * 通过 id 获取用户信息
     *
     * @param id
     * @return
     */
    User getUser(int id);
}

package com.hundsun.demo.auth.service.hi.mapper;

import com.hundsun.demo.auth.service.hi.pojo.Role;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.mapper
 * @className: RoleMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 21:22
 */

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户 ID 获取用户角色信息
     *
     * @param id
     * @return
     */
    List<Role> getRoleByUserId(@Param("userId") Long id);
}

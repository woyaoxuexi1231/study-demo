package org.hulei.common.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hulei.common.security.pojo.Role;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.mapper
 * @className: RoleMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 21:22
 */

public interface SecurityRoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户 ID 获取用户角色信息
     *
     * @param id
     * @return
     */
    List<Role> getRoleByUserId(@Param("userId") Long id);
}

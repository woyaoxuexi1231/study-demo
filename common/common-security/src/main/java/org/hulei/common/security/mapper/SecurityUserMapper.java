package org.hulei.common.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.common.security.pojo.User;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.mapper
 * @className: UserMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 21:05
 */

public interface SecurityUserMapper extends BaseMapper<User> {
    /**
     * 查出完整的用户信息, 包括用户的角色信息
     *
     * @param username 用户名(唯一)
     * @return 用户信息
     */
    User selectFullInfo(String username);
}

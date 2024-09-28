package org.hulei.springboot.tkmybatis.mapper;

import org.hulei.commom.core.model.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author hulei42031
 * @since 2024-04-03 9:59
 */

@Repository
public interface TkUserMapper extends BaseMapper<User> {
}

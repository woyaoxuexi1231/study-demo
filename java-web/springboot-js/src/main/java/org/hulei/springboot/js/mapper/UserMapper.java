package org.hulei.springboot.js.mapper;

import org.hulei.commom.core.model.TkUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author hulei42031
 * @since 2024-04-03 17:13
 */

@Repository
public interface UserMapper extends BaseMapper<TkUser> {
}

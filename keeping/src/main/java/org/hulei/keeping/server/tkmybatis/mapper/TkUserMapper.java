package org.hulei.keeping.server.tkmybatis.mapper;

import com.hundsun.demo.commom.core.model.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author hulei42031
 * @since 2024-04-03 9:59
 */

@Repository
public interface TkUserMapper extends BaseMapper<User> {
}

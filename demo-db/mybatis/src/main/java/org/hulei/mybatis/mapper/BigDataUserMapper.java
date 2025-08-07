package org.hulei.mybatis.mapper;

import org.hulei.entity.jpa.pojo.BigDataUser;

import java.util.List;

/**
 * @author hulei
 * @since 2025/8/7 17:17
 */

public interface BigDataUserMapper {

    List<BigDataUser> getUserByName(String name);

    int savaOne(BigDataUser user);
}

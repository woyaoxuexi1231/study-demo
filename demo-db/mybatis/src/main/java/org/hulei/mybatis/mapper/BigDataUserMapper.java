package org.hulei.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.hulei.entity.jpa.pojo.BigDataUser;

import java.util.List;

/**
 * @author hulei
 * @since 2025/8/7 17:17
 */

public interface BigDataUserMapper {

    List<BigDataUser> getUserByName(String name);

    int savaOne(BigDataUser user);

    void batchInsert(@Param(value = "records") List<BigDataUser> records);

    List<BigDataUser> selectAll();

    @Select(value = "select * from big_data_users where name = #{name} ")
    BigDataUser selectByIdWithAnnotation(@Param(value = "name") String name);
}

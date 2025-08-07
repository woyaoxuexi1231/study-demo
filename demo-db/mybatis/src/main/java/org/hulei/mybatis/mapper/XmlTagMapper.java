package org.hulei.mybatis.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.ResultHandler;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.mybatis.spring.model.OrderWithUser;
import org.hulei.mybatis.spring.model.UserWithOrder;

import java.util.List;

public interface XmlTagMapper {

    /**
     * 简单的使用一个定义好的 ResultMap 来进行数据映射，并返回数据
     *
     * @return 雇员列表
     */
    List<BigDataUser> getEmployeeWithResultMap();

    /**
     * 使用 ResultMap + collection 标签来处理 一对多 的数据关系
     * 但是这里并不适用嵌套查询，而是直接使用笛卡尔积结果，然后进行去重统计，这种方式性能上有一定弊病
     */
    List<UserWithOrder> getDataFromResultMapWithCollection(@Param("userId") Long userId);

    /**
     * 使用 ResultMap + association 标签来处理 一对一 的数据关系
     */
    List<OrderWithUser> getDataFromResultMapWithAssociation(@Param("orderId") Long orderId);

    BigDataUser poundSign(@Param("name") String name);

    BigDataUser dollarSign(@Param("name") String name);

    List<BigDataUser> selectTagsTest();

    /**
     * mybatis使用流式查询/游标查询
     *
     * @return 雇员数据
     */
    List<BigDataUser> mybatisStreamQuery();

    /**
     * 使用ResultHandler来进行流式查询操作结果集的结果
     *
     * @param handler
     */
    void resultSetOpe(ResultHandler<BigDataUser> handler);

    @Select("SELECT * FROM big_data_users")
    @Options(fetchSize = 1)
    List<BigDataUser> fetchSize1();

    @Select("SELECT * FROM big_data_users")
    @Options(fetchSize = 100)
    List<BigDataUser> fetchSize100();

    void noXml();

    void insertGenerateKey(BigDataUser bigDataUser);
}
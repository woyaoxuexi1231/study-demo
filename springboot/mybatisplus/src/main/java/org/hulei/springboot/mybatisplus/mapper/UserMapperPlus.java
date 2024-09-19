package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hundsun.demo.commom.core.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author woaixuexi
 * @since 2024/4/2 21:23
 */

@Repository
public interface UserMapperPlus extends BaseMapper<User> {

    /**
     * 查询数据分页
     *
     * @param pageFinder 分页参数
     * @return 分页数据
     */
    IPage<User> pageList(IPage<User> pageFinder);

    /**
     * updateBatchByCaseWhen
     *
     * @param users
     */
    void updateBatchByCaseWhen(@Param("list") List<User> users);

    /**
     * updateBatch
     *
     * @param users
     */
    void updateBatch(@Param("list") List<User> users);

    /**
     * 一个没有 sql 实现的方法
     * author: hulei42031
     * date: 2023-11-23 19:47
     */
    void selectAll();

    /**
     * 插入一个用户,并返回主键
     *
     * @param user user
     */
    void insertOne(User user);
}

package com.hundsun.demo.springboot.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hundsun.demo.springboot.common.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息Mapper
 *
 * @author chendd
 * @date 2022/6/30 21:29
 */

@Repository
public interface UserMapper extends BaseMapper<User> {

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
}
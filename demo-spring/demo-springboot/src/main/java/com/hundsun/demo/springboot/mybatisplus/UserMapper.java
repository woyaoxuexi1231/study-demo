package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * updateBatch
     *
     * @param users
     */
    public void updateBatch(@Param("list") List<User> users);

    public void selectAll();
}
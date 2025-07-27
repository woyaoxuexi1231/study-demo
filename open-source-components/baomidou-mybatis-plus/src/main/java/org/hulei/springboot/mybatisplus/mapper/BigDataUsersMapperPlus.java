package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hulei.entity.mybatisplus.domain.BigDataUsers;

import java.util.List;

/**
 * @author woaixuexi
 * @since 2024/4/2 21:23
 */

public interface BigDataUsersMapperPlus extends BaseMapper<BigDataUsers> {

    /**
     * 查询数据分页
     *
     * @param pageFinder 分页参数
     * @return 分页数据
     */
    IPage<BigDataUsers> pageList(IPage<BigDataUsers> pageFinder);

    /**
     * updateBatchByCaseWhen
     *
     * @param users
     */
    void updateBatchByCaseWhen(@Param("list") List<BigDataUsers> users);

    /**
     * updateBatch
     *
     * @param users
     */
    void insertBatch(@Param("list") List<BigDataUsers> users);
    /**
     * 插入一个用户,并返回主键
     *
     * @param user user
     */
    void insertOne(BigDataUsers user);
}

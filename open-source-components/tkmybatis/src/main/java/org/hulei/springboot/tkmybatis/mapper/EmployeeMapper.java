package org.hulei.springboot.tkmybatis.mapper;

import org.hulei.common.mapper.entity.pojo.EmployeeDO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.mapper
 * @className: EmployeMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:27
 */

// 二级缓存,其实一般不使用这个
@CacheNamespace(
        implementation = PerpetualCache.class,    // 使用 PerpetualCache 作为缓存实现
        eviction = LruCache.class,                // 使用 LRU 策略清理缓存
        flushInterval = 60 * 1000,                // 每 1 分钟自动刷新缓存
        size = 512,                               // 缓存的最大存储对象数量为 512
        readWrite = true,                         // 缓存为可读写模式，保证线程安全
        blocking = true                           // 启用阻塞缓存，避免缓存穿透  同一个缓存在检测到前一个线程正在生成缓存的时候,后面一个会阻塞
)
@Repository
public interface EmployeeMapper extends BaseMapper<EmployeeDO>, ConditionMapper<EmployeeDO>, MySqlMapper<EmployeeDO> {
    /**
     * 保存
     *
     * @param employeeDO do
     */
    void saveOne(EmployeeDO employeeDO);

    /**
     * 查询所有数据,所有字段
     *
     * @return all data
     */
    List<EmployeeDO> selectAllData();

    /**
     * 通过 id 查询 name
     *
     * @param employeeNumber id
     * @return rsp
     */
    String selectLastNameById(@Param(value = "id") Long employeeNumber);

    /**
     * 查询所有数据,所有字段
     *
     * @return all data
     */
    @Select(value = "select * from employees")
    List<EmployeeDO> selectAllDataButInterface();
}

package org.hulei.mybatis.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.hulei.entity.jpa.pojo.Customer;

import java.util.List;

/**
 * @author hulei
 * @since 2024/11/16 21:34
 */

// 二级缓存,其实一般不使用这个
@CacheNamespace(
        implementation = PerpetualCache.class,    // 使用 PerpetualCache 作为缓存实现
        eviction = LruCache.class,                // 使用 LRU 策略清理缓存
        flushInterval = 60 * 1000,                // 每 1 分钟自动刷新缓存
        size = 512,                               // 缓存的最大存储对象数量为 512
        readWrite = true,                         // 缓存为可读写模式，保证线程安全
        blocking = false                           // 启用阻塞缓存，避免缓存穿透  同一个缓存在检测到前一个线程正在生成缓存的时候,后面一个会阻塞
)
public interface CustomerMapper {

    @Results({
            @Result(property = "id", column = "customer_number"),
            @Result(property = "customerName", column = "customer_name"),
            @Result(property = "contactLastName", column = "contact_last_name"),
            @Result(property = "contactFirstName", column = "contact_first_name"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "addressLine1", column = "address_line1"),
            @Result(property = "addressLine2", column = "address_line2"),
            @Result(property = "city", column = "city"),
            @Result(property = "state", column = "state"),
            @Result(property = "postalCode", column = "postal_code"),
            @Result(property = "country", column = "country"),
            @Result(property = "salesRepEmployeeNumber", column = "sales_rep_employee_number"),
            @Result(property = "creditLimit", column = "credit_limit")
    })
    @Select("select * from test_customers")
    List<Customer> selectAll();

    @Update("update test_customers set phone = #{phone} where customer_number = #{id} ")
    int updateOne(Customer customer);

    Customer selectById(@Param(value = "id") Integer id);

    @Results({
            @Result(property = "id", column = "customer_number"),
            @Result(property = "customerName", column = "customer_name"),
            @Result(property = "contactLastName", column = "contact_last_name"),
            @Result(property = "contactFirstName", column = "contact_first_name"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "addressLine1", column = "address_line1"),
            @Result(property = "addressLine2", column = "address_line2"),
            @Result(property = "city", column = "city"),
            @Result(property = "state", column = "state"),
            @Result(property = "postalCode", column = "postal_code"),
            @Result(property = "country", column = "country"),
            @Result(property = "salesRepEmployeeNumber", column = "sales_rep_employee_number"),
            @Result(property = "creditLimit", column = "credit_limit")
    })
    @Select(value = "select * from test_customers where customer_number = #{id} ")
    Customer selectByIdWithAnnotation(@Param(value = "id") Integer id);
}

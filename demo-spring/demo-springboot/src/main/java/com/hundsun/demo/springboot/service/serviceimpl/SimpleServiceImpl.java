package com.hundsun.demo.springboot.service.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.service.SimpleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.service.serviceimpl
 * @className: SimpleServiceImpl
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:32
 */

@Service
@Slf4j
public class SimpleServiceImpl implements SimpleService {

    @Resource
    EmployeeMapper employeeMapper;

    @Autowired
    SimpleServiceImpl simpleService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    // @Autowired
    // RedissonClient redissonClient;


    /**
     * mybatis占位符测试
     */
    @Override
    public void mybatis() {

        /*
        $ 和 # 号的区别
        我们都知道 #号可以防止 SQL 注入
        原理其实就是 #号占位符把位置解析成 ?, 填值的时候对特殊字符进行转移, 外加一些必要的符号, 字符串自动加单引号, 使用 PreparedStatement
        而 $ 符号不做任何处理直接写入, 这使得拥有更大的灵活性, 但是却引入了 SQL 注入
         */
        // insert into ${tableName}
        // values ( ${employeeNumber}, ${lastName}, ${firstName}, ${extension}, ${email}, ${officeCode}, ${reportsTo}, ${jobTitle})
        // values ( #{employeeNumber}, #{lastName}, #{firstName}, #{extension}, #{email}, #{officeCode}, #{reportsTo}, #{jobTitle})
        EmployeeDO employeeDO = new EmployeeDO();
        // employeeDO.setTableName("employees");
        employeeDO.setEmployeeNumber(2001L);
        employeeDO.setLastName("'mybatis'");
        employeeDO.setFirstName("'dollar'");
        employeeDO.setExtension("'?'");
        employeeDO.setEmail("'?'");
        employeeDO.setOfficeCode("'?'");
        employeeDO.setReportsTo(1002);
        employeeDO.setJobTitle("'?'");
        employeeMapper.insertWithDollar(employeeDO);
    }


    /**
     * 多数据源手动切换测试
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void getOneEmployeeDO() {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(1002L);
        System.out.println(employeeMapper.selectOne(employeeDO));
    }

    /**
     * 分页参数 startPage 的两个参数测试
     */
    @Override
    public void pageHelper() {
        /*
        pagehelper.auto-runtime-dialect=true 每次查询通过连接信息获取对应的数据源信息, 这个连接用完后关闭
        开启后, 每一次分页都会去获取连接, 根据这个连接的具体信息来开启不同的分页上下文
        PageAutoDialect.getDialect()
         */
        PageHelper.startPage(1, 10);
        employeeMapper.selectAll2();
    }
}

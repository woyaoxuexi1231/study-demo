package com.hundsun.demo.springboot.mysql;

import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.db.dynamicdb.config.coding.DataSourceTag;
import com.hundsun.demo.springboot.db.dynamicdb.core.DataSourceToggleUtil;
import com.hundsun.demo.springboot.mysql.mapper.AutoKeyTestMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mysql
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-13 20:15
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@RestController
@RequestMapping("/mysql")
public class MysqlController {

    @Resource
    EmployeeMapper employeeMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * select … for update 测试
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @SneakyThrows
    public void mysqlSelect() {

        /*
        select for update
        如果查询条件用了索引/主键，那么select … for update就会进行行锁。
        如果是普通字段(没有索引/主键)，那么select … for update就会进行锁表。
         */
        // List<EmployeeDO> employeeDOS = jdbcTemplate.query("select * from employees where lastName like '%M%' for update ", new BeanPropertyRowMapper<>(EmployeeDO.class));
        List<EmployeeDO> employeeDOS = jdbcTemplate.query("select * from employees where employeeNumber < 1600 for update", new BeanPropertyRowMapper<>(EmployeeDO.class));
        employeeDOS.forEach(i -> log.info(i.toString()));
        System.out.println("--------------------------------------------");
        // jdbcTemplate.execute("update employees set lastName = 'Murph4' where employeeNumber = 1003");

        Thread.sleep(60 * 1000);
        List<EmployeeDO> employeeDOS2 = jdbcTemplate.query("select * from employees where employeeNumber < 1600", new BeanPropertyRowMapper<>(EmployeeDO.class));
        employeeDOS2.forEach(i -> log.info(i.toString()));
        System.out.println("--------------------------------------------");

    }

    @Autowired
    private DataSourceTransactionManager transactionManager;

    /**
     * spring手动事务测试
     */
    // @Transactional(isolation = Isolation.READ_COMMITTED)
    @SneakyThrows
    public void mysqlUpdate() {

        // 手动开启事务
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);

        try {
            log.info("开始更新...");
            jdbcTemplate.execute("update employees set lastName = 'Murph3' where employeeNumber = 1003");
            // jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1003,'M','D','x6789','222@qq.com','1',1002,'sss')");
            // jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1800,'M','D','x6789','222@qq.com','1',1002,'sss')");
            // List<EmployeeDO> employeeDOS2 = jdbcTemplate.query("select * from employees where employeeNumber < 1600", new BeanPropertyRowMapper<>(EmployeeDO.class));
            // employeeDOS2.forEach(i -> log.info(i.toString()));
            // System.out.println("--------------------------------------------");
            log.info("更新完成! ");
            // Thread.sleep(10 * 1000);
            throw new RuntimeException("提交报错");
        } catch (Exception e) {
            log.error("更新异常! ", e);
            status.setRollbackOnly();
        }

        if (status.isRollbackOnly()) {
            transactionManager.rollback(status);
            log.info("回滚完成! ");
        } else {
            transactionManager.commit(status);
            log.info("提交完成! ");
        }

    }

    public void testMysqlAutoKey() {
        // 主库插入一千条数据
        // 每次插入100条, 插入10次
        /*
        数据库的 binlog_format=row, 是基于行复制的, 这里并没有出现主从复制不一致的情况

        我这里测试在从库删除一条数据之后, 是否还会进行同步, 如果操作的数据不包含主从不一致的数据对于主从复制是不影响的
        但是如果包含从库没有的数据, 那么会报错, 并停止主从同步
        通过 show slave status \G; 和 select * from performance_schema.replication_applier_status_by_worker\G;
        可以看到报错信息显示
        Coordinator stopped because there were error(s) in the worker(s). The most recent failure being: Worker 1 failed executing transaction 'ANONYMOUS' at master log mysql-bin.000025, end_log_pos 2204. See error log and/or performance_schema.replication_applier_status_by_worker table for more details about this failure or others, if any.
        Worker 1 failed executing transaction 'ANONYMOUS' at master log mysql-bin.000025, end_log_pos 2204; Could not execute Update_rows event on table test.autokeytest; Can't find record in 'autokeytest', Error_code: 1032; handler error HA_ERR_KEY_NOT_FOUND; the event's master log mysql-bin.000025, end_log_pos 2204
        主库要更新的数据在对应的表内从库是没有的
        通过 set global sql_slave_skip_counter=1; 提过这条错误, 但是主库依旧不做任何修改, 然后主从复制可以恢复, 但是如果继续操作那一条被从库删除的数据依旧会让主从复制停止
         */
        for (int i = 1; i <= 10; i++) {

            List<AutoKeyTest> list = new ArrayList<>();

            for (int j = 1; j < 100; j++) {
                AutoKeyTest autoKeyTest = new AutoKeyTest();
                autoKeyTest.setA(i + "," + j);
                autoKeyTest.setB(i + "," + j);
                list.add(autoKeyTest);
            }

            new Thread(() -> {
                DataSourceToggleUtil.set(DataSourceTag.SECOND.getTag());
                autoKeyTestMapper.insertList(list);
            }).start();

        }
    }

    @Resource
    AutoKeyTestMapper autoKeyTestMapper;
}

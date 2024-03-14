package com.hundsun.demo.spring.mybatis;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.CustomerDO;
import com.hundsun.demo.spring.db.dynamicdb.DynamicDataSourceType;
import com.hundsun.demo.spring.mybatis.mapper.CustomerMapper;
import lombok.Data;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.mybatis
 * @className: MyBatisDynamicdbServiceImpl
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/14 1:03
 */

@Data
public class MyBatisDynamicdbServiceImpl implements MyBatisDynamicdbService {

    /**
     * 自动注入 Mybatis生成的 mapperBean
     */
    private CustomerMapper customerMapper;

    @Override
    public void mybatisSpringTransaction(MyBatisOperationType myBatisOperationType, DynamicDataSourceType dynamicDataSourceType) {

        System.out.println();
        System.out.println("-------------------------------------- Spring + Mybatis --------------------------------------");

        /*
        Q1: Mybatis 是如何去拿这个动态数据源的信息的?? - 2023/02/18
        A1: 不管啥框架, 只要在 Spring 里面, 最终都是通过 Spring 来获取 connect, 对于动态数据源来说, 既然我们自己定义了多数据源的获取方法, 那么 Spring 会通过我们定义的逻辑来获取对应的数据源 - 2023/02/26
         */
        System.out.println("当前绑定的数据源为 " + dynamicDataSourceType + " - 执行 " + myBatisOperationType + " 操作");
        // select
        if (myBatisOperationType.equals(MyBatisOperationType.SELECT)) {
                /*
                pageHelper
                pageSizeZero 参数 - pageSize 为 0 的时候会查出所有数据而不进行分页, 在稍低版本中 pageNum 为 0 不会影响这个参数的使用, 稍新版本中 pageNum 为 0 不会查数据(这里使用 5.2.0 版本)
                 */
            PageHelper.startPage(1, 10);
            // 通过 spring Bean 的方式使用 Mybatis
            List<CustomerDO> customerDOS = customerMapper.selectAll();
            // customerDOS.forEach(System.out::println);
        }

        // update
        if (myBatisOperationType.equals(MyBatisOperationType.UPDATE)) {
            CustomerDO customerDO = new CustomerDO();
            customerDO.setCustomernumber(103);
            customerDO.setPhone("40.32.100");
            customerMapper.updateOne(customerDO);
            /*
            mybatis会通过isSqlSessionTransactional方法判断事务是否被spring管理
             if (!isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory)) {
              // force commit even on non-dirty sessions because some databases require
              // a commit/rollback before calling close()
              sqlSession.commit(true);
            }
            * */
            if (dynamicDataSourceType.equals(DynamicDataSourceType.SECOND)) {
                throw new RuntimeException("数据源 SECOND 更新出错!");
            }
        }
    }
}

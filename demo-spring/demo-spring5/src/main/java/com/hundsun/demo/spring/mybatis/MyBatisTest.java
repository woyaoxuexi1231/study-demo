package com.hundsun.demo.spring.mybatis;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.spring.init.listener.MybatisEvent;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.mybatisplugins
 * @className: MyBatisTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 21:59
 */
@Data
@Slf4j
public class MyBatisTest implements ApplicationListener<MybatisEvent> {

    /**
     * 自动注入 Mybatis生成的 mapperBean
     */
    private CustomerMapper customerMapper;

    @Override
    public void onApplicationEvent(MybatisEvent event) {

        System.out.println();
        System.out.println("-------------------------------------- Spring + Mybatis --------------------------------------");
        /*
        pageHelper
        pageSizeZero 参数 - pageSize 为 0 的时候会查出所有数据而不进行分页, 在稍低版本中 pageNum 为 0 不会影响这个参数的使用, 稍新版本中 pageNum 为 0 不会查数据(这里使用 5.2.0 版本)
         */
        try {

            // select
            if (event.getMyBatisOperationType().equals(MyBatisOperationType.SELECT)) {
                PageHelper.startPage(1, 10);
                // 通过 spring Bean 的方式使用 Mybatis
                List<CustomerDO> customerDOS = customerMapper.selectAll();
                // customerDOS.forEach(System.out::println);
            }

            // update
            if (event.getMyBatisOperationType().equals(MyBatisOperationType.UPDATE)) {
                CustomerDO customerDO = new CustomerDO();
                customerDO.setCustomernumber(103);
                customerDO.setPhone("40.32.25541");
            }
        } finally {
            System.out.println("-------------------------------------- Spring + Mybatis --------------------------------------");
            System.out.println();
        }
    }


    private static void staticInvoke() {

        // 读取 mybatis-config.xml 配置文件
        String resource = "mybatis-config.xml";
        InputStream in;
        try {
            in = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 加载 mybatis-config.xml 配置文件, 并创建 SqlSessionFactory 对象
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(in);
        selectAll(sessionFactory);
    }

    private static void selectAll(SqlSessionFactory sessionFactory) {

        try (SqlSession session = sessionFactory.openSession()) {
            PageHelper.startPage(1, 10);
            List<CustomerDO> customerDOS = session.selectList("com.hundsun.demo.spring.mybatis.CustomerMapper.selectAll");
        }
    }

    private static void update(SqlSessionFactory sessionFactory) {

        try (SqlSession session = sessionFactory.openSession()) {
            boolean isRollback = false;
            try {
                CustomerDO customerDO = new CustomerDO();
                customerDO.setCustomernumber(103);
                customerDO.setPhone("40.32.25541");
                session.update("com.hundsun.demo.spring.mybatis.CustomerMapper.updateOne", customerDO);
            } catch (Exception e) {
                isRollback = true;
                log.error("更新出现异常! 正在尝试回滚...", e);
            }

            if (isRollback) {
                session.rollback();
            } else {
                // mybatis 不主动提交的话, 是不会自动提交的, session关闭后会自动回滚
                session.commit();
            }
        }
    }
}

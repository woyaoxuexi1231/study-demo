package com.hundsun.demo.spring.mybatis;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.CustomerDO;
import com.hundsun.demo.spring.mybatis.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.db.mybatis
 * @className: MybatisTest
 * @description:
 * @author: h1123
 * @createDate: 2023/10/15 19:14
 */

@Slf4j
public class MybatisTest {

    public static void main(String[] args) throws IOException {
        SqlSessionFactory sessionFactory = getSessionFactory();
        // staticInvoke(sessionFactory);
        update(sessionFactory);
    }

    private static SqlSessionFactory getSessionFactory() {
        // 读取 mybatis-config.xml 配置文件
        String resource = "mybatis-config.xml";
        InputStream in;
        try {
            in = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 加载 mybatis-config.xml 配置文件, 并创建 SqlSessionFactory 对象
        return new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * 原生mybatis查询
     *
     * @param sessionFactory sessionFactory
     */
    private static void staticInvoke(SqlSessionFactory sessionFactory) {

        /*两种不同的调用方式*/
        try (SqlSession session = sessionFactory.openSession()) {
            // 对于原生Java, 使用pageHelper需要在mybatis的配置文件配置该插件
            PageHelper.startPage(1, 10);
            List<CustomerDO> customerDOS = session.selectList("com.hundsun.demo.spring.mybatis.mapper.CustomerMapper.selectAll");
            log.info("{}", customerDOS);
        }
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            // 获取Mapper
            CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
            // 调用Mapper方法
            List<CustomerDO> customerDOS = customerMapper.selectAll();
            log.info("{}", customerDOS);
        }
    }

    /**
     * 原生mybatis更新操作(包括回滚)
     *
     * @param sessionFactory sessionFactory
     */
    private static void update(SqlSessionFactory sessionFactory) {
        try (SqlSession session = sessionFactory.openSession()) {
            boolean isRollback = false;
            try {
                CustomerDO customerDO = new CustomerDO();
                customerDO.setCustomernumber(103);
                customerDO.setPhone("40.32.251");
                session.update("com.hundsun.demo.spring.mybatis.mapper.CustomerMapper.updateOne", customerDO);
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

package org.hulei.mybatis.basic;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hulei.entity.jpa.pojo.Customer;
import org.hulei.mybatis.mapper.CustomerMapper;

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
        staticInvoke(sessionFactory);
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
     * 原生 mybatis 查询
     *
     * @param sessionFactory sessionFactory
     */
    private static void staticInvoke(SqlSessionFactory sessionFactory) {

        try (SqlSession sqlSession = sessionFactory.openSession()) {
            // 这里使用 pageHelper 插件来分页, 由于没有使用 mybatis-spring 需要在 mybatis 的配置文件配置该插件
            PageHelper.startPage(1, 10);
            // 1. 使用 sqlSession.selectList + 全限定方法名来进行查询, 这种形式灵活性强, 使真正要查询的方法可以动态的传递进来
            sqlSession.selectList(CustomerMapper.class.getName() + ".selectAll").forEach(i -> log.info("{}", i));
        }

        try (SqlSession sqlSession = sessionFactory.openSession()) {
            PageHelper.startPage(1, 10);
            // 2. 使用更加符合面向对象的思想, 通过 sqlSession 获取 mapper 接口的代理实例, 然后像调用 Java 方法一样调用映射器方法, 安全性更强, 可读性更强
            CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
            // 调用 mapper 接口的方法
            customerMapper.selectAll().forEach(i -> log.info("{}", i));
        }
    }

    /**
     * 原生mybatis更新操作(包括回滚)
     *
     * @param sessionFactory sessionFactory
     */
    private static void update(SqlSessionFactory sessionFactory) {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            boolean isRollback = false;
            try {
                Customer customer = new Customer();
                customer.setId(103);
                customer.setPhone("40.32.251");
                CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
                int i = customerMapper.updateOne(customer);
                log.info("成功更新 {} 条数据", i);
            } catch (Exception e) {
                isRollback = true;
                log.error("更新出现异常! 正在尝试回滚...", e);
            }
            if (isRollback) {
                sqlSession.rollback();
            } else {
                // mybatis 不主动提交的话, 是不会自动提交的, session关闭后会自动回滚
                sqlSession.commit();
            }
        }
    }
}

package com.hundsun.demo.java.db.mybatis;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.CustomerDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
        // 读取配置文件
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        // 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 获取Mapper
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 调用Mapper方法
            User user = userMapper.getUser(1);
            log.info("{}", user);
        } finally {
            sqlSession.close();
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
            // 对于原生Java, 使用pageHelper需要在mybatis的配置文件配置该插件
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

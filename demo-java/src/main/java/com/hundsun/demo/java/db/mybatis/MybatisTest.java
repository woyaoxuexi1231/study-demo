package com.hundsun.demo.java.db.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

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
}

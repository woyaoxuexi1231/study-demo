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
    /*
    xml 文件的 sql 标签校验可能出错
    <sql id="AllColumn">
        id, name, xxx
    </sql>
    类似上诉这种会报错 <statement> or DELIMITER expected, got 'id'


    在MyBatis的XML映射文件中，`<sql>`元素用于定义可以被其他语句引用的SQL片段。你的代码看起来没有问题，应该可以正常工作。这个错误可能是由于IDEA的MyBatis插件解析XML时出现了问题。
    你可以尝试以下步骤来解决这个问题：
    1. 确保你的IDEA已经安装了最新版本的MyBatis插件。
    2. 检查你的`mybatis.xml`文件是否有语法错误或者不符合MyBatis的XML规范¹。
    3. 如果以上步骤都不能解决问题，你可以考虑在IDEA的设置中禁用MyBatis插件的SQL检查功能。
        File | Settings | Editor | Language Injections -> xml配置删除 sql 的校验

    如果你仍然遇到问题，建议你查阅MyBatis的官方文档¹，或者在相关的开发者社区（如StackOverflow²）寻求帮助。希望这些信息对你有所帮助！

    Source: Conversation with Bing, 2023/11/17
    (1) mybatis – MyBatis 3 | Mapper XML Files. https://mybatis.org/mybatis-3/sqlmap-xml.html.
    (2) How to Perform proper SQL INSERT using MyBatis-SpringBoot-MySQL in XML .... https://stackoverflow.com/questions/56246697/how-to-perform-proper-sql-insert-using-mybatis-springboot-mysql-in-xml-style.
    (3) Quick Guide to MyBatis | Baeldung. https://www.baeldung.com/mybatis.
    (4) GitHub - hhyo/mybatis-mapper2sql: Generate SQL Statements from the .... https://github.com/hhyo/mybatis-mapper2sql.
     */
}

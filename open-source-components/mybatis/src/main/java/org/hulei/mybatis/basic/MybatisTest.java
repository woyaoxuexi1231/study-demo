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

        // ============================================== 1. 配置文件加载 ============================================
        // 读取 mybatis-config.xml 配置文件
        String resource = "mybatis-config.xml";
        InputStream in;
        try {
            in = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // ============================================== 2. 配置文件解析 ============================================
        /*
        1. 解析数据库信息、mapper 信息：这个过程包括了数据库信息的解析  mapper 文件的解析，包括解析 mapper @select注解定义的sql 或者是 xml 文件内定义的sql
        2. 生成 MappedStatement 对象：用于封装一个 SQL 映射语句的所有配置信息，这些配置信息就是在注解或者xml文件中定义的各种配置，包括sql本身
        3. 绑定 Mapper 接口：动态生成 MapperProxy 代理对象，建立接口方法与 MappedStatement 的映射

        加载 mybatis-config.xml 配置文件, 并创建 SqlSessionFactory 对象
         */
        SqlSessionFactory sessionFactory =  new SqlSessionFactoryBuilder().build(in);

        // ============================================== 3. SQL执行阶段 ============================================
        /*
        1. sqlSession的创建，包含三种不同的Executor(默认是simple)，以及事务管理器的创建
        2. mapper方法的调用，这里采用了两种方式进行调用，一种直接使用 statement 的全限定名来进行调用，一种使用Mapper接口进行调用
        3. sql的解析于执行在这个阶段有比较复杂的流程。
            a. Executor 会负责创建好 Statement(jdbc中用于执行sql语句的接口)，Statement包含了sql所有的信息，sql和传入的Java参数会被ParameterHandler进行处理
               BoundSql会包含最终可执行的sql
         */
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

        /*
        这里包装了一个比较原始的使用sqlSession进行更新数据的流程，包含了异常状态的数据回滚以及正常状态的数据提交
         */
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

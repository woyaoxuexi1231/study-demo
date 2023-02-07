package com.hundsun.demo.spring.mybatis;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.spring.init.listener.SimpleEvent;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import lombok.Data;
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
public class MyBatisTest implements ApplicationListener<SimpleEvent> {

    /**
     * 自动注入 Mybatis生成的 mapperBean
     */
    private CustomerMapper customerMapper;

    @Override
    public void onApplicationEvent(SimpleEvent event) {

        // 通过 spring 的方式使用 Mybatis
        PageHelper.startPage(1, 10);
        List<CustomerDO> customerDOS = customerMapper.selectAll();
        customerDOS.forEach(System.out::println);
    }

    public void staticInvoke() {

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

    private void selectAll(SqlSessionFactory sessionFactory) {

        try (SqlSession session = sessionFactory.openSession()) {
            PageHelper.startPage(1, 10);
            List<CustomerDO> customerDOS = session.selectList("com.hundsun.demo.spring.mybatis.CustomerMapper.selectAll");
            customerDOS.forEach(System.out::println);
        }
    }
}

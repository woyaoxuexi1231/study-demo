package com.hundsun.demo.spring.mybatis;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.spring.init.listener.MybatisEvent;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import com.hundsun.demo.spring.service.YiibaidbService;
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

    private YiibaidbService yiibaidbService;

    @Override
    public void onApplicationEvent(MybatisEvent event) {

        this.yiibaidbService.mybatisSpringTransaction(event.getMyBatisOperationType(), event.getDataSourceType());
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

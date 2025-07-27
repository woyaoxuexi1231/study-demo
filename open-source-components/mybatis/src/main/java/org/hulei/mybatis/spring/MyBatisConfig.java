package org.hulei.mybatis.spring;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.mybatis.spring.plugin.MyBatisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MyBatisConfig {

    @Autowired
    public void pluginRegistry(SqlSessionFactory sqlSessionFactory, List<SqlSessionFactory> sqlSessionFactoryList) {
        // sqlSessionFactory.getConfiguration().addInterceptor(new MyBatisPlugin());
    }

}

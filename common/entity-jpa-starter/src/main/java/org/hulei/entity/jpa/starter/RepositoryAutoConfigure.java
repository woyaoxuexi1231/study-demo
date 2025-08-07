package org.hulei.entity.jpa.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author hulei
 * @since 2025/8/7 22:01
 */

@EntityScan(basePackages = {"org.hulei.entity.jpa.pojo"})
// 这个是自动引入的关键
@EnableJpaRepositories(basePackages = "org.hulei.entity.jpa.starter.dao")
// 这个仅仅只是解决了编译器 bean 报错
@ComponentScan(basePackages = "org.hulei.entity.jpa.starter.dao")
@Configuration
public class RepositoryAutoConfigure {
}

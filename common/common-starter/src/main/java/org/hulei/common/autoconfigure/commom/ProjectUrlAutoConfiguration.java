package org.hulei.common.autoconfigure.commom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * @author hulei
 * @since 2024/11/13 10:23
 */

@Configuration
public class ProjectUrlAutoConfiguration {

    @Bean
    public ApplicationRunner projectUrlRunner() {
        return new MyApplicationRunner();
    }
}

@Slf4j
class MyApplicationRunner implements ApplicationRunner, EnvironmentAware {

    private String port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        log.info("project url: http://" + inetAddress.getHostAddress() + ":{}", port);
    }

    @Override
    public void setEnvironment(Environment environment) {
        port = environment.getProperty("server.port");
    }
}

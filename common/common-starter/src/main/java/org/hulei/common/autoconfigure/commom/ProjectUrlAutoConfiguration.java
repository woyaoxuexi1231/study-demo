package org.hulei.common.autoconfigure.commom;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.net.InetAddress;

/**
 * @author hulei
 * @since 2024/11/13 10:23
 */

@Configuration
public class ProjectUrlAutoConfiguration {

    @Bean
    public ServerPortListener projectUrlRunner() {
        return new ServerPortListener();
    }
}

@Getter
@Slf4j
class ServerPortListener {

    private int port;

    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) throws Exception {
        this.port = event.getWebServer().getPort();
        log.info("获得端口：{}", port);
        log.info("project url: http://{}:{}", InetAddress.getLocalHost().getHostAddress(), port);
        log.info("swagger-ui index: http://{}:{}/swagger-ui/index.html", InetAddress.getLocalHost().getHostAddress(), port);
    }

}

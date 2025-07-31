package org.hulei.springboot.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2024/11/20 17:22
 */

@Component
@ConfigurationProperties(prefix = "jsch")
public class JschConfig {
    String sshHost;
    int sshPort;
    String sshUsername;
    String sshPassword;

    String upstreamConfigPath;

    String nginxBinPath;
    String nginxConfigPath;

    public String getSshHost() {
        return sshHost;
    }

    public void setSshHost(String sshHost) {
        this.sshHost = sshHost;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public String getSshUsername() {
        return sshUsername;
    }

    public void setSshUsername(String sshUsername) {
        this.sshUsername = sshUsername;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getUpstreamConfigPath() {
        return upstreamConfigPath;
    }

    public void setUpstreamConfigPath(String upstreamConfigPath) {
        this.upstreamConfigPath = upstreamConfigPath;
    }

    public String getNginxBinPath() {
        return nginxBinPath;
    }

    public void setNginxBinPath(String nginxBinPath) {
        this.nginxBinPath = nginxBinPath;
    }

    public String getNginxConfigPath() {
        return nginxConfigPath;
    }

    public void setNginxConfigPath(String nginxConfigPath) {
        this.nginxConfigPath = nginxConfigPath;
    }
}

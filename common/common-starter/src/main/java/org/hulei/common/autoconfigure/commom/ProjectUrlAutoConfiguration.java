package org.hulei.common.autoconfigure.commom;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author hulei
 * @since 2024/11/13 10:23
 */

@Slf4j
@Configuration
public class ProjectUrlAutoConfiguration {

    @Bean
    public ServerPortListener projectUrlRunner() {
        return new ServerPortListener();
    }

    public static String getLocalIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // 排除回环接口和未启用的接口
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // 排除 IPv6 地址和回环地址
                    if (addr instanceof Inet6Address || addr.isLoopbackAddress()) continue;
                    return addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }
        return "127.0.0.1"; // 默认回退
    }

    public static String getIPByInterface(String interfaceName) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            if (networkInterface == null) {
                throw new RuntimeException("Network interface " + interfaceName + " not found!");
            }

            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                // 排除 IPv6 和回环地址
                if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }
        throw new RuntimeException("No valid IPv4 address found for " + interfaceName);
    }

    public static String getPhysicalIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // 排除回环、未启用、虚拟网卡（如 docker、veth、virbr）
                if (iface.isLoopback() || !iface.isUp() || iface.getDisplayName().contains("docker")
                        || iface.getDisplayName().contains("veth") || iface.getDisplayName().contains("virbr")) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }
        throw new RuntimeException("No physical network interface found!");
    }

    public static void getNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            log.info("Interface: {} ({})", iface.getName(), iface.getDisplayName());

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                System.out.println("  " + addr.getHostAddress());
            }
        }
    }

    public static String getLocalHost() {
        try {
            if (InetAddress.getLocalHost().getHostName().equals("MS-7E13")) {
                return ProjectUrlAutoConfiguration.getIPByInterface("eth13");
            } else {
                return ProjectUrlAutoConfiguration.getPhysicalIP();
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // System.out.println(InetAddress.getLocalHost().getHostName());
        getNetworkInterfaces();
    }

}

@RequestMapping("/server-port-listener")
@RestController
@Getter
@Slf4j
class ServerPortListener {

    private int port;

    private final UUID uuid = UUID.randomUUID();

    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) throws Exception {
        this.port = event.getWebServer().getPort();
        log.info("获得端口：{}", port);
        log.info("project url: http://{}:{}", ProjectUrlAutoConfiguration.getLocalHost(), port);
        log.info("swagger-ui index: http://{}:{}/swagger-ui/index.html", ProjectUrlAutoConfiguration.getLocalHost(), port);
        log.info("当前应用的唯一标识：{}", uuid);
    }

    @RequestMapping("/print-network-interfaces")
    public void printNetworkInterfaces() throws SocketException {
        ProjectUrlAutoConfiguration.getNetworkInterfaces();
    }
}

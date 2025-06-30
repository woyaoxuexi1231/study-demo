package com.hundsun.demo.dubbo.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:10
 */
@MapperScan("com.hundsun.demo.dubbo.provider.mapper")
@SpringBootApplication
@Slf4j
public class ProviderApplication {

    public static void main(String[] args) {
        /*
        windows本地debug两个进程之后,后启动的服务爆出了警告
        24-04-07 11:01:36.694 WARN [main] o.a.d.c.c.FileCacheStoreFactory:  [DUBBO] Failed to create file store cache. Local file cache will be disabled. Cache file name: C:\Users\hulei42031\.dubbo\.mapping.demo-dubbo-provider.dubbo.cache, dubbo version: 3.2.7, current host: 192.168.5.1, error code: 0-3. This may be caused by inaccessible of cache path, go to https://dubbo.apache.org/faq/0/3 to find instructions.
        org.apache.dubbo.common.cache.FileCacheStoreFactory$PathNotExclusiveException: C:\Users\hulei42031\.dubbo\.mapping.demo-dubbo-provider.dubbo.cache is not exclusive. Maybe multiple Dubbo instances are using the same folder.

        ① dubbo.application.enable-file-cache
        当设置为 true 时，Dubbo 将启用文件缓存。文件缓存主要用于缓存 Dubbo 服务的元数据信息，例如服务提供者和消费者的配置信息、服务的注册信息等。启用文件缓存可以提高 Dubbo 服务的启动速度和性能，因为 Dubbo 不需要在每次启动时重新加载和解析元数据信息，而是直接从缓存文件中读取。
        当设置为 false 时，Dubbo 将禁用文件缓存，每次启动 Dubbo 服务时都会重新加载和解析元数据信息。禁用文件缓存可能会导致 Dubbo 服务启动速度较慢，但是可以确保 Dubbo 使用的是最新的元数据信息。
        修改此项配置为false之后,可以解决这个warn

        ② dubbo.meta.cache.filePath 和 dubbo.mapping.cache.filePath
        针对每个服务设置这两个配置,每个服务在不同的地方生成缓存文件
        System.setProperty("dubbo.meta.cache.filePath", dubboCachePath);
        System.setProperty("dubbo.mapping.cache.filePath", dubboCachePath);

        dubbo.registry.file
        在某些情况下，我们可能希望 Dubbo 服务不直接连接到注册中心，而是通过加载一个本地文件来模拟注册中心的行为。这在测试和调试阶段非常有用，因为可以避免对真实的注册中心进行访问，而直接使用本地文件来配置和管理服务信息。
        通过设置 dubbo.registry.file 参数，我们可以指定一个包含注册中心信息的本地文件的路径。Dubbo 将会读取该文件，并将其作为注册中心来使用，从而模拟注册中心的行为。




        关于如何在 zk 看注册信息：
·           在 zk 的 dubbo 目录下，每一个服务都会被会注册成一个目录，服务下会有提供者，消费者，已经服务的配置信息
         */
        log.info("provider开始启动");
        ApplicationContext applicationContext = SpringApplication.run(ProviderApplication.class, args);
        log.info("provider启动完成");
    }

}

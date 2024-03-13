package com.hundsun.demo.springboot.idgenerator;

import cn.hutool.core.net.NetUtil;
import com.hundsun.demo.springboot.idgenerator.SnowflakeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-07 16:18
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@RestController
@RequestMapping("/snowflake")
public class SnowFlakeController {

    @Autowired
    SnowflakeUtil snowflakeUtil;

    @GetMapping("/getId")
    public void getId() {
        System.out.println(NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()));
        System.out.println(snowflakeUtil.snowflakeId());
    }

    @GetMapping("/getId2")
    public void getId2() {
        long workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        long dataCenterId = 1;
        System.out.println(snowflakeUtil.snowflakeId(workerId, dataCenterId));
    }

    @Autowired
    CuratorFramework curatorFramework;
    /**
     * 是否初始化完成
     */
    boolean isInitialized = false;
    /**
     * 内部锁对象
     */
    private final Object object = new Object();

    @SneakyThrows
    @GetMapping("/curator")
    public void curator() {

        synchronized (object) {

            if (!isInitialized) {
                if (curatorFramework.checkExists().forPath("/snowflaketest") == null) {
                    curatorFramework.create().forPath("/snowflaketest");
                }
                // curatorFramework.create().forPath("/snowflaketest/seq-", "test".getBytes(StandardCharsets.UTF_8));
                String s = new String(curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/snowflaketest/seq-").getBytes(StandardCharsets.UTF_8));
                System.out.println(" 申请的节点为: " + s);
                String[] split = s.split("/");
                int integer = Integer.parseInt(split[split.length - 1].substring("seq-".length())) % 32;
                System.out.println("当前序号为: " + integer);
                isInitialized = true;
            }
        }
    }
}

package com.hundsun.demo.springboot.idgenerator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-07 16:19
 */

@Component
public class SnowflakeUtil {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final long workerId = 0;//为终端ID
    private final long dataCenterId = 1;//数据中心ID
    private final Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);

    // @PostConstruct
    // public void init() {
    //     workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
    //     System.out.println("当前获取的工作id为：" + workerId);
    // }

    public synchronized String snowflakeId() {
        return String.valueOf(snowflake.nextId());
    }

    public synchronized long snowflakeId(long workerId, long dataCenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}

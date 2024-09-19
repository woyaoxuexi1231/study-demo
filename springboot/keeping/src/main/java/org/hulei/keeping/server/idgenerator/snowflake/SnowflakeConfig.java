package org.hulei.keeping.server.idgenerator.snowflake;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author hulei
 * @since 2024/8/30 14:33
 */

@Slf4j
@Configuration
public class SnowflakeConfig {

    /**
     * curator工具类, 方便我们生成雪花算法的 workId
     */
    @Qualifier(value = "curatorFramework")
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
    /**
     * 雪花算法,生成64bit的long类型整数
     * 雪花算法由四部分组成 1位固定位(0)+41位时间戳+10位标识位+序列号
     * <p>
     * 1. 最高位是符号位，0 表示正，1 表示负，固定为 0，如果是 1 就是负数了。
     * 2. 存储毫秒级时间戳（41 位的长度可以使用 69 年）。
     * 3. 机器id（5bit）和 服务id（5bit）统一叫作“标识位”，两个标识位组合起来最多可以支持部署 1024 个节点。也
     * 4. 用于表示在同一毫秒内生成的多个ID的序号。如果在同一毫秒内生成的ID超过了4096个（2的12次方），则需要等到下一毫秒再生成ID。
     * <p>
     * 保证时间回拨的方式 hutool 超过两秒就直接报错了, 没超过两秒的检查上次
     */
    @Getter
    private Snowflake snowflake;

    @SneakyThrows
    @PostConstruct
    public void init() {

        synchronized (object) {
            if (!isInitialized) {
                // 检查是否有根节点,如果有根节点那么
                try {
                    curatorFramework.create().creatingParentsIfNeeded().forPath("/snowflakeGenerate");
                } catch (KeeperException.NodeExistsException e) {
                    log.warn("节点已经存在,不再重复创建,{}", e.getMessage());
                } catch (Exception e) {
                    log.error("节点创建异常, ", e);
                }
                // 创建临时有序节点,节点的顺序会由zk保证
                // curatorFramework.create().forPath("/snowflakeGenerate/seq-", "test".getBytes(StandardCharsets.UTF_8));
                String s = new String(curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/snowflakeGenerate/seq-").getBytes(StandardCharsets.UTF_8));
                log.info("申请节点号为: {}", s);
                String[] split = s.split("/");
                long workId = Long.parseLong(split[split.length - 1].substring("seq-".length())) % 32;
                log.info("当前序号(WorkId)为: {}", workId);
                isInitialized = true;

                snowflake = IdUtil.createSnowflake(workId, 0);
            }
        }
    }
}

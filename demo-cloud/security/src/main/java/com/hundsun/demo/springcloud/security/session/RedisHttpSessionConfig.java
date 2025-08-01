package com.hundsun.demo.springcloud.security.session;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * @author hulei
 * @since 2025/8/1 15:49
 */

@RequiredArgsConstructor
@EnableRedisHttpSession
public class RedisHttpSessionConfig {

    /*
    这里实际引入 RedisIndexedSessionRepository
    通过 RedisHttpSessionConfiguration 引入，RedisHttpSessionConfiguration 又通过 @EnableRedisHttpSession 启动

    启用 Redis Session 后，每一个用户 Session 会在 Redis 中产生 4 个 key（假设默认前缀是 spring:session）
        ✅ 1. spring:session:sessions:<sessionId>
            类型：Redis Hash
            作用：保存当前会话的所有属性（即 HttpSession.setAttribute() 的内容）和元数据（如创建时间、上次访问时间等）。
        ✅ 2. spring:session:sessions:expires:<sessionId>
            类型：Redis String（空值）
            作用：用于设置 Redis 中的 Key 过期时间（TTL）
            为什么需要？ Redis Hash 类型本身不能直接设置字段级 TTL，因此 Spring Session 使用这个键配合 EXPIRE 来控制会话超时。
        ✅ 3. spring:session:expirations:<epochSecond>
            类型：Redis Set
            作用：索引所有将要在某个时间戳（单位：秒）过期的 Session ID。
            Spring Session 后台使用调度任务定期清理过期会话时，会查这个 Key。
        ✅ 4. spring:session:principalName:<username>
            类型：Redis Set
            作用：保存某个用户的所有 Session ID，用于支持并发会话控制、强制下线、查看用户当前登录情况等功能。
            例子：spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:admin
     */
    private final FindByIndexNameSessionRepository sessionRepository;

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }


}

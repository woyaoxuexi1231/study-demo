package com.hundsun.demo.springcloud.security.session;

import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class RedisSessionCleanupListener implements ApplicationListener<HttpSessionDestroyedEvent> {

    private final SessionRepository<?> sessionRepository;

    public RedisSessionCleanupListener(SessionRepository<?> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        /*
        ❗这里解决的问题是：退出登录后，session状态未被正确删除而导致的后续的登录都无法正常使用的问题
        💡这里两种方式去删除 redis 的session信息问题，避免在最大会话只有 1 的时候导致退出后无法再次登录的问题
         */

        // 1️⃣ 显示调用 sessionRepository.deleteById，通过 session id 直接删除
        // String sessionId = event.getId();
        // sessionRepository.deleteById(sessionId);

        // 2️⃣ 使用 HttpSession 的 invalidate() 方法进行过期，这个方法最后依旧使用 SessionRepositoryFilter.this.sessionRepository.deleteById(getId()); 来删除
        HttpSession session = event.getSession();
        session.invalidate();
    }
}
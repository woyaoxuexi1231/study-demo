package org.hulei.basic.jdk.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MemoryLeakDemo {

    // 1️⃣ 静态集合泄漏
    private static final List<byte[]> staticList = new ArrayList<>();

    // 2️⃣ ThreadLocal 泄漏
    private static final ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();

    // 3️⃣ 伪 Listener 泄漏
    private static final List<EventListener> listeners = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("MemoryLeakDemo started. PID: " + ProcessHandle.current().pid());

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 模拟不断产生泄漏
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                while (true) {
                    // 1️⃣ 模拟静态集合泄漏 staticList 一直 add，大量对象一直挂着。
                    staticList.add(new byte[1024 * 1024]); // 1 MB

                    // 2️⃣ ThreadLocal 泄漏（ThreadLocal 没有 remove） ThreadLocal.set 之后不 remove，线程池线程复用，引用一直挂在线程里。
                    threadLocal.set(new byte[1024 * 1024]); // 1 MB

                    // 3️⃣ 注册伪 Listener 泄漏（永不 remove） 注册的监听器被静态集合 listeners 持有，没有注销。
                    listeners.add(new EventListener() {
                        private byte[] bigData = new byte[1024 * 1024]; // 1 MB

                        @Override
                        public void onEvent(String event) {
                            // do nothing
                        }
                    });

                    // 4️⃣ 模拟缓存泄漏（没有过期） Cache 用 HashMap 储存，没有过期清理。
                    Cache.put("key_" + System.nanoTime(), new byte[1024 * 1024]); // 1 MB

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    interface EventListener {
        void onEvent(String event);
    }

    // 4️⃣ 简单的伪缓存实现，没有过期策略
    static class Cache {
        private static final Map<String, Object> CACHE = new HashMap<>();

        public static void put(String key, Object value) {
            CACHE.put(key, value);
        }

        public static Object get(String key) {
            return CACHE.get(key);
        }
    }
}

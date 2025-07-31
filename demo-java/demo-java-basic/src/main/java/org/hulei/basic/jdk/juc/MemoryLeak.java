package org.hulei.basic.jdk.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/9/25 17:38
 */

public class MemoryLeak {

    static ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();

    static Map<String, Object> map = new HashMap<>();

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            2,
            4,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public static void main(String[] args) {

        // 1.静态集合内添加元素,但是长期不使用,又不释放导致内存泄漏
        map.put("key", "value");

        // 2.ThreadLocal在线程池内使用后却不清理,这可能会造成内存泄漏
        threadPoolExecutor.execute(() -> {
            stringThreadLocal.set("hello world");
        });

        // 3.未及时关闭的资源可能会导致内存泄漏,比如文件的IO流
    }
}

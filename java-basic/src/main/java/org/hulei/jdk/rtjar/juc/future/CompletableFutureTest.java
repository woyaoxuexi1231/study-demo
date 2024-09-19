package org.hulei.jdk.rtjar.juc.future;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 为了解决Thread和ThreadPoolExecutor没有返回值的问题,jdk1.5推出了future,future+callable接口可以实现获得子线程返回的执行结果
 * 但是future也存在问题,他会阻塞调用future.get()方法的线程
 * 为了解决上述future的问题,Java8推出了CompletableFuture,可以异步等待结果并且执行操作
 *
 * @author hulei
 * @since 2024/9/18 15:11
 */

@Slf4j
public class CompletableFutureTest {

    @SneakyThrows
    public static void main(String[] args) {
        CompletableFuture<String> supplyAsync = supplyAsync();
        System.out.println(supplyAsync.get());
    }

    @SneakyThrows
    public static CompletableFuture<String> supplyAsync() {
        CompletableFuture<String> stringCF = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                log.info("begin to execute the Supplier get method.");
                LockSupport.parkNanos(3L * 1000000000);
                return "supplyAsync";
            }
        });
        // 这是设置一个回调,在任务成功执行之后会调用这个回调
        stringCF.thenAccept(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
        // 调用get方法相当与一个同步方法,这里实际是Future接口的get方法
        log.info("{}", stringCF.get());
        return stringCF;
    }

    public static CompletableFuture<Void> runAsync() {
        CompletableFuture<Void> voidCF = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                log.info("begin to execute the Runnable run method.");
                System.out.println("run");
            }
        });
        voidCF.thenAccept(unused -> {
        });
        return voidCF;
    }
}

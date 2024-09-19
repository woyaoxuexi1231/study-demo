package org.hulei.jdk.rtjar.juc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hulei42031
 * @since 2024-04-26 16:00
 */

@Slf4j
public class ObjectWaitAndNotify {

    public static void main(String[] args) {
        waitAndNotify();
    }

    static final Object lock = new Object();

    @SneakyThrows
    private static void waitAndNotify() {
        Thread one = new Thread(() -> {
            synchronized (lock) {
                log.info("{}正在执行,当前线程准备执行对象的wait方法...", Thread.currentThread().getName());
                try {
                    // 这个操作会释放synchronized获取到的锁
                    // 当然使用这个方法的前提是走到这一步的时候已经获取到这个对象的锁了
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("{}正在执行,当前线程结束wait", Thread.currentThread().getName());
            }
        },"线程1");

        Thread two = new Thread(() -> {
            synchronized (lock) {
                log.info("{}正在执行,当前线程准备执行对象的notify方法...", Thread.currentThread().getName());

                // 执行完这个方法之后,并不立即释放锁,会等到方法执行完毕才释放对象锁
                lock.notify(); // 唤醒第一个在等待的线程
                // lock.notifyAll() 同时唤醒所有正在等待的线程
                log.info("{}正在执行,当前线程执行notify方法结束", Thread.currentThread().getName());
            }
        },"线程2");

        one.start();
        // Thread.sleep(1000);
        two.start();
    }

}

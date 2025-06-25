package org.hulei.basic.jdk.juc;

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
        /*
        Object.wait() - 调用该方法的线程会释放持有对象的监视器锁
        Object.wait(long timeout) 方法, 没有在执行的 timeout 时间内被唤醒会因为超时而返回, wait(0) 等价于 wait 方法
        Object.wait(long timeout, int nanos) 函数
        调用该方法必须持有对象的监视器锁, 否则会抛出 IllegalMonitorStateException 异常
        调用该方法的线程会被阻塞挂起, 直到以下情况, 会尝试重新获取监视器锁, 然后往下执行:
            1. 其他线程调用 notify() 或者 notifyAll() 方法
            2. 其他线程调用该线程的 interrupt() 方法, 该线程会抛出 InterruptedException 异常而返回
        需要注意的是, 可能会存在虚假唤醒 (虚假唤醒出现在 notifyAll() 的时候, 当多个线程同时被唤醒由只有一个能获得监视器锁的时候, 前面获得监视器锁的线程把限制条件给改掉了, 导致后面获得监视器锁的线程在错误的条件下执行)
        所以在实际使用中, 会在使用 wait 方法的地方使用 while 循环一直判断条件是否满足, 不满足则继续 wait

        Object.notify() - 调用该方法的线程会唤醒调用了持有对象的 wait 方法的线程
        Object.notifyAll() 只会唤醒在调用这个方法前 调用了 wait 系列函数而被放入共享变量等待集合里面的线程, 后面放入的是不会被唤醒的
        notify() 和 notifyAll() 也都是必须获得对象的监视器锁才能使用, 否则会抛出 IllegalMonitorStateException 异常
         */
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

package org.hulei.basic.jdk.juc;

import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hulei
 * @since 2025/7/22 13:24
 */

public class MianShiNote {

    public static void main(String[] args) {
        MianShiNote note = new MianShiNote();
        note.jiaotushuchu();
    }


    /**
     * 两个线程交替输出
     */
    @SneakyThrows
    public void jiaotushuchu() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        Thread thread1 = new Thread(() -> {
            try {
                while (atomicInteger.get() < 100) {
                    synchronized (atomicInteger) {
                        atomicInteger.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + " - " + atomicInteger.get());
                        if (atomicInteger.get() == 100) {
                            atomicInteger.notify();
                            countDownLatch.countDown();
                        } else {
                            atomicInteger.notify();
                            atomicInteger.wait();
                        }
                    }
                }
            } catch (Exception e) {

            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                while (atomicInteger.get() < 100) {
                    synchronized (atomicInteger) {
                        atomicInteger.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + " - " + atomicInteger.get());
                        if (atomicInteger.get() == 100) {
                            atomicInteger.notify();
                            countDownLatch.countDown();
                        } else {
                            atomicInteger.notify();
                            atomicInteger.wait();
                        }
                    }
                }
            } catch (Exception e) {

            }
        });

        thread1.start();
        thread2.start();

        countDownLatch.await();

        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(0);
        Thread thread3 = new Thread(() -> {
            try {
                while (atomicInteger.get() < 200) {
                    semaphore1.acquire();
                    atomicInteger.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " - " + atomicInteger.get());
                    semaphore2.release();
                }
            } catch (Exception e) {

            }
        });

        Thread thread4 = new Thread(() -> {
            try {
                while (atomicInteger.get() < 200) {
                    semaphore2.acquire();
                    atomicInteger.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " - " + atomicInteger.get());
                    semaphore1.release();
                }
            } catch (Exception e) {

            }
        });

        thread3.start();
        thread4.start();

    }

    /**
     * 不使用 Synchronization 和 Lock 实现线程安全的单例
     */
    @SneakyThrows
    public void withoutLockToSafe() {
        // 饿汉式，jvm启动就会创建这个对象。
        SimpleObject simpleObject = SimpleObject2.simpleObject;

        // 懒汉式，只有第一次主动获取对象时，才进行加载这个对象
        SimpleObject simpleObject1 = SimpleObject3.getSimpleObject();

        // 使用 AtomicReference CAS 来进行单例的创建，同样无所，使用cas自选
        SimpleObject simpleObject2 = SimpleObject4.getSimpleObject();
    }
}

class SimpleObject {
}

class SimpleObject2 {
    public static SimpleObject simpleObject = new SimpleObject();
}

class SimpleObject3 {

    static class simpleObjectHolder {
        private static SimpleObject simpleObject = new SimpleObject();
    }

    public static SimpleObject getSimpleObject() {
        return simpleObjectHolder.simpleObject;
    }
}


class SimpleObject4 {

    private static final AtomicReference<SimpleObject> atomicReference = new AtomicReference<>();

    public static SimpleObject getSimpleObject() {
        while (true) {
            SimpleObject simpleObject = atomicReference.get();
            if (Objects.nonNull(simpleObject)) {
                return simpleObject;
            }

            SimpleObject newSimpleObject = new SimpleObject();
            if (atomicReference.compareAndSet(null, newSimpleObject)) {
                return newSimpleObject;
            }
        }
    }
}
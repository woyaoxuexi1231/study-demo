package com.hundsun.demo.spring.lock;

public class SynchronizedExample {

    private int count = 0;

    /**
     * 使用synchronized关键字
     */
    public void increment() {
        // 字节码文件里可以看到这一段代码会被 MONITORENTER,MONITOREXIT 这两个关键字包裹
        synchronized (this) {
            count++; //  增加计数器的值
        }
    }

    public void doWork() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });

        t1.start();
        t2.start();

        // 等待两个线程执行完毕
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Count is: " + count);
    }

    public static void main(String[] args) {
        SynchronizedExample example = new SynchronizedExample();
        example.doWork();
    }
}

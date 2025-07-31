package org.hulei.basic.jdk.jvm;

public class SynchronizedDeadlock {
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1: Holding lock 1...");
                sleep(100);
                synchronized (lock2) {
                    System.out.println("Thread 1: Holding lock 2...");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2: Holding lock 2...");
                sleep(100);
                synchronized (lock1) {
                    System.out.println("Thread 2: Holding lock 1...");
                }
            }
        }).start();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}

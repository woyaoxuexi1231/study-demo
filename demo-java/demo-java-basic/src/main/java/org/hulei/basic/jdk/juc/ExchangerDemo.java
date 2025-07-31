package org.hulei.basic.jdk.juc;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {

    static class Producer implements Runnable {
        private final Exchanger<String> exchanger;
        private String data;

        Producer(Exchanger<String> exchanger) {
            this.exchanger = exchanger;
            this.data = "Producer Data"; // 初始数据
        }

        @Override
        public void run() {
            try {
                System.out.println("Producer has: " + data);
                // 等待与Consumer交换数据。将当前data给Consumer，并接收Consumer的data
                data = exchanger.exchange(data); // 阻塞直到Consumer到达
                System.out.println("Producer now has: " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        private final Exchanger<String> exchanger;
        private String data;

        Consumer(Exchanger<String> exchanger) {
            this.exchanger = exchanger;
            this.data = "Consumer Data"; // 初始数据
        }

        @Override
        public void run() {
            try {
                System.out.println("Consumer has: " + data);
                // 等待与Producer交换数据。将当前data给Producer，并接收Producer的data
                data = exchanger.exchange(data); // 阻塞直到Producer到达
                System.out.println("Consumer now has: " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(new Producer(exchanger)).start();
        new Thread(new Consumer(exchanger)).start();
    }
}
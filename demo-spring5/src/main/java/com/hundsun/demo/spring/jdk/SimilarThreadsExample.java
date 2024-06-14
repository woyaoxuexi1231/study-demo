package com.hundsun.demo.spring.jdk;

public class SimilarThreadsExample {

    /*
    `http-nio-13941-Acceptor@11683` 是 Tomcat 中的一个重要线程之一。它属于 Tomcat 的 NIO 线程池，并负责处理接收客户端连接的请求。具体来说，它会不断地监听 Tomcat 的网络端口，等待来自客户端的请求。
    当一个新的连接请求到达时，`http-nio-13941-Acceptor@11683` 线程将建立一个新的连接，并将其传递给 Tomcat 的工作线程池，以便后续处理。这种基于线程池的设计模式可以在多个连接中共享有限的系统资源，以提高系统的可扩展性和并发性能。
    需要注意的是，如果 Tomcat 的请求量很高，`http-nio-13941-Acceptor@11683` 线程可能会成为系统瓶颈之一。因此，在调优 Tomcat 时，我们需要配置适当的线程池大小，以平衡资源利用率和性能响应速度。
     */
    /*
    `http-nio-13941-Poller@11680` 是 Tomcat 中的一个重要线程之一。它属于 Tomcat 的 NIO 线程池，并负责监听已建立的客户端连接上的读写事件。
    具体来说，`http-nio-13941-Poller@11680` 线程会循环监听已建立的连接，等待数据可读或可写的事件发生。当有读写事件发生时，它将触发相关的回调方法进行数据的读取或写入操作，并将读写结果返回给对应的连接。
    这种基于事件驱动的模式可以使 Tomcat 在处理大量连接时具有很好的可扩展性和并发性能。通过使用 NIO 的非阻塞 IO 操作，`http-nio-13941-Poller@11680` 线程可以高效地处理多个连接，而无需为每个连接创建一个独立的线程。
    需要注意的是，如果 Tomcat 的读写操作非常频繁或连接数量过多，`http-nio-13941-Poller@11680` 线程可能会成为系统瓶颈之一。因此，在调优 Tomcat 时，我们需要根据系统的负载情况和性能需求来合理配置 NIO 线程池的大小，以获得最佳的性能表现。
     */

    public static void main(String[] args) {
        Runnable runnable = new MyRunnable();

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread Name: " + Thread.currentThread().getName());
            try {
                // 模拟线程执行耗时操作
                Thread.sleep(10000);
                methodA();
                methodB();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void methodA() {
            // 方法 A 的具体逻辑
        }

        private void methodB() {
            // 方法 B 的具体逻辑
        }
    }
}
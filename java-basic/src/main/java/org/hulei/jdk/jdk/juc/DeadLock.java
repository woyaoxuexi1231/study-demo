package org.hulei.jdk.jdk.juc;

import lombok.SneakyThrows;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分析几种死锁问题
 *
 * @author hulei
 * @since 2024/9/19 23:16
 */

public class DeadLock {

    @SneakyThrows
    public static void main(String[] args) {
        // semaphoreDeadLock();
        // synchronizedDeadLock();
        // reentrantDeadLock();
        reentrantLockUnreleased();

    }

    public static void synchronizedDeadLock() {
        Object first = new Object();
        Object second = new Object();

        new Thread(() -> {
            synchronized (first) {
                try {
                    Thread.sleep(1000);
                    synchronized (second) {

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (second) {
                try {
                    Thread.sleep(1000);
                    synchronized (first) {

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        /*
        对于这种情况,死锁的情况非常好鉴定,这是一种非常明显的死锁问题

        Found one Java-level deadlock:
        =============================
        "Thread-2":
          waiting to lock monitor 0x00000191686c6648 (object 0x0000000719145020, a java.lang.Object),
          which is held by "Thread-1"
        "Thread-1":
          waiting to lock monitor 0x00000191686c25a8 (object 0x0000000719145030, a java.lang.Object),
          which is held by "Thread-2"

        Java stack information for the threads listed above:
        ===================================================
        "Thread-2":
                at org.hulei.jdk.rtjar.juc.DeadLock.lambda$synchronizedDeadLock$1(DeadLock.java:43)
                - waiting to lock <0x0000000719145020> (a java.lang.Object)
                - locked <0x0000000719145030> (a java.lang.Object)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$20/951031848.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)
        "Thread-1":
                at org.hulei.jdk.rtjar.juc.DeadLock.lambda$synchronizedDeadLock$0(DeadLock.java:30)
                - waiting to lock <0x0000000719145030> (a java.lang.Object)
                - locked <0x0000000719145020> (a java.lang.Object)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$19/1991278377.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)

        Found 1 deadlock.
         */
    }

    public static void reentrantDeadLock() {
        ReentrantLock first = new ReentrantLock();
        ReentrantLock second = new ReentrantLock();
        new Thread(() -> {
            first.lock();
            try {
                Thread.sleep(1000);
                second.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            second.lock();
            try {
                Thread.sleep(1000);
                first.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        /*
        这里也是可以非常轻易的发现存在死锁的

        Found one Java-level deadlock:
        =============================
        "Thread-2":
          waiting for ownable synchronizer 0x0000000719147d90, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
          which is held by "Thread-1"
        "Thread-1":
          waiting for ownable synchronizer 0x0000000719147dc0, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
          which is held by "Thread-2"

        Java stack information for the threads listed above:
        ===================================================
        "Thread-2":
                at sun.misc.Unsafe.park(Native Method)
                - parking to wait for  <0x0000000719147d90> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
                at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
                at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
                at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
                at org.hulei.jdk.rtjar.juc.DeadLock.lambda$reentrantDeadLock$3(DeadLock.java:101)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$20/951031848.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)
        "Thread-1":
                at sun.misc.Unsafe.park(Native Method)
                - parking to wait for  <0x0000000719147dc0> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
                at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
                at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
                at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
                at org.hulei.jdk.rtjar.juc.DeadLock.lambda$reentrantDeadLock$2(DeadLock.java:92)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$19/1991278377.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)

        Found 1 deadlock.
         */
    }

    public static void semaphoreDeadLock() {

        Semaphore first = new Semaphore(1);
        Semaphore second = new Semaphore(1);

        new Thread(() -> {
            try {
                first.acquire();
                Thread.sleep(1000);
                second.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                second.acquire();
                Thread.sleep(1000);
                first.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        /*
        程序成功运行后,jstack可以看到线程1和线程2都处于Waiting状态,都在不同的Semaphore$NonfairSync上面处于park

        "Thread-2" #25 prio=5 os_prio=0 tid=0x0000020fc6b07800 nid=0x9a14 waiting on condition [0x0000004f86ffe000]
           java.lang.Thread.State: WAITING (parking)
                at sun.misc.Unsafe.park(Native Method)
                - parking to wait for  <0x00000005c2421930> (a java.util.concurrent.Semaphore$NonfairSync)
                at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
                at java.util.concurrent.Semaphore.acquire(Semaphore.java:312)
                at org.hulei.jdk.rtjar.juc.DeadLock.lambda$main$1(DeadLock.java:33)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$20/951031848.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)

        "Thread-1" #24 prio=5 os_prio=0 tid=0x0000020fc6af9800 nid=0x770 waiting on condition [0x0000004f86efe000]
           java.lang.Thread.State: WAITING (parking)
                at sun.misc.Unsafe.park(Native Method)
                - parking to wait for  <0x00000005c2419ac8> (a java.util.concurrent.Semaphore$NonfairSync)
                at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
                at java.util.concurrent.Semaphore.acquire(Semaphore.java:312)
                at org.hulei.jdk.rtjar.juc.DeadLock.lambda$main$0(DeadLock.java:24)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$19/1991278377.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)

        就Thread-1进行分析
        #24 - 分配的内部id号,如果通过Thread.getid就能获得这个值
        prio=5 - 线程的优先级是5,Java线程默认都是5,优先级可以通过Thread.setPriority()修改
        os_prio=0 - 操作系统分配的优先级,对于Java一般不设置特定的操作系统优先级,默认是0
        tid=0x0000020fc6af9800 - 线程的标识符,用于在操作系统中唯一标识这个线程
        nid=0x770 - 线程在操作系统的本地ID,可以在调试或性能分析时用来查找线程的具体状态
        waiting on condition [0x0000004f86efe000] -  线程当前处于“等待某种条件”的状态, 这个方括号里的地址是内存地址，指的是线程在等待某个资源（例如锁或同步器）

        这里两个semaphore引发的死锁问题不太好排查,只能看到两个线程都卡住了,通过dump文件找到两个Semaphore$NonfairSync持有的Node对象,能勉强找到两个Semaphore$NonfairSync的下一个节点就是这两个线程在互相持有
        这个排查还是需要看代码,哪里没有释放
        semaphore不同于 synchronized和ReentrantLock,这两个都有明确的 ownable synchronizers 概念，即线程可以独占持有锁
         */

    }

    public static void reentrantLockUnreleased() {
        ReentrantLock first = new ReentrantLock();
        new Thread(first::lock).start();
        new Thread(first::lock).start();
        /*
        这种情况算是比较好查的,通过jstack和jamp的dump文件,可以找到0x00000005c2400d40这个对象当前只有的线程是Thread-1,然后Thread-1又死了,就代表Thread-1没有释放锁

        "Thread-2" #25 prio=5 os_prio=0 tid=0x00000234c37f0800 nid=0x71a8 waiting on condition [0x0000005cde1ff000]
           java.lang.Thread.State: WAITING (parking)
                at sun.misc.Unsafe.park(Native Method)
                - parking to wait for  <0x00000005c2400d40> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
                at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
                at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
                at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
                at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
                at org.hulei.jdk.rtjar.juc.DeadLock$$Lambda$20/929697158.run(Unknown Source)
                at java.lang.Thread.run(Thread.java:750)

         */
    }
}

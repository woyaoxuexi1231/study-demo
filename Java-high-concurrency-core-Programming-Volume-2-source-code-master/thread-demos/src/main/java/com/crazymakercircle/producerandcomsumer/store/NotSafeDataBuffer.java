package com.crazymakercircle.producerandcomsumer.store;

import com.crazymakercircle.util.Print;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//共享数据区，类定义
class NotSafeDataBuffer<T> {
    public static final int MAX_AMOUNT = 10;
    private List<T> dataList = new LinkedList<>();

    //保存数量
    private AtomicInteger amount = new AtomicInteger(0);

    /**
     * 向数据区增加一个元素
     */
    public void add(T element) throws Exception {
        if (amount.get() > MAX_AMOUNT) {
            Print.tcfo("队列已经满了！");
            return;
        }
        dataList.add(element);
        Print.tcfo(element + "");
        amount.incrementAndGet();

        /*
         * 如果数据不一致，抛出异常
         * 会出现这种情况的原因是因为消费者和生产者在同时对 dataList 进行操作, 整个过程没有并发控制
         * dataList.add 和 amount.incrementAndGet并不是一个原子操作
         * 线程 A 执行完 amount.incrementAndGet() 后, 线程 B, 线程 C刚好同时执行了 dataList.add, 那么这里就即将报错了
         *
         */
        if (amount.get() != dataList.size()) {
            throw new Exception(amount + "!=" + dataList.size());
        }
    }

    /**
     * 从数据区取出一个元素
     */
    public T fetch() throws Exception {
        if (amount.get() <= 0) {
            Print.tcfo("队列已经空了！");
            return null;
        }
        /*
         * 这里还会爆空指针异常
         * 多个线程同时执行到这里的时候会导致最后一个报空指针
         */
        T element = dataList.remove(0);
        Print.tcfo(element + "");
        amount.decrementAndGet();
        //如果数据不一致，抛出异常, 这里和生产者报错的原理是一致的
        if (amount.get() != dataList.size()) {
            throw new Exception(amount + "!=" + dataList.size());
        }
        return element;
    }
}
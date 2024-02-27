package com.hundsun.demo.springboot.utils.segmentid;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.utils.segmentid
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-10 09:52
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Data
public class SegmentBuffer {
    /**
     * 用于标记是否有线程在更新这个 buffer
     */
    private AtomicBoolean threadRunning = new AtomicBoolean(false);
    /**
     * 用于控制更新和获取 id 的读写锁
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 当前的 buffer 对应的 key
     */
    private String key;
    /**
     * 内部持有两个 Segment, 提高并发量
     */
    private Segment[] segments = new Segment[]{new Segment(this), new Segment(this)};
    /**
     * 记录当前指向的 Segment 的下标
     */
    private volatile int currentPos = 0;
    /**
     * 当前 buffer 中的 Segment[] 下一个 segment 是否准备好
     */
    private volatile boolean nextReady = false;
    /**
     * 当前的 buffer 是否已经初始化完成
     */
    private volatile boolean initOk = false;
    /**
     * 当前 buffer 的步长
     */
    private volatile int step;
    /**
     * 当前 buffer 的最小步长
     */
    private volatile int minStep;
    /**
     * 最近一次更新的时间戳
     */
    private volatile long updateTimeStamp;

    /**
     * 获取当前的 Segment
     *
     * @return Segment
     */
    public Segment getCurrent() {
        return this.segments[currentPos];
    }

    /**
     * 获取下一个 Segment 的下标
     *
     * @return nextPos
     */
    public int nextPos() {
        return (this.currentPos + 1) % 2;
    }

    /**
     * 把当前的下标改为下一个 Segment 的下标
     */
    public void switchPos() {
        this.currentPos = this.nextPos();
    }
}

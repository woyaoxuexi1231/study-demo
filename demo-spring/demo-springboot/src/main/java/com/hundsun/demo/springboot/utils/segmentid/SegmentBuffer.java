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
    /*
     todo 当前是否有线程正在使用???
     */
    private AtomicBoolean threadRunning = new AtomicBoolean(false);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private String key;
    private Segment[] segments = new Segment[]{new Segment(this), new Segment(this)};
    private volatile int currentPos = 0;
    private volatile boolean nextReady = false;
    private volatile boolean initOk = false;
    private volatile int step;
    private volatile int minStep;
    private volatile long updateTimeStamp;

    /**
     * 获取当前的 Segment
     *
     * @return Segment
     */
    public Segment getCurrent() {
        return this.segments[currentPos];
    }

    public int nextPos() {
        return (this.currentPos + 1) % 2;
    }

    public void switchPos() {

    }
}

package com.hundsun.demo.springboot.utils.segmentid;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.utils.segmentid
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-10 09:51
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Data
public class Segment {

    /**
     * 这个是最终获取的值
     * leafAlloc.getMaxId() - buffer.getStep();
     */
    private AtomicLong value = new AtomicLong(0L);
    /**
     * leafAlloc.getMaxId()
     */
    private volatile long max;
    private volatile int step;
    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public long getIdle() {
        /*
         * leafAlloc.getMaxId() - (leafAlloc.getMaxId() - buffer.getStep()) = buffer.getStep()
         * todo 这不就是步长吗? 为啥要这么算呢?
         */
        return this.getMax() - this.getValue().get();
    }
}

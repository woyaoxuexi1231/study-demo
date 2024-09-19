package org.hulei.keeping.server.idgenerator.segmentid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.utils.segmentid
 * @Description: 真正存储分布式id的对象, 负责保存当前已生成的最大id,当前能够生成的最大id,步长
 * @Author: hulei42031
 * @Date: 2023-09-10 09:51
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Segment {

    /**
     * 当前的 segment 已生成的 id值
     * leafAlloc.getMaxId() - buffer.getStep();
     */
    private AtomicLong value = new AtomicLong(0L);
    /**
     * 当前这个 Segment 能够生成的 id 的最大值
     * leafAlloc.getMaxId()
     */
    private volatile long max;
    /**
     * 当前的 Segment 的步长
     */
    private volatile int step;
    /**
     * 当前指向的 SegmentBuffer
     */
    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * 获取当前值距离最大值的差值
     *
     * @return 差值
     */
    public long getIdle() {
        return this.getMax() - this.getValue().get();
    }
}

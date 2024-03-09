package com.hundsun.demo.springboot.utils.idgenerator.segmentid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.utils.segmentid
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-10 11:01
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LeafAlloc {
    /**
     * 表名
     */
    private String key;
    /**
     * 最大ID
     */
    private long maxId;
    /**
     * 步长
     */
    private int step;
    /**
     *
     */
    private String updateTime;
}

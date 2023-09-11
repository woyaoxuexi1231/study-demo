package com.hundsun.demo.springboot.utils.segmentid;

import lombok.Data;

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

@Data
public class LeafAlloc {

    private String key;
    private long maxId;
    private int step;
    private String updateTime;
}

package org.hulei.idgenerator.segmentid;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mapper
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-10 16:20
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public interface SequenceMapper {

    @Update(value = {"update sequence t set value = t.value + #{step}   where t.key = #{key} "})
    void update(LeafAlloc leafAlloc);

    @Select(value = {"select t.value from sequence t where t.key = #{key} "})
    Long get(@Param(value = "key") String key);

    @Select(value = {"select t.key from sequence t "})
    List<String> getAll();
}

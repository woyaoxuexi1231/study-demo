package com.hundsun.demo.springboot.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update(value = {"update sequence t set value = t.value + t.step where t.key = #{key}"})
    public void update(@Param("key") String key);

    @Select(value = {"select t.value from sequence t where t.key = #{key}"})
    public int get(@Param("key") String key);
}

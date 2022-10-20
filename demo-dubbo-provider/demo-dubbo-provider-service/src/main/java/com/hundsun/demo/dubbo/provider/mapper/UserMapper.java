package com.hundsun.demo.dubbo.provider.mapper;

import com.hundsun.demo.dubbo.provider.model.domain.UserDO;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.RowBoundsMapper;

/**
 * @projectName: dubbo-demo
 * @package: com.hundsun.dubbodemo.provider.mapper
 * @className: UserMapper
 * @description:
 * @author: h1123
 * @createDate: 2022/5/22 2:44
 * @updateUser: h1123
 * @updateDate: 2022/5/22 2:44
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public interface UserMapper extends BaseMapper<UserDO>, ExampleMapper<UserDO>, ConditionMapper<UserDO>, RowBoundsMapper<UserDO> {
}

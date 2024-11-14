package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hulei.common.mapper.entity.pojo.EmployeeDO;

import java.util.List;
import java.util.Map;

/**
 * @author hulei
 * @since 2024/11/14 17:01
 */


public interface EmployeeMapper2 {

    IPage<Map<String, Object>> getData(@Param("page") Page<?> page, @Param("sql") String sql);
}

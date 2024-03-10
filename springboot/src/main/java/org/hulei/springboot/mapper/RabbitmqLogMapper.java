package org.hulei.springboot.mapper;

import com.hundsun.demo.commom.core.model.RabbitmqLogDO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @projectName: study-demo
 * @package: org.hulei.springboot.mapper
 * @className: RabbitmqLogMapper
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 23:59
 */

@Repository
public interface RabbitmqLogMapper extends BaseMapper<RabbitmqLogDO> {
}

package org.hulei.commom.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.commom.core.model.pojo.Biguser;
import org.hulei.commom.core.model.pojo.OrderdetailsDO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BiguserMapper extends BaseMapper<Biguser> {

    int batchInsert(List<Biguser> bigusers);

    List<Biguser> fetchSize1Tags();
    List<Biguser> fetchSize100Tags();

}
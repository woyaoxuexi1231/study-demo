package org.hulei.common.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.common.mapper.entity.pojo.Biguser;

import java.util.List;

public interface BiguserMapper extends BaseMapper<Biguser> {

    int batchInsert(List<Biguser> bigusers);

    List<Biguser> fetchSize1Tags();
    List<Biguser> fetchSize100Tags();

    int insertOne(Biguser biguser);
}
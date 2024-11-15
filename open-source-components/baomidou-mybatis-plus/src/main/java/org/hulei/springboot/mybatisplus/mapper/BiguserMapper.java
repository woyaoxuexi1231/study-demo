package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.eneity.mybatisplus.domain.BigUser;

import java.util.List;

public interface BiguserMapper extends BaseMapper<BigUser> {

    int batchInsert(List<BigUser> bigUsers);

    List<BigUser> fetchSize1Tags();
    List<BigUser> fetchSize100Tags();

    int insertOne(BigUser biguser);
}
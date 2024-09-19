package org.hulei.keeping.server.spring.transactional;

import com.hundsun.demo.commom.core.model.User;
import org.hulei.springboot.mybatisplus.mapper.UserMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

abstract class ParentUserService {

    @Autowired
    UserMapperPlus userMapperPlus;

    @Transactional
    public void save(User user) {
        userMapperPlus.insertOne(user);
    }
}
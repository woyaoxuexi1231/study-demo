package org.hulei.springboot.spring.transactional;

import org.hulei.common.mapper.entity.User;
import org.hulei.common.mapper.mapper.UserMapperPlus;
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
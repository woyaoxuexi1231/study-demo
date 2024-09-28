package org.hulei.keeping.server.spring.transactional;

import org.hulei.commom.core.model.User;
import org.hulei.commom.core.mapper.UserMapperPlus;
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
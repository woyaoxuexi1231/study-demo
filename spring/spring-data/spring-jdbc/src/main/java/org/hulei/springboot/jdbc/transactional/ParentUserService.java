package org.hulei.springboot.jdbc.transactional;

import org.hulei.entity.mybatisplus.domain.User;
import org.hulei.springboot.jdbc.transactional.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

abstract class ParentUserService {

    @Autowired
    UserMapper userMapperPlus;

    @Transactional
    public void save(User user) {
        userMapperPlus.insert(user);
    }
}
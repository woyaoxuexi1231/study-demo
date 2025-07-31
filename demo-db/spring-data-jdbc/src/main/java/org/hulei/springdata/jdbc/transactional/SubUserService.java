package org.hulei.springdata.jdbc.transactional;

import org.hulei.entity.mybatisplus.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
class SubUserService extends ParentUserService {

    /**
     * Transactional注解是可以继承的,所以子类即使在重写的方法上不加这个注解,也会默认使用父类的注解
     * 要打破继承关系,那么只能覆盖父类的配置
     *
     * @param user 新增的用户信息
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void save(User user) {
        userMapperPlus.insert(user);
        throw new RuntimeException("阻止落库");
    }
}
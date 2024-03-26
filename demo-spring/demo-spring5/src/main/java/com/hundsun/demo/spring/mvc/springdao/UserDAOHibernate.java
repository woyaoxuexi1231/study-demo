package com.hundsun.demo.spring.mvc.springdao;

import com.hundsun.demo.commom.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hulei42031
 * @since 2024-03-26 12:40
 */

@Repository
public class UserDAOHibernate implements UserDAO {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    @Transactional
    public void save(User user) {
        hibernateTemplate.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return hibernateTemplate.get(User.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return hibernateTemplate.loadAll(User.class);
    }

    @Override
    @Transactional
    public void update(User user) {
        hibernateTemplate.update(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = hibernateTemplate.load(User.class, id);
        hibernateTemplate.delete(user);
    }
}


package com.hundsun.demo.spring.hibernate;

import com.hundsun.demo.commom.core.model.User;
import com.hundsun.demo.spring.mvc.springdao.UserDAO;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Setter
// @Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(User user) {
        getCurrentSession().save(user);
    }

    @Override
    public User findById(Long id) {
        return getCurrentSession().get(User.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return getCurrentSession().createQuery("FROM User").list();
    }

    @Override
    public void update(User user) {
        getCurrentSession().update(user);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            getCurrentSession().delete(user);
        }
    }

    private Session getCurrentSession() {
        // 出现报错 Exception in thread "main" org.hibernate.HibernateException: Could not obtain transaction-synchronized Session for current thread
        return sessionFactory.getCurrentSession();
    }
}
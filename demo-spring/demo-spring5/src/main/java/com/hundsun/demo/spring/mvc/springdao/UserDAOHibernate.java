package com.hundsun.demo.spring.mvc.springdao;

import com.hundsun.demo.commom.core.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author hulei42031
 * @since 2024-03-26 12:40
 */

// @Repository(value = "userDAOHibernateTarget")
// @Repository(value = "userDAOHibernate")
public class UserDAOHibernate implements UserDAO {

    private HibernateTemplate hibernateTemplate;

    private SessionFactory sessionFactory;

    @Autowired
    public void init(SessionFactory sessionFactory, HibernateTemplate hibernateTemplate) {
        // sessionFactory.getProperties().
        this.sessionFactory = sessionFactory;
        // hibernateTemplate.setCheckWriteOperations(false);
        this.hibernateTemplate = hibernateTemplate;
    }

    // @Override
    @Transactional
    public void save(User user) {
        hibernateTemplate.save(user);
    }

    // @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return hibernateTemplate.get(User.class, id);
    }

    // @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        // 这里发生了一个警告 HHH90000022: Hibernate's legacy org.hibernate.Criteria API is deprecated; use the JPA javax.persistence.criteria.CriteriaQuery instead
        return hibernateTemplate.loadAll(User.class);
    }

    // @Override
    @Transactional
    public void update(User user) {
        // hibernateTemplate.getSessionFactory().getCurrentSession().setHibernateFlushMode(FlushMode.COMMIT);
        // 这里发生了报错 Write operations are not allowed in read-only mode (FlushMode.MANUAL): Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.
        hibernateTemplate.update(user);
    }

    // @Override
    @Transactional
    public void delete(Long id) {
        User user = hibernateTemplate.load(User.class, id);
        hibernateTemplate.delete(user);
    }
}


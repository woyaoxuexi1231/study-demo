package com.hundsun.demo.spring.mvc.springdao;

import com.hundsun.demo.commom.core.model.User;

import java.util.List;

public interface UserDAO {
    void save(User user);
    User findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(Long id);
}

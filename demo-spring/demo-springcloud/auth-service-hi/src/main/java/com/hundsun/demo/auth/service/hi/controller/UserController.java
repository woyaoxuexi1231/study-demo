package com.hundsun.demo.auth.service.hi.controller;

import com.hundsun.demo.auth.service.hi.pojo.User;
import com.hundsun.demo.auth.service.hi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registry", method = RequestMethod.POST)
    public User createUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userService.create(user);
    }
}

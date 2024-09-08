package com.hundsun.demo.spring.mvc.springdao;

import com.hundsun.demo.commom.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userDAO.findAll());
        return "userList";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userDAO.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userDAO.findById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user) {
        userDAO.update(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userDAO.delete(id);
        return "redirect:/users/list";
    }
}

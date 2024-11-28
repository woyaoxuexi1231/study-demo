package com.hundsun.demo.springcloud.security.controller;

import com.hundsun.demo.springcloud.security.pojo.Blog;
import com.hundsun.demo.springcloud.security.service.impl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.controller
 * @className: BlogController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:35
 */

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping
    public ModelAndView list(Model model) {

        List<Blog> list = blogService.getBlogs();
        model.addAttribute("blogsList", list);
        return new ModelAndView("blogs/list", "blogModel", model);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  //
    @GetMapping(value = "/{id}/deletion")
    public ModelAndView delete(@PathVariable("id") Long id, Model model) {
        blogService.deleteBlog(id);
        model.addAttribute("blogsList", blogService.getBlogs());
        return new ModelAndView("blogs/list", "blogModel", model);
    }
}

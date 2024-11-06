package com.hundsun.demo.springcloud.security.service.impl;

import com.hundsun.demo.springcloud.security.pojo.Blog;
import com.hundsun.demo.springcloud.security.service.IBlogService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.service.impl
 * @className: BlogService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:23
 */

@Service
public class BlogService implements IBlogService {

    private final List<Blog> blogs = new ArrayList<>();

    @PostConstruct
    void init() {
        blogs.add(new Blog(1L, "spring in action", "good!"));
        blogs.add(new Blog(2L, "spring boot in action", "nice!"));
    }

    @Override
    public List<Blog> getBlogs() {
        return blogs;
    }

    @Override
    public void deleteBlog(long id) {
        blogs.removeIf(i -> i.getId().equals(id));
    }
}

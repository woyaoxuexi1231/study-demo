package com.hundsun.demo.springcloud.security.service;

import com.hundsun.demo.springcloud.security.pojo.Blog;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.service
 * @className: IBlogService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:22
 */

public interface IBlogService {
    /**
     * 获取博客
     *
     * @return
     */
    List<Blog> getBlogs();

    /**
     * 删除博客
     *
     * @param id
     */
    void deleteBlog(long id);
}

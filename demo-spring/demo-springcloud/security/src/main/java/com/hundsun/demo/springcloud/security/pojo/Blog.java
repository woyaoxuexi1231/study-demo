package com.hundsun.demo.springcloud.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.pojo
 * @className: Blog
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:21
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    private Long id;
    private String name;
    private String content;
}

package com.hundsun.demo.springboot.spring.lazy;

import com.hundsun.demo.springboot.SpringbootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woaixuexi
 * @since 2024/3/25 0:31
 */

@RequestMapping("/lazy")
@RestController
public class LazyController {

    @GetMapping("/test")
    public void test() {
        LazyBean bean = SpringbootApplication.applicationContext.getBean(LazyBean.class);
        bean.print();
    }
}

package org.hulei.springboot.spring.lazy;

import org.springframework.context.annotation.Lazy;
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

    LazyBean lazyBean;

    @Lazy
    // @Autowired
    public void setLazyBean(LazyBean lazyBean) {
        System.out.println("LazyController开始加载lazyBean");
        this.lazyBean = lazyBean;
    }

    @GetMapping("/test")
    public void test() {
        // LazyBean bean = SpringbootApplication.applicationContext.getBean(LazyBean.class);
        lazyBean.print();
    }
}

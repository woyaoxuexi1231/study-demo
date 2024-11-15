package org.hulei.springboot.spring.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hulei
 * @since 2024/9/17 17:42
 */

// 标识当前类作为一个控制器被注册成Bean, 和@Component,@Service作用类似,不过为了区分不同类型的Bean,使用这个更好
@Controller
public class MvcController {

    /**
     * 如果收到根路径请求 / 的 Http Get 请求, 返回一个 home 的字符串
     * 随后会由视图解析器找到对应的jsp或者模板之后再由 DispatcherServlet 返回渲染好之后的视图到前台
     *
     * @return 视图名
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }
}

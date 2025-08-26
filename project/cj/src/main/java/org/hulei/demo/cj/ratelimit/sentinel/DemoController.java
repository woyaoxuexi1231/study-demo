package org.hulei.demo.cj.ratelimit.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/hello")
    @SentinelResource(value = "helloResource", blockHandler = "handleHelloBlock")
    public String hello() {
        return "Hello, Sentinel!";
    }

    // 限流时的处理逻辑
    public String handleHelloBlock(BlockException ex) {
        return "请求过于频繁，请稍后再试";
    }
}

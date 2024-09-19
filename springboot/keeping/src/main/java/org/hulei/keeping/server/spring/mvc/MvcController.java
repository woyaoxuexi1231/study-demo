package org.hulei.keeping.server.spring.mvc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author hulei
 * @since 2024/9/17 17:42
 */

@RestController(value = "/mvcController")
public class MvcController {

    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map) {
        // 设置时区为东八区（北京时间）
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        // 获取当前时间
        Date now = new Date();
        // 根据指定时区获取当前时间
        Date nowInTimeZone = new Date(now.getTime() + timeZone.getRawOffset());
        map.put("response-tag", nowInTimeZone);
        return map;
    }
}

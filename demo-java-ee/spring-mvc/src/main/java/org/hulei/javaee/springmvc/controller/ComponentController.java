package org.hulei.javaee.springmvc.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hulei
 * @since 2023/4/10 22:35
 */

@Controller
public class ComponentController {

    @RequestMapping(value = "/getJson")
    @ResponseBody
    public String getJson() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", ", world");
        return JSONObject.toJSONString(map);
    }
}

package com.hundsun.demo.springcloud.eureka.client.controller;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.model.req.SimpleReqDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.client.controller
 * @className: HiController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 22:53
 */

@RestController
public class HiController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hi")
    public String hi(HttpServletRequest req, HttpServletResponse rsp) {
        return "here is " + port + " and your ip is " + req.getRemoteAddr();
    }

    /**
     * 普通的 URL 格式, 参数通过 hi2?req=xxx 带入请求
     * 参数名必须一致
     *
     * @param req  req
     * @param req2 req2
     * @return rsp
     */
    @GetMapping("/hi2")
    public ResultDTO<?> hi2(@RequestParam(required = false, name = "req") String req, @RequestParam(required = false, name = "req2") String req2) {
        // throw new RuntimeException("error");
        return ResultDTOBuild.resultSuccessBuild("OK, im receive the msg : " + req + " + " + req2);
    }

    /**
     * 请求中带有占位符的 URL
     * 注解 @PathVariable 绑定参数中的占位符
     * 参数一个不能少
     *
     * @param req 与占位符名字对应的参数
     * @return result
     */
    @GetMapping(path = {"/hi3/{req}/{other}"}, name = "Restful 风格的 URL 请求")
    public ResultDTO<?> hi3(@PathVariable(name = "req") String req, @PathVariable(name = "other") String other) {
        return ResultDTOBuild.resultSuccessBuild("OK, im receive the msg : " + req + ", " + other);
    }

    /**
     * 当参数过多, 考虑用对象来接收请求
     *
     * @param req 对象参数
     * @return result
     */
    @GetMapping("/hi4")
    public ResultDTO<?> hi4(SimpleReqDTO req) {
        return ResultDTOBuild.resultSuccessBuild("OK, im receive the msg : " + req.toString());
    }

    @PostMapping("/hi5")
    public ResultDTO<?> hi5(String req) {
        return ResultDTOBuild.resultSuccessBuild("OK, im receive the msg : " + req);
    }

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

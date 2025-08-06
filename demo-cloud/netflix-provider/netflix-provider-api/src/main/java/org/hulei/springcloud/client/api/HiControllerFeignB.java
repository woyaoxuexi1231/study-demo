package org.hulei.springcloud.client.api;

import org.hulei.util.dto.SimpleReqDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hulei
 * @since 2025/7/29 16:15
 */

/*
不应该这样声明 FeignClient
如果这样声明 FeignClient ，客户端显然需要传递 HttpServletRequest 和 HttpServletResponse

而且 FeignClient 初始化的时候会认为 HttpServletRequest 和 HttpServletResponse 是请求体body
所以初始化的时候会报错
Method has too many Body parameters: public abstract java.lang.String org.hulei.springcloud.client.api.HiControllerFeign.hiWithPathParam(java.lang.String,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
这种情况就最好客户端自己写
 */
// @FeignClient(name = "${spring.application.name}", path = "/client")
// @Service
@Deprecated
public interface HiControllerFeignB {


    @GetMapping("/hi")
    public String hi(HttpServletRequest req, HttpServletResponse rsp);

    @GetMapping("/hi-request-param")
    public String hiWithRequestParam(@RequestParam(required = false, name = "param") String param,
                                           HttpServletRequest req, HttpServletResponse rsp);

    @GetMapping(path = {"/hi-path-param/{param}"}, name = "Restful 风格的 URL 请求")
    public String hiWithPathParam(@PathVariable(name = "param") String param,
                                  HttpServletRequest req, HttpServletResponse rsp);

    @GetMapping("/hi-databinding")
    public String hiDataBinding(SimpleReqDTO reqDTO,
                                HttpServletRequest req, HttpServletResponse rsp);

    @PostMapping("/hi-body")
    public String hiBody(@RequestBody SimpleReqDTO reqDTO,
                         HttpServletRequest req, HttpServletResponse rsp);

    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map,
                                      HttpServletRequest req, HttpServletResponse rsp);
}

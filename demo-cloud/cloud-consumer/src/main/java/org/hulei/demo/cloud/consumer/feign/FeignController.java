package org.hulei.demo.cloud.consumer.feign;

import org.hulei.util.dto.SimpleReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/7/31 14:10
 */

@RequestMapping("/feign")
@RestController
public class FeignController {

    @Autowired
    HiControllerFeign hiControllerFeign;

    @GetMapping("/hi")
    public void hi(){

        System.out.println(hiControllerFeign.hi());

        System.out.println(hiControllerFeign.hiWithRequestParam("你好"));

        System.out.println(hiControllerFeign.hiWithPathParam("你好2"));

        System.out.println(hiControllerFeign.hiDataBinding(new SimpleReqDTO("asd")));

        System.out.println(hiControllerFeign.hiBody(new SimpleReqDTO("阿萨飒飒")));
    }
}

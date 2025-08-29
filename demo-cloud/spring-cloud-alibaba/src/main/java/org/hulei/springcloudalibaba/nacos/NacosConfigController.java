package org.hulei.springcloudalibaba.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/8/28 15:57
 */

@RestController
public class NacosConfigController {

    @Value("${remote-config:null}")
    public String remoteConfig;

    @RequestMapping(value = "/getRemoteConfig", method = RequestMethod.GET)
    public String getRemoteConfig() {
        return String.format("remoteConfig: %s", remoteConfig);
    }
}

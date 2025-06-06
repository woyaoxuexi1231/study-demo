package com.hundsun.demo.springcloud.eureka.ribbon.client.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.hulei.util.dto.ResultDTO;
import org.hulei.util.dto.SimpleReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.ribbon.client.service
 * @className: RibbonService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 23:04
 */

@Deprecated
@Slf4j
@Service
public class RibbonService {


}

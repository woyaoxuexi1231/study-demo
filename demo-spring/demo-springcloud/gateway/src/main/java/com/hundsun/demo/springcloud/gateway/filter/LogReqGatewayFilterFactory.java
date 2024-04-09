// package com.hundsun.demo.springcloud.gateway.filter;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cloud.gateway.filter.GatewayFilter;
// import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
// import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
// import reactor.core.publisher.Mono;
//
// import java.util.Arrays;
// import java.util.List;
//
// @Slf4j
// // @Component
// public class LogReqGatewayFilterFactory extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {
//
//     @Autowired
//     private ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory;
//
//     public LogReqGatewayFilterFactory() {
//         super(NameConfig.class);
//     }
//
//     public List<String> shortcutFieldOrder() {
//         return Arrays.asList("name");
//     }
//
//
//     public GatewayFilter apply(NameConfig config) {
//         String logType = config.getName();
//         ModifyRequestBodyGatewayFilterFactory.Config modConfig = modifyRequestBodyGatewayFilterFactory.newConfig();
//         modConfig.setRewriteFunction(String.class, String.class, (exchange, s) -> {
//             if (s == null) {
//                 System.out.println("null");
//                 log.info("s is null");
//                 return Mono.empty();
//             }
//             System.out.println("null");
//             String gid = "exchange.getRequest().getHeaders().getFirst(Constant.FILTER_CONFIG_LOG_FLAG);";
//             log.info("[{}]{}收到请求,path:{},body:{}", gid, logType, exchange.getRequest().getURI().getRawPath(), s);
//             return Mono.just(s);
//         });
//         return modifyRequestBodyGatewayFilterFactory.apply(modConfig);
//     }
// }
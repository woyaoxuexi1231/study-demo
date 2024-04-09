// package com.hundsun.demo.springcloud.gateway.filter;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.reactivestreams.Publisher;
// import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// import java.util.Map;
//
// public class SomeRewriteFunction implements RewriteFunction<String, String> {
//
//     private final Map<String, String> values;
//
//     public SomeRewriteFunction(Map<String, String> values) {
//         this.values = values;
//     }
//
//     @Override
//     public Publisher<String> apply(ServerWebExchange serverWebExchange, String oldBody) {
//         /* do things here */
//         /* example: */
//         try {
//             String newBody = new ObjectMapper().writeValueAsString(values);
//             return Mono.just(newBody);
//         } catch (Exception e) {
//             /* error parsing values to json, do something else */
//             return Mono.just(oldBody);
//         }
//         // return Mono.just(oldBody);
//     }
// }
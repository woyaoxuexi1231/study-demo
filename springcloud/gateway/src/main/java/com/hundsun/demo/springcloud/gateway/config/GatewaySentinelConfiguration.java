package com.hundsun.demo.springcloud.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// @Configuration
public class GatewaySentinelConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewaySentinelConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                        ServerCodecConfigurer serverCodecConfigurer) {

        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {

        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {

        return new SentinelGatewayFilter();
    }

    /**
     * Spring 容器初始化的时候执行该方法
     */
    @PostConstruct
    public void doInit() {
        initCustomizedApis();
        // 加载网关限流规则
        initGatewayRules();
        // 加载自定义限流异常处理器
        initBlockHandler();
    }

    /**
     * 网关限流规则
     * 建议直接在 Sentinel 控制台上配置
     */
    private void initGatewayRules() {

        // Set<GatewayFlowRule> rules = new HashSet<>();
        // /*
        //     resource：资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称
        //     count：限流阈值
        //     intervalSec：统计时间窗口，单位是秒，默认是 1 秒
        //  */
        // // rules.add(new GatewayFlowRule("order-service")
        // //         .setCount(3) // 限流阈值
        // //         .setIntervalSec(60)); // 统计时间窗口，单位是秒，默认是 1 秒
        // // --------------------限流分组----------start----------
        // rules.add(new GatewayFlowRule("url-proxy-1")
        //         .setCount(1) // 限流阈值
        //         .setIntervalSec(1)); // 统计时间窗口，单位是秒，默认是 1 秒
        // rules.add(new GatewayFlowRule("api-service")
        //         .setCount(5) // 限流阈值
        //         .setIntervalSec(1)); // 统计时间窗口，单位是秒，默认是 1 秒
        // // --------------------限流分组-----------end-----------
        // // 加载网关限流规则
        // GatewayRuleManager.loadRules(rules);
        // // 加载限流分组
        // initCustomizedApis();
        //
        // rules.add(new GatewayFlowRule("eureka-client-api")
        //         .setCount(1) // 限流阈值
        //         .setIntervalSec(1)); // 统计时间窗口，单位是秒，默认是 1 秒

        // // ---------------熔断-降级配置-------------
        // DegradeRule degradeRule = new DegradeRule("api-service") // 资源名称
        //         .setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO) // 异常比率模式(秒级)
        //         .setCount(0.5) // 异常比率阈值(50%)
        //         .setTimeWindow(10); // 熔断降级时间(10s)
        // // 加载规则.
        // DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));


        List<FlowRule> rules = new ArrayList<>();
        FlowRule orderApiRule2 = new FlowRule();
        orderApiRule2.setResource("yourApi");
        orderApiRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        orderApiRule2.setCount(1);
        rules.add(orderApiRule2);
        // 加载限流规则
        FlowRuleManager.loadRules(rules);

        Set<GatewayFlowRule> gatewayFlowRules = new HashSet<>();
        GatewayFlowRule eurekarule = new GatewayFlowRule();
        eurekarule.setResource("eureka-client");
        eurekarule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        eurekarule.setCount(1);
        gatewayFlowRules.add(eurekarule);
        GatewayRuleManager.loadRules(gatewayFlowRules);

        // ParamFlowRule rule = new ParamFlowRule("eureka-client")
        //         .setParamIdx(0)  // 限流基于第一个参数 (user)
        //         .setCount(1)    // 每秒最多允许10次请求
        //         .setGrade(RuleConstant.FLOW_GRADE_QPS);  // 基于QPS的限流
        // 加载限流规则
        // ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }

    /**
     * 自定义限流异常处理器
     */
    private void initBlockHandler() {

        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {

            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {

                Map<String, String> result = new HashMap<>(3);
                result.put("code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
                result.put("message", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
                result.put("x", "xx");
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        //.body(BodyInserters.fromValue(result));
                        .body(BodyInserters.fromObject(result));
            }
        };
        // 加载自定义限流异常处理器
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    /**
     * 分组限流
     */
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();

        // 定义一个API group，命名为 "order_api"
        // ApiDefinition orderApi = new ApiDefinition("order_api")
        //         .setPredicateItems(new HashSet<ApiPredicateItem>() {{
        //             // 将 `/api/order/create` 作为此API组的一部分
        //             add(new ApiPathPredicateItem().setPattern("/api/order/create"));
        //             // 将 `/api/order/update` 作为此API组的一部分
        //             add(new ApiPathPredicateItem().setPattern("/api/order/update"));
        //         }});

        ApiDefinition eureka = new ApiDefinition("eureka-client")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/eureka-client/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        // definitions.add(orderApi);
        definitions.add(eureka);

        // 将定义的API组加载到ApiDefinitionManager
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}

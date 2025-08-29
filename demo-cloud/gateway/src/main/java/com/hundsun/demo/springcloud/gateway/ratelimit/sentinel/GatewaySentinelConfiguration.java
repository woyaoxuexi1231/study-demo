package com.hundsun.demo.springcloud.gateway.ratelimit.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
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
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
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

@Slf4j
@Configuration
public class GatewaySentinelConfiguration {


    /* ================================================ sentinel配置 ============================================= */

    /*
    使用时只需注入对应的 SentinelGatewayFilter 实例以及 SentinelGatewayBlockExceptionHandler 实例即可（若使用了 Spring Cloud Alibaba Sentinel，则只需按照文档进行配置即可，无需自己加 Configuration）
     */

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
        // 加载自定义限流异常处理器
        initBlockHandler();

        initFlowRules();
        initDegradeRules();
        // 加载网关限流规则
        initGatewayRules();
        initParamFlowRules();
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

        /*
        ApiDefinition是用来 对资源（通常是接口/方法）进行逻辑分组与命名 的一种机制
        基于 API 路径、业务含义等，将多个底层资源（如 Controller 方法、URL 等）抽象成一个具有业务语义的 API 分组，便于更直观地进行流量控制、监控和管理。
        简单来说：
          - 默认情况下，Sentinel 是基于 资源名称（如方法名、URL 路径等） 来识别“什么是需要控制的资源”。
          - 但如果你有成百上千个接口，直接使用原始方法名（如 com.example.controller.UserController.getUserById）去配置规则会非常难管理。
          - 这时，你可以使用 ApiDefinition，把多个接口按业务维度（如 /user/, /order/）进行分组，并为每一组定义一个 易读的 API 名称（如 "获取用户信息"、"创建订单"），然后基于这个 API 分组 去配置限流、熔断等规则，而不是直接针对每个底层方法。

         */


        Set<ApiDefinition> definitions = new HashSet<>();

        ApiDefinition genImages = new ApiDefinition("genImages")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/genimages/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        ApiDefinition netflixCommonHi = new ApiDefinition("netflix-common-hi")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/netflix-provider/common/hi/baz"));
                    add(new ApiPathPredicateItem().setPattern("/netflix-provider/common/hi/sss")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        ApiDefinition netflixDegradeRulesApi = new ApiDefinition("netflixDegradeRulesApi")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/netflix-provider/common/hi/degrade"));
                }});

        ApiDefinition sentinelHiParamRulesApi = new ApiDefinition("sentinelHiParamRulesApi")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(
                            new ApiPathPredicateItem().setPattern("/netflix-provider/common/hi-param/*")
                                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX)
                    );
                }});

        ApiDefinition sentinelHiRulesApi = new ApiDefinition("sentinelHiRulesApi")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(
                            new ApiPathPredicateItem().setPattern("/netflix-provider/common/hi-sentinel/*")
                                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX)
                    );
                }});


        definitions.add(netflixCommonHi);
        definitions.add(genImages);
        definitions.add(netflixDegradeRulesApi);
        definitions.add(sentinelHiParamRulesApi);
        definitions.add(sentinelHiRulesApi);

        // 将定义的API组加载到ApiDefinitionManager
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    /**
     * 初始化 FlowRules 配置
     */
    private void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule orderApiRule2 = new FlowRule();
        orderApiRule2.setResource("netflix-common-hi");
        orderApiRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        orderApiRule2.setCount(5);
        rules.add(orderApiRule2);

        // 对 genImages 分组进行整体的限流操作
        FlowRule genImagesFlowRule = new FlowRule();
        genImagesFlowRule.setResource("genImages");
        genImagesFlowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        genImagesFlowRule.setCount(1);
        rules.add(genImagesFlowRule);

        // 加载限流规则
        FlowRuleManager.loadRules(rules);
    }

    /**
     * 初始化 DegradeRules 配置
     */
    private void initDegradeRules() {
        // ---------------熔断-降级配置-------------
        DegradeRule degradeRule = new DegradeRule("netflixDegradeRulesApi") // 资源名称

                // .setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO) // 异常比率模式(秒级)
                // .setCount(0.5) // 异常比率阈值(50%)
                // .setTimeWindow(10); // 熔断降级时间(10s)

                .setGrade(RuleConstant.DEGRADE_GRADE_RT) // RT（响应时间）模式
                .setCount(0.1) // 平均响应时间超过 200ms 进入熔断
                .setTimeWindow(10) // 熔断时长 10 秒
                ;

        // 加载规则.
        DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
    }

    /**
     * 网关限流规则
     * 建议直接在 Sentinel 控制台上配置
     */
    private void initGatewayRules() {

        /*
        Sentinel 为 Spring Cloud Gateway 提供了 网关流量控制的能力，让我们可以对 不同的路由（route） 或 API 路径 进行 网关层面的限流
          - 限制某个 API 路径（如 /api/user/）的 QPS
          - 限制某个微服务下游（如 user-service）的流量
          - 针对网关的路由维度做 限流、熔断等控制
        Sentinel 提供的 网关限流规则类：GatewayFlowRule
         */
        Set<GatewayFlowRule> rules = new HashSet<>();
        /*
            resource：资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称
            count：限流阈值
            intervalSec：统计时间窗口，单位是秒，默认是 1 秒
         */
        GatewayFlowRule rule = new GatewayFlowRule();
        rule.setResource("sentinelHiRulesApi");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(1);
        rule.setIntervalSec(10);
        // rule.setParamItem(new GatewayParamFlowItem()
        //         .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
        //         .setFieldName("userId")); // 取 query 参数 userId

        rules.add(rule);
        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 热点规则限流 TODO
     */
    public void initParamFlowRules() {
        /*
        ParamFlowRule（参数流控规则）是 Sentinel 提供的一种高级限流规则
        用于针对请求中的某些参数（如用户ID、商品ID、IP等）进行细粒度的流量控制，也就是 “热点参数限流”。

        这个非常关键的一点是，方法必须要有参数，不然无法进行限流，相当于无法找到符合方法签名的方法
        这里就有一个非常难搞的点，目前测试下来，这个限流规则和网关服务发现的接口就根本无法配合，因为无法知道下游的方法
        这个只能配合本地方法使用
         */
        ParamFlowRule rule = new ParamFlowRule("yourApi")
                .setGrade(RuleConstant.FLOW_GRADE_QPS) // 限流模式，QPS 模式
                .setParamIdx(0) // 参数索引，表示方法的第几个参数（从0开始）
                .setCount(1) // 限流阈值，例如每秒最多5次
                .setDurationInSec(10) // 统计窗口时长（秒）
                // .setMaxQueueSize(0) // 匀速排队模式下的最大等待队列长度
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT) // 流控效果，直接拒绝
                // 可选：针对特定参数值设置不同的阈值
                .setParamFlowItemList(Collections.singletonList(
                        new ParamFlowItem()
                                .setObject("specificValue") // 参数的特定值
                                .setClassType(String.class.getName())
                                .setCount(2) // 该特定值的限流阈值
                ));

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));

        log.info("初始化 ParamFlowRule 完成");
    }


    @Bean
    public RouteLocator mySentinelRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hi_everything_route",
                        r -> r.path("/netflix-provider/common/hi/**")
                                .filters(f -> f.stripPrefix(1))  // 去掉 1 个前缀，/netflix-provider将被去掉
                                .uri("lb://NETFLIX-PROVIDER")
                )

                // .route("sentinel_hi_abc_route",
                //         r -> r.order(-1).path("/netflix-provider/common/hi-sentinel/abc")
                //                 .filters(f -> f.stripPrefix(1))  // 去掉 1 个前缀，/netflix-provider将被去掉
                //                 .uri("lb://NETFLIX-PROVIDER"))

                // .route("sentinel_hi_param_route",
                //         r -> r.path("/netflix-provider/common/hi-param/**")
                //                 .filters(f -> f.stripPrefix(1))  // 去掉 1 个前缀，/netflix-provider将被去掉
                //                 .uri("lb://NETFLIX-PROVIDER"))

                .build();

    }
}

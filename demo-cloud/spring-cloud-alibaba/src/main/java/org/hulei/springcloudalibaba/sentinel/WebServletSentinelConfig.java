package org.hulei.springcloudalibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

// @Configuration
public class WebServletSentinelConfig {

    @Bean
    public FilterRegistrationBean sentinelFilterRegistration() {

        // FilterRegistrationBean 是 Spring Boot 提供的一个用于 注册 Servlet Filter 的类
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        // CommonFilter 是 Sentinel 官方为 Servlet 容器（如 Tomcat） 提供的一个标准 Filter 实现
        // 它会拦截请求，并根据请求的 URI（或其它资源名）应用 Sentinel 的流量控制规则（如 QPS 限流）。
        registration.setFilter(new CommonFilter());
        registration.addUrlPatterns("/*"); // 拦截 所有的 Web 请求
        registration.setName("sentinelFilter"); // 设置 Filter 的名称
        registration.setOrder(1); // 优先级
        
        return registration;
    }

    @PostConstruct
    public void init(){
        initFlowRules();
    }

    /**
     * 初始化限流规则
     */
    private void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        // 规则针对的资源名称，通常是请求的路径，比如 "/api/test"
        String resourceName = "/api/hello";

        FlowRule rule = new FlowRule(resourceName)
                .setGrade(RuleConstant.FLOW_GRADE_QPS) // 限流阈值类型：QPS
                .setCount(1); // 阈值：每秒最多 10 次请求

        rules.add(rule);
        FlowRuleManager.loadRules(rules); // 加载规则
    }
}
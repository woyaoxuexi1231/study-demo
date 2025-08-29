package org.hulei.springcloudalibaba.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hulei
 * @since 2025/8/29 14:00
 */

@Slf4j
@Configuration
public class MySentinelConfig {

    @PostConstruct
    public void initFlowRule() {
        log.info("开始初始化 sentinel 配置信息");

        List<FlowRule> rules = new ArrayList<>();

        FlowRule rule = new FlowRule();
        rule.setResource("hi-body"); // 对应 @SentinelResource 的 value
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 按 QPS 限流
        rule.setCount(1); // 每秒最多 2 次请求

        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}

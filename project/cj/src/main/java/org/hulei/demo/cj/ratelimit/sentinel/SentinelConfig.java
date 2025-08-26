package org.hulei.demo.cj.ratelimit.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentinelConfig implements CommandLineRunner {

    @Override
    public void run(String... args) {
        List<FlowRule> rules = new ArrayList<>();

        FlowRule rule = new FlowRule();
        rule.setResource("helloResource"); // 对应 @SentinelResource 的 value
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 按 QPS 限流
        rule.setCount(2); // 每秒最多 2 次请求

        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}

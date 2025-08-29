package org.hulei.springcloudalibaba.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentinelRuleInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("hello") // 对应 @SentinelResource(value = "hello")
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setCount(1); // 每秒最多 1 次请求
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
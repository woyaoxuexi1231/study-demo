package com.hundsun.demo.springcloud.gateway;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;

import java.util.Collections;

public class UserFlowLimitExample {

    public static void main(String[] args) {
        // 定义一个参数限流规则
        ParamFlowRule rule = new ParamFlowRule("user-api")
                .setParamIdx(0) // 设置参数位置为第一个参数
                .setCount(10)   // 每个用户的限流 QPS：10
                .setGrade(RuleConstant.FLOW_GRADE_QPS);

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));

        // 模拟用户请求
        for (int i = 0; i < 20; i++) {
            final String userId = "user_" + (i % 4); // 模拟不同的用户
            try {
                Entry entry = SphU.entry("user-api", 1, EntryType.valueOf(userId));
                // 执行业务逻辑
                System.out.println("Passed for user: " + userId);
            } catch (BlockException ex) {
                // 被限流处理
                System.out.println("Blocked for user: " + userId);
            }
        }
    }
}
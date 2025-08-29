package org.hulei.springcloudalibaba.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.Collections;
import java.util.List;

public class SentinelExceptionExample {

    private static final String RESOURCE_NAME = "HelloWorld";

    public static void main(String[] args) {
        // 1. 配置规则
        initFlowRules();

        // 2. 模拟持续的请求
        while (true) {
            try {
                // 定义资源， resourceName 为资源名
                // 如果被限流，这里会抛出 BlockException
                Entry entry = SphU.entry(RESOURCE_NAME);

                // 被保护的业务逻辑
                try {
                    System.out.println("Hello World at: " + System.currentTimeMillis());
                    // 模拟业务处理耗时
                    Thread.sleep(20);
                } finally {
                    // 务必保证 exit，务必保证每个 entry 与 exit 配对
                    if (entry != null) {
                        entry.exit();
                    }
                }

            } catch (BlockException e) {
                // 资源访问被限流或被降级时，会进入此逻辑
                // 在这里处理限流后的逻辑，例如返回默认值、抛出特定异常、记录日志等
                System.out.println("Blocked by Sentinel: " + e.getClass().getSimpleName() + ", rule: " + e.getRule());
            } catch (InterruptedException e) {
                // 处理 sleep 中断异常
                System.out.println("Interrupted!");
                break;
            }

            // 控制请求发送的间隔，以便观察限流效果
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * 初始化限流规则
     * 限制 RESOURCE_NAME 资源的 QPS 为 10
     */
    private static void initFlowRules() {
        FlowRule flowRule = new FlowRule();
        flowRule.setResource(RESOURCE_NAME);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(10);
        List<FlowRule> rules = Collections.singletonList(
            flowRule
        );
        FlowRuleManager.loadRules(rules); // 加载规则
        System.out.println("Flow rule loaded for resource: " + RESOURCE_NAME + ", QPS limit: 10");
    }
}




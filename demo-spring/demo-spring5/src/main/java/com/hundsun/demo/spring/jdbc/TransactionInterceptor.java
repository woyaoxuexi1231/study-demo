package com.hundsun.demo.spring.jdbc;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: TransactionInterceptor
 * @description: 一个简单的 Spring 事务拦截器
 * @author: h1123
 * @createDate: 2023/2/18 14:36
 */
@Data
public class TransactionInterceptor implements MethodInterceptor {

    /**
     * transactionManager
     */
    private PlatformTransactionManager transactionManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        TransactionDefinition definition = getTransactionDefinitionByMethod(method);
        TransactionStatus txStatus = transactionManager.getTransaction(definition);
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Throwable t) {
            if (needRollbackOn(t)) {
                transactionManager.rollback(txStatus);
            } else {
                transactionManager.commit(txStatus);
            }
            throw t;
        }
        transactionManager.commit(txStatus);
        return result;
    }

    protected boolean needRollbackOn(Throwable t) {
        return false;
    }

    protected TransactionDefinition getTransactionDefinitionByMethod(Method method) {
        return new DefaultTransactionDefinition();
    }
}

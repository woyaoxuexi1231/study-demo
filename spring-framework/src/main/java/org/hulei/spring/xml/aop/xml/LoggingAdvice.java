package org.hulei.spring.xml.aop.xml;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定义的具体的切面逻辑,没有使用任何spring的注解配置,但是环绕方式 以及 切点的定义全部在 xml 文件中声明
 *
 * @author hulei
 * @since 2024/9/8 16:12
 */
public class LoggingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

    public void beforeAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info("这个方法将执行一个前置通知: {}", methodName);
    }
}

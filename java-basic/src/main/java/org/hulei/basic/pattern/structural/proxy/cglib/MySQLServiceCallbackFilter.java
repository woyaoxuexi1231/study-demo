package org.hulei.basic.pattern.structural.proxy.cglib;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.proxy.cglib
 * @className: MySQLCallbackFilter
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 16:21
 */

public class MySQLServiceCallbackFilter implements CallbackFilter {


    /**
     * 过滤方法
     * <p>
     * 通过返回对应下标, 而执行事先设置的 callbacks数组中指定下标的自定义 MethodInterceptor
     *
     * @param method 代理的方法
     * @return 这里的数字代表着 Enhancer.setCallbacks 方法中设置的 callbacks数组的下标
     */
    @Override
    public int accept(Method method) {
        if (method.getName().equals("update")) {
            System.out.println("Filter method - update");
            return 1;
        }
        return 0;
    }
}

package com.hundsun.demo.java.jdk.proxy.proxycglib;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;
/**
 * 回调方法过滤
 * @author zghw
 *
 */
public class TargetMethodCallbackFilter implements CallbackFilter {
 
    /**
     * 过滤方法
     * 返回的值为数字，代表了Callback数组中的索引位置，要到用的Callback
     */
    @Override
    public int accept(Method method) {

        if(method.getName().equals("update")){
            System.out.println("filter method - update");
            return 0;
        }
        if(method.getName().equals("find")){
            System.out.println("filter method - find");
            return 1;
        }
        return 0;
    }
 
}
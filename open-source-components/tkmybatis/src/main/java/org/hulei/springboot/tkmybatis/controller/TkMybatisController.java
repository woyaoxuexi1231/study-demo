package org.hulei.springboot.tkmybatis.controller;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.springboot.tkmybatis.mapper.EmployeeMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei42031
 * @since 2024-03-29 9:58
 */

@RequiredArgsConstructor
@SuppressWarnings("CallToPrintStackTrace")
@Slf4j
@RestController
@RequestMapping("/tkmybatis")
public class TkMybatisController {

    private final EmployeeMapper employeeMapper;

    @GetMapping("/employee/{id}")
    public void getEmployeeById(@PathVariable(value = "id") Long id) {
        // 数组的临界情况为数组是空数组,数组对象是不为空的
        // List<EmployeeDO> employeeDOS = employeeMapper.selectAll();
        // EmployeeDO employeeDO = employeeDOS.get(0);
        // 基本类型的临界值情况是直接为null
        String s = employeeMapper.selectLastNameById(id);
        System.out.println(s);
    }

    @GetMapping("/tkmyabtis-log")
    public void tkmyabtisLog() {
        /*
        这里发生一个比较奇怪的问题，开始我的Employee没有实现 Serializable 接口，但是我没有发现。
        这里一直报错 Detected an attempt at releasing unacquired lock. This should never happen. 在源码的 org.apache.ibatis.cache.decorators.BlockingCache.releaseLock 这里
        我一直在尝试找到为什么这里会发生这个问题，但是最后debug的结果是真正的报错是没有实现 Serializable 接口，不清楚是不是因为 tkmybatis 的问题
         */
        employeeMapper.selectAll();
    }
}

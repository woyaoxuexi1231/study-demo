package org.hulei.springboot.tkmybatis.controller;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.springboot.tkmybatis.mapper.EmployeeMapper;
import org.hulei.util.dto.PageQryReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            new ThreadFactoryBuilder().setNamePrefix("tkmybatis-pool-").build(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    private final EmployeeMapper employeeMapper;
    private final SqlSessionFactory sqlSessionFactory;

    @GetMapping("/print")
    public void print(Long id) {
        // 数组的临界情况为数组是空数组,数组对象是不为空的
        // List<EmployeeDO> employeeDOS = employeeMapper.selectAll();
        // EmployeeDO employeeDO = employeeDOS.get(0);
        // 基本类型的临界值情况是直接为null
        String s = employeeMapper.selectLastNameById(id);
        System.out.println(s);
    }

    /**
     * mybatis 一级缓存
     *
     * @param req      分页信息
     * @param request  httpReq
     * @param response httpRsp
     */
    @PostMapping(value = "/localCache")
    public void localCache(@Valid @RequestBody PageQryReqDTO req, HttpServletRequest request, HttpServletResponse response) {

        // mybatis-plus.configuration.local-cache-scope 用于开启mybatis的一级缓存
        // mybatis.configuration.cache-enabled 用于开启mybatis的二级缓存

        try (SqlSession session = sqlSessionFactory.openSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            // 一级缓存开启,并且范围是statement,那么即使在同一个session内,相同的sql也都会进行数据库查询操作
            log.info("第一次查询结果: {}", mapper.selectAll());
            // 一级缓存范围是session的话,这里就不会再查询数据库了
            // 这里开启二级缓存的话,同一个session内好像并不增加缓存命中次数 Cache Hit Ratio [org.hulei.tkmybatis.mapper.EmployeeMapper]: 0.0 一直是0
            log.info("第二次查询结果: {}", mapper.selectAll());
            session.commit();
        }
    }

    /**
     * mybatis 二级缓存
     */
    @GetMapping("/cache")
    public void cache() {

        // 二级缓存开启的条件有两个: 1.mybatis.configuration.cache-enabled=true 2.要么在mapper接口上使用 @CacheNamespace, 要么在mapper.xml文件中配置一个 <cache/>标签
        // 注意: @CacheNamespace 和  <cache/> 在使用时两者的适用范围不一样(通用mapper-spring-boot-starter 4.2.1)
        //      @CacheNamespace只适用通用mapper的直接使用的api, 或者直接使用@Select类似这样的接口层的sql
        //      <cache/>只适用于xml文件内的sql
        // Cache Hit Ratio [org.hulei.tkmybatis.mapper.EmployeeMapper]: 0.5 统计的也是针对整个mapper文件的命中率,所有语句的命中率会统一统计
        // 这是一个tkmybatis的通用api,只有@CacheNamespace才会生效
        employeeMapper.selectAll();
        // 这个也是一样的,即使有入参,只有@CacheNamespace才会生效
        employeeMapper.selectOne(new Employee().setId(1002L));
        // 同样的,虽然这个不是通用mapper的api,但是他使用@Select查询数据,也只有@CacheNamespace才会生效
        employeeMapper.selectAllDataButInterface();
        // 这个是xml内的sql,只有使用<cache/>标签才能使用二级缓存
        employeeMapper.selectAllData();
    }

    @GetMapping("/tkmyabtisLog")
    public void tkmyabtisLog() {
        // 通用mapper提供的接口使用mybatis的日志打印配置是不生效的,需要开启log4j2的debug才行
        employeeMapper.selectAllDataButInterface();
    }
}

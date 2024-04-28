package com.hundsun.demo.springboot.tkmybatis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.common.model.req.EmployeeQryReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author hulei42031
 * @since 2024-03-29 9:58
 */

@Slf4j
@RestController
@RequestMapping("/tkmybatis")
public class TkMybatisController {

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/print")
    public void print(Long id) {
        // 数组的临界情况为数组是空数组,数组对象是不为空的
        // List<EmployeeDO> employeeDOS = employeeMapper.selectAll();
        // EmployeeDO employeeDO = employeeDOS.get(0);
        // 基本类型的临界值情况是直接为null
        String s = employeeMapper.selectLastNameById(id);
        System.out.println(s);
    }

    @Cacheable(value = "myCache", key = "#req")
    @PostMapping(value = "/getEmployees")
    public PageInfo<EmployeeDO> getEmployees(@Valid @RequestBody EmployeeQryReqDTO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return new PageInfo<>(employeeMapper.selectAll());
    }

    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map) {
        // 设置时区为东八区（北京时间）
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        // 获取当前时间
        Date now = new Date();
        // 根据指定时区获取当前时间
        Date nowInTimeZone = new Date(now.getTime() + timeZone.getRawOffset());
        map.put("response-tag", nowInTimeZone);
        return map;
    }
}

@Configuration
@EnableCaching
class CacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        return RedisCacheManager.create(redisConnectionFactory);
    }

    // 这是一个本地缓存
    // @Bean
    // public CacheManager cacheManager() {
    //     return new ConcurrentMapCacheManager("myCache");
    // }
}

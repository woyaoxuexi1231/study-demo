package org.hulei.mybatis.spring.cache;

import com.github.pagehelper.PageHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.mybatis.mapper.CustomerMapper;
import org.hulei.util.dto.PageQryReqDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/11/17 17:39
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cache")
@RestController
public class MybatisCacheController {

    private final CustomerMapper customerMapper;

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * mybatis 一级缓存
     *
     * @param req      分页信息
     * @param request  httpReq
     * @param response httpRsp
     */
    @PostMapping(value = "/localCache")
    public void localCache(@Valid @RequestBody PageQryReqDTO req, HttpServletRequest request, HttpServletResponse response) {

        try (SqlSession session = sqlSessionFactory.openSession()) {

            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            log.info("第一次查询结果: {}", mapper.selectAll());
            log.info("第二次查询结果: {}", mapper.selectAll());
            session.commit();

            /*
            一级缓存 mybatis.configuration.local-cache-scope
            1. statement, 缓存仅限在单个查询语句中，其实也就是没有缓存，每次查询都是当前读，会去数据库拿最新的数据
            2. session，缓存在单个会话内，这里为了实现这种场景，专门单开了一个 session
               如果只是单纯使用注入进来的 mapper 对象且不开启事务的情况下，单次查询之后session就完了，试验不了
               如果非要使用注入进来的 mapper 来实验也可以，方法开启事务即可，这样 session 在一个事务内就不会直接关闭，还会接着用
               这里如果使用 session 级别，第二次查询是不会去查询数据的
             */
        }

    }

    /**
     * mybatis 二级缓存
     */
    @GetMapping("/cache")
    public void cache() {

        /*
        二级缓存
        要开启二级缓存
            1. mybatis.configuration.cache-enabled=true
            2. 要么在 mapper 接口上使用 @CacheNamespace, 要么在 mapper.xml 文件中配置一个 <cache/>标签

        注意:
            1. @CacheNamespace 和  <cache/> 在使用时两者的适用范围不一样(通用mapper-spring-boot-starter 4.2.1)
                - @CacheNamespace 只适用通用 mapper 的直接使用的 api, 或者直接使用 @Select 类似这样的接口层的 sql
                - <cache/> 只适用于 xml 文件内的 sql
            2. 要使用二级缓存，缓存的对象必须要实现 Serializable 接口


        缓存命中率: Cache Hit Ratio
        - 开启二级缓存的话, 同一个session内好像并不增加缓存命中次数 Cache Hit Ratio [org.hulei.tkmybatis.mapper.EmployeeMapper]: 0.0 一直是 0
        - 缓存统计的也是针对整个 mapper 文件的命中率, 所有语句的命中率会统一统计
        - 二级缓存和一级缓存的优先级问题，在二级缓存没有生成的时候，第一次的 session 内一级缓存会根据自己的配置进行查询，后续二级缓存生成后，就会优先使用二级缓存
         */

        log.info("selectById: {}", customerMapper.selectById(103));

        // 同样的, 虽然这个不是通用 mapper 的 api, 但是他使用 @Select 查询数据, 也只有 @CacheNamespace 才会生效
        log.info("selectByIdWithAnnotation: {}", customerMapper.selectByIdWithAnnotation(103));
    }
}

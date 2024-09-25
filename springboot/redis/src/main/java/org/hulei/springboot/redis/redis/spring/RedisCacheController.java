package org.hulei.springboot.redis.redis.spring;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.req.PageQryReqDTO;
import com.hundsun.demo.commom.core.mapper.EmployeeMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/9/11 15:07
 */

@RequestMapping("/redisCache")
@RestController
public class RedisCacheController {

    @Autowired
    ThreadPoolExecutor commonPool;

    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    /**
     * 初始化一个HashMapper,这个主要用于Java对象与redis哈希结构之间的转换使用
     * Jackson2HashMapper可以帮我们构建哈希值是string-object这种形式的映射
     */
    Jackson2HashMapper jackson2HashMapper = new Jackson2HashMapper(true);


    public static final String get_employees_key = "employee::getEmployees";

    public static final String get_employee_by_id_prefix = "employee::getEmployeeById::";

    Random random = new Random();

    /**
     * 使用哈希结构作为缓存的数据结构
     * key作为整体业务的键, field-key为查询条件, field-value为查询结果
     * 这种方式存在缺陷 1.某种查询条件的结果无法单独过期 2.数据的序列化和反序列化由于泛型的存在不好做
     *
     * @param req 查询条件
     * @return rsp
     */
    @Deprecated
    @PostMapping(value = "/getEmployees")
    public List<EmployeeDO> getEmployees(@RequestBody PageQryReqDTO req) {
        // 这里使用哈希作为缓存的键值,会存在一个问题就是无法单独对每次的查询条件进行一个定时过期的处理,所以如果以哈希作为缓存的存储结构可以考虑如下:
        // 1. 批量查询似乎并不太适合哈希
        // 2. 对于单个数据的缓存更容易进行存储, key作为http接口的入参条件(当然加上业务前缀), field+vue分别存储对象的每个属性

        // 比如这里我们以employee::getEmployees作为key, 每种不同的查询条件作为一个单独的field的key,查询结果作为field的value
        // hget employee::getEmployees req
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        // hget employee::getEmployees req
        String cacheRsp = operations.get(get_employees_key, JSON.toJSONString(req));

        Optional<List<EmployeeDO>> optional = Optional.empty();
        if (Objects.nonNull(cacheRsp)) {
            // 反序列化的时候由于泛型,我们无法准确的还原出List中的准确的对象
            List parsedObject = JSON.parseObject(cacheRsp, List.class);
            optional = Optional.of(parsedObject);
        }
        return optional.orElseGet(() -> {
            LambdaQueryWrapper<EmployeeDO> wrapper = new LambdaQueryWrapper<>();
            PageHelper.startPage(req.getPageNum(), req.getPageSize());
            List<EmployeeDO> dbRsp = employeeMapperPlus.selectList(wrapper);
            commonPool.execute(() -> {
                // 由于我们的具体值放在了field,这也导致我们无法对单个查询条件进行过期处理
                // hset key field value
                redisTemplate.opsForHash().put(get_employees_key, JSON.toJSONString(req), JSON.toJSONString(dbRsp));
            });
            return dbRsp;
        });
    }

    /**
     * 以哈希结构作为缓存的数据结构,但是存储的形式以单个键存储单个对象的形式存储
     *
     * @param id employeeID
     * @return 单个Employee
     */
    @GetMapping("/getEmployeeById")
    public EmployeeDO getEmployeeById(Long id) {
        // 先尝试从缓存中获取对象  hgetall key
        Optional<EmployeeDO> optional = Optional.empty();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(get_employee_by_id_prefix + id))) {
            HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
            Map<String, Object> entries = operations.entries(get_employee_by_id_prefix + id);
            EmployeeDO fromHash = jackson2HashMapper.fromHash(EmployeeDO.class, entries);
            if (!CollectionUtils.isEmpty(entries)) {
                optional = Optional.of(fromHash);
            }
        } else {
            // 在redis中并没有查到这条数据,通过过滤器查找一下是否这个数据本身就不存在
            Boolean bit = redisTemplate.opsForValue().getBit(get_employee_by_id_prefix + "bitmap", id);
            if (Boolean.TRUE.equals(bit)) {
                return null;
            }
        }
        return optional.orElseGet(() -> {
            LambdaQueryWrapper<EmployeeDO> lambdaQuery = Wrappers.lambdaQuery(EmployeeDO.class);
            lambdaQuery.eq(EmployeeDO::getEmployeeNumber, id);
            // 尝试从数据库中获取数据
            Optional<EmployeeDO> dbOptional = Optional.ofNullable(employeeMapperPlus.selectOne(lambdaQuery));
            // 如果能查出数据, 异步放入缓存
            dbOptional.ifPresent(
                    (db) -> {
                        // 缓存操作通过异步执行,不影响主线程返回
                        commonPool.execute(() -> {
                            // 通过springredis提供的ObjectHashMapper这个类加上putAll()这个api可以快速的把对象内的属性分解为哈希内的field
                            redisTemplate.opsForHash().putAll(get_employee_by_id_prefix + id, jackson2HashMapper.toHash(db));
                            // 设置同一个过期时间可能会导致缓存雪崩,解决方案也有多种
                            // 1.热点数据永不过期(这里我们不使用这种方案,对于变化不大的数据可以采用这种方案) 2.采用随机过期时间
                            // 这里采用120秒内
                            int nextInt = random.nextInt();
                            redisTemplate.expire(get_employee_by_id_prefix + id, (nextInt < 0 ? nextInt >>> 1 : nextInt) % 120, TimeUnit.SECONDS);
                            // 这里考虑如果发生redis操作失败怎么办
                        });
                    }
            );
            return dbOptional.orElseGet(() -> {
                // 如果我们查询一个数据库压根不存在的数据,就会触发缓存穿透的问题,这里引发的,这种数据会造成数据库大量的压力
                // 1.缓存空对象 2.各种过滤器, 如布隆过滤器(这个是由redis的bitmap实现,但是需要对数据量有估算,才能知道位图大小需要多少)
                commonPool.execute(() -> redisTemplate.opsForValue().setBit(get_employee_by_id_prefix + "bitmap", id, true));
                return null;
            });
        });
    }
}

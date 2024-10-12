package org.hulei.springboot.redis.redis.spring;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hulei
 * @since 2024/9/20 16:58
 */

@RequestMapping("/redisSet")
@RestController
public class RedisSetController {

    /*
     集合:
     集合内操作:
     操作元素:
        sadd key member [member ...] 添加元素,可以添加多个
        srem key member [member ...] 删除元素,可以删除多个
        spop key 随机弹出一个元素

    得到元素:
        scard key 查看元素个数
        srandmember key [count] 随机得到元素,不指定count默认弹出一个,这个操作不会删除元素
        sismember key member 判断指定的元素是否在集合内
        smembers key 得到所有元素,这是一个重量级操作,如果集合内元素过多,那么将耗费大量时间和cpu资源

     集合间操作:
        sinter key [key ...] 多个集合的交集
        sunion key [key ...] 多个集合的并集
        sdiff key [key ...] 返回第一个集合与其他集合的不同的值
        每个命令加store就是把对应的结果保存到指定的key
        sdiffstore destination key [key ...] 把多个集合的差集保存到新的 destination 集合
     */

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Qualifier(value = "strObjRedisTemplate")
    @Autowired
    RedisTemplate<String, Object> strObjRedisTemplate;


    /**
     * 使用redis集合获得集合交集
     *
     * @return
     */
    @RequestMapping("/basic")
    public String basic() {

        // 对于用户 Aurelia 来说,这算是他的关注列表,他一共关注了四个人 sadd key
        strObjRedisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");

        // 对于用户 Nou 来说,这算是他的关注列表,他也关注了四个人 sadd key
        strObjRedisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");

        // 现在的问题是,要求这两个人的共同关注,这其实redis有提供这相关api可以使用 sinter key1 key2
        Set<Object> intersect = strObjRedisTemplate.opsForSet().intersect("Aurelia", "Nou");

        // 扩展一下
        // 1.求并集  sunion key1 key2
        Set<Object> union = strObjRedisTemplate.opsForSet().union("Aurelia", "Nou");
        // 2.求差集 sdiff key1 key2
        Set<Object> difference = strObjRedisTemplate.opsForSet().difference("Aurelia", "Nou");

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("intersect", CollectionUtils.isEmpty(intersect) ? "null" : intersect.toString());
        resultMap.put("union", CollectionUtils.isEmpty(union) ? "null" : union.toString());
        resultMap.put("difference", CollectionUtils.isEmpty(difference) ? "null" : difference.toString());
        return JSONObject.toJSONString(resultMap);
    }
}

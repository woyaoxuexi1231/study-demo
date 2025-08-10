package org.hulei.demo.cj.seckill;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataProducts;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataProductMapper;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static org.hulei.demo.cj.seckill.SeckillConsts.LIMIT_KEY_PREFIX;
import static org.hulei.demo.cj.seckill.SeckillConsts.STOCK_KEY_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeckillStockService {

    private final BigDataProductMapper bigDataProductMapper;
    private final StringRedisTemplate redisTemplate;

    private DefaultRedisScript<Long> seckillScript;
    private final RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        this.seckillScript = new DefaultRedisScript<>();
        this.seckillScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("seckill/initSeckill.lua")));
        this.seckillScript.setResultType(Long.class);
    }

    // 初始化商品库存
    @Transactional(rollbackFor = Exception.class)
    public String initStock(Long productId, Integer stock) {
        // 查询数据库的商品信息
        BigDataProducts product = bigDataProductMapper.selectById(productId);
        if (Objects.nonNull(product) && product.getQuantity() >= stock) {
            String key = STOCK_KEY_PREFIX + productId;
            // 使用 lua 脚本来执行
            Long result = redisTemplate.execute(
                    seckillScript,
                    CollectionUtil.newArrayList(key),
                    String.valueOf(stock)
            );
            if (Objects.nonNull(result)) {
                // 💡修改库存，这里应该配置冻结库存或者其他方式来削减库存，如果redis数据没了，那么库存也就没了，需要有一种保障措施来恢复库存
                product.setQuantity(product.getQuantity() - stock);
                product.setFreezeQuantity(product.getFreezeQuantity() + stock);
                bigDataProductMapper.updateById(product);
                return "商品：" + productId + " 新增秒杀数量：" + stock + "成功！";
            } else {
                return "商品：" + productId + " 新增秒杀失败！";
            }
        }
        return "商品不存在，或数量不足！";
    }

    // 获取剩余库存
    public int getRemainStock(long productId) {
        String stock = redisTemplate.opsForValue().get(STOCK_KEY_PREFIX + productId);
        return stock != null ? Integer.parseInt(stock) : 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void decreaseFreezeStock(Integer productId) {
        // 这里我想到两个方案 1.查询需要返回多少库存  2.直接减少冻结库存
        // 我这里选择 2
        // 这个redis操作应该拿出去更好一点
        Long size = redisTemplate.opsForHash().size(LIMIT_KEY_PREFIX + productId);
        // 减掉冻结库存
        BigDataProducts products = bigDataProductMapper.selectById(productId);
        products.setFreezeQuantity(products.getFreezeQuantity() - size.intValue());
        bigDataProductMapper.updateById(products);
    }

    @Transactional(rollbackFor = Exception.class)
    public void backStock(Long productId, Integer seckillNumber) {
        BigDataProducts products = bigDataProductMapper.selectById(productId);

        Integer backNumber = products.getFreezeQuantity() - seckillNumber;

        products.setQuantity(products.getQuantity() + backNumber);
        products.setFreezeQuantity(products.getFreezeQuantity() - backNumber);
        bigDataProductMapper.updateById(products);
    }

    // todo 这里 backStock 和 decreaseFreezeStock 的功能看似都有用，其实重复了，两种方式取其一，只要完成归还库存就行(前提是冻结库存仅仅给秒杀用)
}
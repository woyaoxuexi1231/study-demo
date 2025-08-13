package org.hulei.demo.cj.seckill;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataProducts;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ErrorHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.hulei.demo.cj.seckill.SeckillConsts.LIMIT_KEY_PREFIX;
import static org.hulei.demo.cj.seckill.SeckillConsts.STOCK_KEY_PREFIX;

/**
 * @author hulei
 * @since 2025/8/10 21:58
 */

@RequiredArgsConstructor
@Slf4j
@Configuration
public class SeckillRedisExpireConfig {

    final SeckillStockService seckillStockService;
    final RedisTemplate<String, Object> redisTemplate;
    final BigDataProductMapper bigDataProductMapper;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
        // 初始化库存后，建立一个监听事件，避免在秒杀缓存失效后，库存消失了，用于把库存还原
        container.addMessageListener(
                new MessageListener() {
                    @Override
                    public void onMessage(Message message, byte[] pattern) {
                        String eventType = new String(message.getBody(), StandardCharsets.UTF_8);
                        String key = new String(message.getChannel(), StandardCharsets.UTF_8);
                        // 库存过期后，需要返回冻结的库存
                        if ("expired".equals(eventType)) {
                            // 分解 key
                            String substring = key.substring(("__keyspace@0__:" + STOCK_KEY_PREFIX).length());
                            seckillStockService.backStock(Long.parseLong(substring));
                        }
                    }
                },
                new PatternTopic("__keyspace@0__:" + STOCK_KEY_PREFIX + "*")
        );
        return container;
    }


    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void scanExpiredProduct() {
        log.info("开始扫描秒杀清单，以及归还库存。");
        // 每分钟扫描一次目前冻结的库存是否能够与秒杀一致
        // 如果不一致退还库存
        List<BigDataProducts> products = bigDataProductMapper.selectList(Wrappers.<BigDataProducts>lambdaQuery()
                .gt(BigDataProducts::getFreezeQuantity, 0)
        );
        products.forEach(product -> {
            // 假设现在冻结的数量仅仅用作秒杀，那么可以这么做，如果不是
            // 需要有一个东西来记录秒杀一共拿了多少库存
            // 然后这里应该也要用lua脚本来做，不然没有原子性
            if (Boolean.FALSE.equals(redisTemplate.hasKey(STOCK_KEY_PREFIX + product.getId()))) {
                // 归还没有秒杀完的商品
                seckillStockService.backStock(product.getId());
            }
        });
    }
}

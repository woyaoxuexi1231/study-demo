package org.hulei.demo.cj.seckill;

/**
 * @author hulei
 * @since 2025/8/10 21:31
 */

public class SeckillConsts {

    /**
     * String 类型，完整的键最后是产品 id 信息，此 key 的 value 对应实际的库存
     */
    public static final String STOCK_KEY_PREFIX = "seckill:stock:";
    /**
     * hash 类型，完整的键最后是产品信息，field key 对应用户id，value 对应用户购买限制值
     */
    public static final String LIMIT_KEY_PREFIX = "seckill:user_limit:";

}

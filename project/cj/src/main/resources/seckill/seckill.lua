-- KEYS[1]: 库存key
-- KEYS[2]: 用户限购key
-- ARGV[1]: 用户ID
-- ARGV[2]: 最大购买限制
-- ARGV[3]: 购买数量

-- 检查库存
redis.log(redis.LOG_NOTICE,"1")
local stock = tonumber(redis.call('get', KEYS[1]))
if stock == nil or stock <= 0 then
    return 0  -- 库存不足
end

redis.log(redis.LOG_NOTICE,"2")
-- 检查用户购买限制
local userLimit = redis.call('hget', KEYS[2], ARGV[1])
-- userLimit 如果不为空，那么把 userLimit 转换为数字，否则直接为 0
userLimit = userLimit and tonumber(userLimit) or 0


redis.log(redis.LOG_NOTICE,"3")
if userLimit + tonumber(ARGV[3]) > tonumber(ARGV[2]) then
    return -1  -- 超过购买限制
end

redis.log(redis.LOG_NOTICE,"4")
-- 扣减库存
if stock >= tonumber(ARGV[3]) then
    redis.call('decrby', KEYS[1], ARGV[3])

    -- 更新用户购买记录
    redis.call('hincrby', KEYS[2], ARGV[1], ARGV[3])
    return 1  -- 成功
else
    return 0  -- 库存不足
end

redis.log(redis.LOG_NOTICE,"5")
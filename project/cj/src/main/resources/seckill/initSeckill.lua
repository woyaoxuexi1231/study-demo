-- 查看键当前是否存在
local remain = tonumber(redis.call('get', KEYS[1]))
if remain == nil then
    redis.call('set', KEYS[1], ARGV[1])
    redis.call('expire', KEYS[1], 5)
    return 1;
else
    redis.call('incrby', KEYS[1], ARGV[1])
    redis.call('expire', KEYS[1], 5)
    return 1;
end

--
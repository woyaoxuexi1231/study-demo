package org.hulei.springboot.redis;
/*

哈希:



集合:
    集合内操作:
        sadd key member [member ...] 添加元素,可以添加多个
        srem key member [member ...] 删除元素,可以删除多个
        scard key 查看元素个数
        sismember key member 判断指定的元素是否在集合内
        srandmember key [count] 随机得到元素,不指定count默认弹出一个
        spop key 随机弹出一个元素
        smembers key 得到所有元素

    smembers lrange hgetall hkeys hvals 都是属于重量级操作,慎用
    集合间操作:
        sinter key [key ...] 多个集合的交集
        sunion key [key ...] 多个集合的并集
        sdiff key [key ...] 返回第一个集合与其他集合的不同的值
        每个命令加store就是把对应的结果保存到指定的key
        sdiffstore destination key [key ...] 把多个集合的差集保存到新的 destination 集合

 */
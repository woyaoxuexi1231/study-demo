package com.hundsun.demo.spring.redis;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.redis
 * @className: RedisTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/26 15:27
 */

public class RedisTest {

    /*
    Q1: 什么是序列化?
    A1: 把对象转换成字节码序列的过程称为对象的序列化, 把字节序列恢复为对象的过程成为反序列化
        对象序列化的用途主要有: 1. 把对象的字节序列永久的保存在硬盘上, 以便之后恢复这个对象 2. 在网络上传送对象, 所以只要接受者使用相同的序列化机制, 那么收到这个字节序列就可以重塑这个对象
    Q1: 为什么非得序列化?
    A1: 主要是为了恢复对象, 可以提高性能, 不同的序列化方式性能不一样

    Spring 提供了两个可使用的 RedisTemplate
    RedisTemplate<Object, Object> - 默认全部使用 JdkSerializationRedisSerializer 的序列化方式
    StringRedisTemplate - 默认全部使用 StringRedisSerializer.UTF_8 的序列化方式

    自定义的 redis 的序列化方式
    1. JdkSerializationRedisSerializer - POJO对象的存取场景, 使用 JDK 本身的序列化机制, 默认机制 ObjectInputStream/ObjectOutputStream 进行序列化操作
    2. StringRedisSerializer - key 或者 value 为字符串
    3. Jackson2JsonRedisSerializer - 利用 jackson-json 工具, 将 pojo 实例序列化为 json 格式存储
    GenericJackson2JsonRedisSerializer,GenericToStringSerializer


     */
}

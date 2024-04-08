日志框架的进化史 - log4j - Apache(1999) -> JUL - sun(2002) -> JCL - Apache(commons logging)(2002) -> slf4j&logback - Ceki Gülcü(2005,2006) -> log4j - 2Apache(2014)

log4j 最早的日志框架, 是 apache 基金会一个项目, 由 Ceki Gülcü 创建.  
-> 随后apache想让sun公司把log4j并入jdk遭到了拒绝, sun又模仿log4j推出了JUL(java.util.logging)  
-> 为了解耦日志的接口与实现, apache紧接着推出了 JCL(Jakarta Commons Logging), 当时具体的实现由 log4j和JUL 完成  
-> Ceki Gülcü 后来与 Apache 基金会关于 Commons-Logging 制定的标准存在分歧, Ceki Gülcü 离开 Apache 并先后创建了 Slf4j(门面,同样支持log4j和JUL) 和 Logback(实现) 两个项目  
-> 为了维护在 Java 日志江湖的地位, 防止 JCL、Log4j 被 Slf4j、Logback 组合取代, 2014 年 Apache 推出了 Log4j2, Log4j2 与 log4j 不兼容, 经过大量深度优化, 其性能显著提升  

日志框架分为日志门面(只提供相应API而不提供接口实现)和日志系统(提供具体的接口实现)
日志门面: commons-logging(JCL), slf4j
日志系统: log4j(log4j2), JUL, Logback, slf4j-simple

 Ceki Gülcü 当年因为觉得 Commons-Logging 的 API 设计的不好, 性能也不够高因而设计了 Slf4j。
 而他为了 Slf4j 能够兼容各种类型的日志系统实现，还设计了相当多的 adapter 和 bridge 来连接, 如下:
日志库适配器: 这几个桥接包都是为了让其他的日志框架都统一输出到 slf4j这个日志框架上
    jul-to-slf4j,
    log4j-over-slf4j - 它的主要作用是将使用log4j日志框架输出的日志路由到SLF4J上
    jcl-over-slf4j
日志门面适配器: 主要作用是将slf4j的API和 xxx链接起来, 这样当你在代码中使用SLF4J API进行日志记录时，这些日志实际上会被路由到log4j进行处理, 这种设计使得我们可以很容易地切换具体的日志框架，比如从logback切换到log4j
    slf4j-jdk, slf4j-jcl, slf4j-log4j12(旧版本的log4j(即log4j 1.x)连接起来的桥接器)

常用的日志搭配:
    slf4j+logback 不需要桥接包(都是 Ceki Gülcü 写的一套东西, 他自己的东西肯定不需要适配的)
    slf4j+log4j2 需要桥接包 log4j-slf4j-impl(和log4j2连接起来的桥接器)
    commons logging+log4j2 不需要桥接包

经常出现这样的报错
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/C:/Users/h1123/.m2/repository/org/apache/logging/log4j/log4j-slf4j-impl/2.13.3/log4j-slf4j-impl-2.13.3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/C:/Users/h1123/.m2/repository/org/slf4j/slf4j-log4j12/1.7.30/slf4j-log4j12-1.7.30.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
这个是存在了多个 SLF4J 的具体实现, 这两个一个log4j(slf4j-log4j12)的一个是log4j2(log4j-slf4j-impl)


JCL(Commons-Logging) & log4j
slf4j(Simple Logging Facade for Java) & logback
Spring Boot默认使用的日志框架是 Logback。
Spring Boot在所有内部日志中使用 Commons Logging，但是默认配置也提供了对常用日志的支持，如：Java Util Logging，Log4J, Log4J2和Logback1。

spring-boot-starter-logging依赖包括三个依赖
logback-classic(经典库, 提供了对 slf4j 的支持, 包含了 logback-core, slf4j-api)
log4j-to-slf4j(提供了 log4j 的日志消息到 SLF4j 的路由, 包含了 slf4j-api)
jul-to-slf4j(提供了 jul(java-util-logging) 到 slf4j 的支持, 依赖包含slf4j-api)

我们的方案是 slf4j+lof4j2, 所以我们要排除上面的所有包
log4j-slf4j-impl+log4j2

log4j-slf4j-impl cannot be present with log4j-to-slf4j


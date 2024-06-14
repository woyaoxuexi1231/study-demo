package com.hundsun.demo.spring.jdk.pattern.structural;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: FacadeTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 21:48
 */

public class FacadeTest {

    /*
    外观模式 - 为子系统中的一组接口提供一个统一的入口
    对过程进行封装, 外部不单独对某个接口进行访问, 而是把整个操作封装成一个接口, 外部只访问这个单独的接口
    场景:
        1. 系统需要对某给文件进行加密, 操作分为读取文件、加密文件、替换文件,
            系统可只对外提供一个加密接口, 加密接口内对三个功能进行整合封装,
            三个接口也可以进行抽象, 读取文件可以使用不同的读取引擎, 加密文件可以使用不同的加密方式

        2. Java的日志框架就是使用外观模式(门面模式), 我们只管打印日志, 而不必关心日志是如何被打印出来的
     */
}

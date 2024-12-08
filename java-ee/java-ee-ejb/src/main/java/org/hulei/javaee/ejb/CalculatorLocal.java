package org.hulei.javaee.ejb;

import jakarta.ejb.Local;
import jakarta.ejb.Remote;

/*
jakarta.ejb.Local 和 jakarta.ejb.Remote 是Jakarta EE（Java Platform, Enterprise Edition的后续版本，原名为Java EE）中EJB（Enterprise JavaBeans）技术的两个关键注解，它们用于定义EJB的本地和远程业务接口。以下是它们之间的主要区别：

1. 定义与用途
jakarta.ejb.Local：
用于定义EJB的本地业务接口。
当客户端和EJB在同一个JVM（Java虚拟机）中运行时，使用本地接口可以提高性能，因为调用是通过直接引用而不是通过网络进行的。
本地接口调用不涉及序列化和反序列化过程，因此速度更快。
jakarta.ejb.Remote：
用于定义EJB的远程业务接口。
当客户端和EJB在不同的JVM中运行时，使用远程接口进行通信。
远程接口调用涉及序列化和反序列化过程，以及可能的网络延迟，因此性能相对较低。
2. 使用场景
本地接口（Local）：
适用于同一JVM内的EJB调用，如EJB之间的内部调用。
由于性能优势，建议在可能的情况下优先使用本地接口。
远程接口（Remote）：
适用于跨JVM的EJB调用，如Web层调用应用层（app layer）的EJB。
当需要在不同服务器或不同Java EE应用之间调用EJB时，必须使用远程接口。
3. 调用方式
本地接口：
调用本地接口时，客户端直接获取EJB的本地对象引用，并调用其方法。
本地接口调用通常不涉及复杂的网络协议和序列化过程。
远程接口：
调用远程接口时，客户端首先通过JNDI（Java Naming and Directory Interface）查找EJB的远程对象引用。
然后，客户端使用RMI（Remote Method Invocation）或其他远程通信协议调用EJB的方法。
远程接口调用涉及序列化和反序列化过程，以及网络传输。
4. 性能差异
本地接口调用通常比远程接口调用更快，因为前者不涉及网络传输和序列化/反序列化过程。
在性能敏感的应用中，应优先考虑使用本地接口。
综上所述，jakarta.ejb.Local 和 jakarta.ejb.Remote 注解分别用于定义EJB的本地和远程业务接口。它们的主要区别在于使用场景、调用方式和性能差异。在选择使用哪种接口时，应根据具体的应用需求和部署环境来决定。
 */

@Local
// @Remote
public interface CalculatorLocal {

    int add(int a, int b);

    int subtract(int a, int b);
}

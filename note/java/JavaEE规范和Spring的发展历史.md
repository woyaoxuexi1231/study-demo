# JavaEE的起源

Java起初拥有三个版本：

1. JavaSE - 标准版，Java SE 提供了 Java 编程语言的基础，包含了核心库、Java 虚拟机 (JVM)、开发工具 (如 javac) 等，是 Java 开发的基础平台
2. JavaEE- 企业版，Java EE 在 Java SE 的基础上扩展，提供了用于企业级应用程序开发的 API（起初只是一系列规范） 和运行时环境，包括 web 应用、服务端程序、分布式计算等。Java EE 现在被称为 Jakarta EE。
3. JavaME - 移动版，主要用于开发资源受限的设备上的应用程序，比如嵌入式系统、移动设备等。它提供了一个轻量级的 Java 平台，适合于小型设备的开发。目前已经几乎销声匿迹了。

1999 年 SUN 公司想在企业级应用上提出一个规范，以容器和组件的形式提供服务。共性的东西由容器管理，企业个性化的东西由组件管理，容器和组件合二为一作为企业级应用，这就是最早出现的 J2EE 的规范。

而后出现了 Tomcat，Jetty 等符合 J2EE 的规范的容器（应用服务器）。99年12月份，第一个版本的JavaEE的规范出现了。

在最初的应用架构中分为 B/S 和 C/S，基于B/S和MVC模型，JavaEE规范提出了两种容器：

- Web Container：主要负责页面渲染，仍然提供JDBC,JPA,JMS等其他规范，但是推荐使用 EJB 容器进行这些操作，Web 容器仅作为页面渲染使用。
- EJB Container：主要提供业务逻辑的处理

2001年3月 SUN公司推出了 J2EE1.3版本，新增了XML的新特性，RMI-IIOP的通信方式（Web容器和EJB容器之间通信的技术）改为XML的通信方式。

2002年10月，Rod Johnson编写了一本《expert one-to-one J2EE Development without EJB》，书中提出：既然 Web Container 能够做到 EJB Container 能做的所有事情，为什么还需要 EJB Container，基于这个问题，他使用大量的面向对象的设计方法设计了一个包含IoC和AOP的底层的框架，实现了一个没有使用 EJB Container 的在线订座系统。

2003年2月，Juergen Hoeller 和 Yann Caroff 找到了 Rod Johnson 提出想把书中一部分代码拿出来做成一个独立的框架：Spring！

2003年11月，J2EE1.4版本推出。

2004年3月，此前三人推出了 Spring 1.0 版本：包含 J2EE 的 Web Container 规范，Spring的IoC，AOP，以及xml配置Bean的方式。




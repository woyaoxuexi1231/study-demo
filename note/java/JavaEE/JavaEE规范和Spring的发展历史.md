# JavaEE的起源

Java起初拥有三个版本：

1. JavaSE - 标准版，Java SE 提供了 Java 编程语言的基础，包含了**核心库、Java 虚拟机 (JVM)、开发工具 (如 javac)** 等，是 Java 开发的基础平台
2. JavaEE- 企业版，Java EE 在 Java SE 的基础上扩展，提供了用于企业级应用程序开发的 API（起初只是一系列规范） 和运行时环境，包括 web 应用、服务端程序、分布式计算等。Java EE 现在被称为 Jakarta EE。
3. JavaME - 移动版，主要用于开发资源受限的设备上的应用程序，比如嵌入式系统、移动设备等。它提供了一个轻量级的 Java 平台，适合于小型设备的开发。目前已经几乎销声匿迹了。

1999 年 SUN 公司想在企业级应用上提出一个规范，以容器和组件的形式提供服务。共性的东西由容器管理，企业个性化的东西由组件管理，容器和组件合二为一作为企业级应用，这就是最早出现的 J2EE 的规范。

而后出现了 Tomcat，Jetty 等符合 J2EE 的规范的容器（应用服务器）。99年12月份，第一个版本的JavaEE的规范出现了。

在最初的应用架构中分为 B/S 和 C/S，基于B/S和MVC模型，JavaEE规范提出了两种容器：

- Web Container：主要负责页面渲染，仍然提供JDBC,JPA,JMS等其他规范，但是推荐使用 EJB 容器进行这些操作，Web 容器仅作为页面渲染使用。
- EJB Container：主要提供业务逻辑的处理

2001年3月 SUN公司推出了 J2EE1.3版本，新增了XML的新特性，RMI-IIOP的通信方式（Web容器和EJB容器之间通信的技术）改为XML的通信方式。

2002年10月，Rod Johnson编写了一本《expert one-to-one J2EE Development without EJB》，书中提出：既然 Web Container 能够做到 EJB Container 能做的所有事情，为什么还需要 EJB Container，基于这个问题，他使用大量的面向对象的设计方法设计了一个**包含IoC和AOP的底层的框架**，实现了一个没有使用 EJB Container 的在线订座系统。

2003年2月，Juergen Hoeller 和 Yann Caroff 找到了 Rod Johnson 提出想把书中一部分代码拿出来做成一个独立的框架：Spring！

2003年11月，J2EE1.4版本推出。

2004年3月，此前三人推出了 Spring 1.0 版本：只包含 J2EE 的 Web Container 规范，除此之外加入了Spring的两大核心特性IoC，AOP，以及xml配置Bean对象的方式。xml的配置方式在同一时间于J2EE有很大的区别，J2EE全部使用代码来进行配置，要让一个简单的 hello world 程序在ejb容器中跑起来，需要大概100行代码来进行配置。

2006年5月，J2EE在这三年期间，意识到 Spring 这些强大的对手后，也进行了改变。

- J2EE正式更名为JavaEE，并且发布JavaEE5
- 吸收Spring的配置方式，使用配置文件的方式来配置组件
- 提出ORM的规范，JPA
- 进一步增强Web Service

2006年10月，Spring发布2.0。xml文件配置进行大量简化。继续引入JavaEE的部分规范，包括JPA等，同时引入了大量的其他规范，这其中就包括了Struts，WebWork等，当时风靡一时的**SSH，就是Struts，spring，hibernate。**

2007年11月，spring发布2.5，发生了一次决定性改变。**spring引入了注解配置**，这来源于JavaEE5（JavaSE5）的注解（annotation），spring决定使用注解来代替xml的配置形式，并且**支持自动扫描注解来扫描bean对象**。同时引入了单元测试JUnit的框架和TestNG的框架。

2009年12月，JavaEE发布版本6。**吸收spring的两大核心技术Ioc(控制反转)和Aop，取名CDI和DI(依赖注入)，其实做的是同一件事情。**

同月，spring发布3.0版本。

2013年6月，JavaEE7发布。支持HTML5和WebSocket。

同月，spring4.0发布。

 2014年4月 **Spring Boot 1.0.0** 于发布。

2017年8月，JavaEE8发布，引入了**响应式编程**。JavaEE就此终结，Sun公司被Oracle收购，Oracle发现JavaEE的规范并不赚钱，所以决定把JavaEE交给了Eclipse基金会。但是Eclipse不允许使用JavaEE这个商标，Eclipse决定投票选出新的名字，最后投票的结果确定了JakartaEE(雅加达EE)。所以JakartaEE8也就是JavaEE8。

2020年11月，JakartaEE9推出。

而这之前，2018年1月，Spring推出了Spring boot2.0，推出了响应式编程，与传统的命令式编程形成完全割裂的两种编程方式。

![image-20241105011248124](C:\dataz\Project\study-demo\note\images\image-20241105011248124.png)

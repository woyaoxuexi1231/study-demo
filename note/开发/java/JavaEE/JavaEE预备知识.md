# Web 基础知识

URL统一资源定位器，Uniform Resource Locator

https://www.xmu.edu.cn:80/dept/teacher/info.html

网络协议+服务器名称+端口+路径+资源





# Servlet

![servlet工作流程_简述servlet的工作过程-CSDN博客](C:\dataz\Project\study-demo\note\images\20190401161434143.png)

要实现一个Servlet实例就必须要去实现 **jakarta.servlet.Servlet** 这个接口。

容器在收到一个Http请求后，容器会通过某个线程去选择对应的Servlet，然后执行Servlet的 **service()** 方法。

主流的Servlet的容器主要有：**Apache Tomcat**，**Jetty**，**WildFly**，**GlassFish**



# Tomcat并发

tomcat容器的线程处理请求的过程如下：

线程：read -> decode -> compute -> encode -> send

- **read**: Tomcat使用其底层的网络通信组件（如NIO、BIO或APR等连接器）来监听和接收来自客户端的连接和数据。
- **decode**: 读取到的HTTP请求数据是原始的字节流或文本。在这一步，Tomcat需要对这些数据进行解码，将它们转换为Java对象或数据结构，以便后续处理。解码过程包括解析请求行、请求头和请求体，并根据HTTP协议规范提取出所有必要的信息（如URL、参数、Cookie、认证信息等）。
- **encode**: 这一步是请求处理的核心。Tomcat根据解码后的请求信息，找到对应的Servlet或JSP文件，并调用它们来处理请求。
- **encode**: 在将响应返回给客户端之前，Tomcat需要将响应对象转换为HTTP格式的字节流或文本。编码过程包括设置响应状态码、添加响应头、序列化响应体（如将Java对象转换为JSON或XML格式）等。
- **send**: Tomcat使用底层的网络通信组件，通过之前建立的连接，将响应数据发送给客户端。

Tomcat底层为了避免大量创建线程，使用了线程池。线程池为 http-nio-{port}-exec-xxx

如果使用最原始的Tomcat应用部署，在tomcat的server.xml配置文件中可以配置线程池的各种参数，包括连接数，线程数，阻塞队列的长度。



IO密集型：内存足够大的情况下可以多开线程。

计算密集型：线程由cpu核数来决定了，开多了cpu也算不过来。



Tomcat内部的 IO 模型：

**BIO**

![image-20241107005519881](..\..\images\image-20241107005519881.png)

**NIO** 

Tomcat 8 之后的默认 IO 模式

![image-20241107005605440](..\..\images\image-20241107005605440.png)

**APR**

APR(Apache Portable Runtime/Apache可移植运行时)，是Apache HTTP服务器的支持库。可以简单地理解为，Tomcat将以JNI的形式调用Apache HTTP服务器的核心动态链接库来处理文件读取或网络传输操作，从而大大地提高Tomcat对静态文件的处理性能。 Tomcat apr也是在Tomcat上运行高并发应用的首选模式。



# Maven



 [maven使用教程.md](..\..\maven\maven使用教程.md) 



# Docker




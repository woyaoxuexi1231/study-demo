## 配置EJB(Enterprise Java Beans)服务器

要使用ejb服务，必须要使用支持ejb服务的服务器。

这里使用Tomcat EE，apache-tomee-microprofile-10.0.0-M3。

安装下载解压即可。



## 配置服务

这里配置 EJB 服务为 jar包作为 lib 引入 Java EE 服务器。JavaEE服务器在启动时会扫描这个jar包，并且扫描其中的EJB组件。

web服务以war包形式打入webapps作为前端提供服务，服务内有servlet组件，通过这些servlet组件访问EJB组件。在Servlet内通过 @EJB 引入在lib内提供的EJB服务组件。


> 1. web应用由web服务器来发布和运行
> 2. Tomcat启动时，出现了一系列中文乱码这里需要设置一下Tomcat服务器的输出文字编码找到conf文件夹下的 logging.properties 文件找到这一行： java.util.logging.ConsoleHandler.encoding = UTF-8将UTF-8修改为GBK
> 

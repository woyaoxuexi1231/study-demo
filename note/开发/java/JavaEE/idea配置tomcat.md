# Exploded Archive 

1. **Web Application: Exploded**
   - **描述**：将 Web 应用以未压缩的目录结构部署（如 `WEB-INF`、`META-INF`）。
   - **适用场景**：开发阶段快速测试，支持动态更新文件。
2. **Web Application: Archive (WAR)**
   - **描述**：标准的 Web 归档文件，压缩为 `.war` 格式，用于部署到 Servlet 容器（如 Tomcat）。
   - **适用场景**：生产环境部署 Java Web 应用



# 问题

🚨 Unrecognized option: --enable-native-access=ALL-UNNAMED

```
Using CATALINA_BASE:   "C:\Users\h1123\AppData\Local\JetBrains\IntelliJIdea2024.2\tomcat\801e11a0-bcba-49e9-8d93-006a5322b789"
Using CATALINA_HOME:   "C:\dataz\apache-tomcat-11.0.0-windows-x64\apache-tomcat-11.0.0"
Using CATALINA_TMPDIR: "C:\dataz\apache-tomcat-11.0.0-windows-x64\apache-tomcat-11.0.0\temp"
Using JRE_HOME:        "C:\dataz\Java\jdk-11.0.23"
Using CLASSPATH:       "C:\dataz\apache-tomcat-11.0.0-windows-x64\apache-tomcat-11.0.0\bin\bootstrap.jar;C:\dataz\apache-tomcat-11.0.0-windows-x64\apache-tomcat-11.0.0\bin\tomcat-juli.jar"
Using CATALINA_OPTS:   ""
Unrecognized option: --enable-native-access=ALL-UNNAMED
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
Disconnected from server
```

tomcat 应该是不支持 jdk 11的，我换到 jdk17 就正常了。鉴于此，我退回到了 tomcat 9
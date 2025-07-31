# 一个非常基础的 Dockerfile

# slim 是 Debian 的一个精简版本，适用于大多数生产环境。
# alpine 是一个超小体积的 Linux 发行版，适合对镜像体积特别敏感的场景。
# jdk 包含编译 Java 应用所需的完整工具链。
FROM openjdk:11
# 创建一个临时卷挂载点 /tmp，用于存储临时文件
# 设置卷,挂载主机目录
VOLUME /tmp
# 定义一个构建参数 JAR_FILE，允许在构建时传入参数
# 这个参数将用于指定要打包的 JAR 文件名称
ARG JAR_FILE
# 将构建上下文中的 target/${JAR_FILE} 文件添加到容器中
# 并重命名为 /app.jar
# ${JAR_FILE} 会在构建时被替换为实际值
ADD target/${JAR_FILE} /app.jar
# 设置容器启动时默认执行的命令
ENTRYPOINT ["java","-jar","/app.jar"]
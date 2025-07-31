# 一个非常基础的 Dockerfile

# slim 是 Debian 的一个精简版本，适用于大多数生产环境。
# alpine 是一个超小体积的 Linux 发行版，适合对镜像体积特别敏感的场景。
# jdk 包含编译 Java 应用所需的完整工具链。
FROM openjdk:11
VOLUME /tmp

ADD netflix-eureka-client.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
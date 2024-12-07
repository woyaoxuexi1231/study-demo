FROM openjdk:17
MAINTAINER hulei 154347188@qq.com
WORKDIR /app
ARG JAR_FILE
ADD ${JAR_FILE} /app/admin-client.jar
EXPOSE 10011
ENTRYPOINT ["java", "-jar", "admin-client.jar"]
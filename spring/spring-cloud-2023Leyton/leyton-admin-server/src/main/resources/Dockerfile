FROM openjdk:17
MAINTAINER hulei 154347188@qq.com
WORKDIR /app
ARG JAR_FILE
ADD ${JAR_FILE} /app/admin-server.jar
EXPOSE 10012
ENTRYPOINT ["java", "-jar", "admin-server.jar"]
CMD ["--spring.boot.admin.client.instance.service-base-url=http://autowiredemo:10012", "--spring.boot.admin.client.url=http://adminui:10011"]
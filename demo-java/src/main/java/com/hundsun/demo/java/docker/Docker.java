package com.hundsun.demo.java.docker;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.docker
 * @className: Docker
 * @description:
 * @author: hl
 * @createDate: 2023/5/28 17:54
 */

public class Docker {
    /*
    docker run -itd -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 --name mysql-latest mysql
    sudo docker run -p 6379:6379 --name redis -v /root/redis-6.0.6/redis.conf:/etc/redis/redis.conf -v /root/redis-6.0.6/data/:/data -d redis redis-server /etc/redis/redis.conf
    docker run --restart=always -id --name=rabbitmq -v rabbitmq-home:/var/lib/rabbitmq -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=swsk33 -e RABBITMQ_DEFAULT_PASS=123456 rabbitmq:management

     */
}

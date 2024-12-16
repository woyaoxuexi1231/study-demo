CREATE TABLE big_user
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`       varchar(255) NOT NULL COMMENT '账号',
    `ssn`             varchar(255) NOT NULL COMMENT '身份证',
    `name`            varchar(255) NOT NULL COMMENT '名字',
    `phone_number`    varchar(32)  NOT NULL COMMENT '手机号',
    `plate`           varchar(255) NOT NULL COMMENT '车牌',
    `address`         varchar(255) NOT NULL COMMENT '地址',
    `building_number` varchar(255) NOT NULL COMMENT '楼名',
    `country`         varchar(255) NOT NULL COMMENT '国家',
    `birth`           varchar(255) NOT NULL COMMENT '生日',
    `company`         varchar(255) NOT NULL COMMENT '公司',
    `job`             varchar(255) NOT NULL COMMENT '职位',
    `card_number`     varchar(255) NOT NULL COMMENT '信用卡号',
    `city`            varchar(255) NOT NULL COMMENT '城市',
    `week`            varchar(255) NOT NULL COMMENT '星期',
    `email`           varchar(255) NOT NULL COMMENT '邮件',
    `title`           varchar(255) NOT NULL COMMENT '标题',
    `paragraphs`      longtext     NOT NULL COMMENT '内容',
    `create_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 75528
  DEFAULT CHARSET = utf8mb4;
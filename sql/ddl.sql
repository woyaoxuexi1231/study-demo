-- ----------------------------
-- Table structure for `customers`
-- ----------------------------
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers`
(
    `customer_number`           int(11)     NOT NULL,
    `customer_name`             varchar(50) NOT NULL,
    `contact_last_name`         varchar(50) NOT NULL,
    `contact_first_name`        varchar(50) NOT NULL,
    `phone`                     varchar(50) NOT NULL,
    `address_line_1`            varchar(50) NOT NULL,
    `address_line_2`            varchar(50)    DEFAULT NULL,
    `city`                      varchar(50) NOT NULL,
    `state`                     varchar(50)    DEFAULT NULL,
    `postal_code`               varchar(15)    DEFAULT NULL,
    `country`                   varchar(50) NOT NULL,
    `sales_rep_employee_number` int(11)        DEFAULT NULL,
    `credit_limit`              decimal(10, 2) DEFAULT NULL,
    PRIMARY KEY (`customer_number`),
    KEY `salesRepEmployeeNumber` (`sales_rep_employee_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `employees`
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`
(
    `employee_number`  bigint       NOT NULL,
    `last_name`        varchar(50)  NOT NULL,
    `first_name`       varchar(50)  NOT NULL,
    `extension`        varchar(10)  NOT NULL,
    `email`            varchar(100) NOT NULL,
    `office_code`      varchar(10)  NOT NULL,
    `reports_to`       int(11)               DEFAULT NULL,
    `job_title`        varchar(50)  NOT NULL,
    PRIMARY KEY (`employee_number`),
    KEY `reportsTo` (`reports_to`),
    KEY `officeCode` (`office_code`),
    `last_update_time` timestamp    not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `items`
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items`
(
    `id`      int(11)      NOT NULL AUTO_INCREMENT,
    `item_no` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `offices`
-- ----------------------------
DROP TABLE IF EXISTS `offices`;
CREATE TABLE `offices`
(
    `office_code`    varchar(10) NOT NULL,
    `city`           varchar(50) NOT NULL,
    `phone`          varchar(50) NOT NULL,
    `address_line_1` varchar(50) NOT NULL,
    `address_line_2` varchar(50) DEFAULT NULL,
    `state`          varchar(50) DEFAULT NULL,
    `country`        varchar(50) NOT NULL,
    `postal_code`    varchar(15) NOT NULL,
    `territory`      varchar(10) NOT NULL,
    PRIMARY KEY (`office_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `orderdetails`
-- ----------------------------
DROP TABLE IF EXISTS `orderdetails`;
CREATE TABLE `orderdetails`
(
    `order_number`      int(11)        NOT NULL,
    `product_code`      varchar(15)    NOT NULL,
    `quantity_ordered`  int(11)        NOT NULL,
    `price_each`        decimal(10, 2) NOT NULL,
    `order_line_number` smallint(6)    NOT NULL,
    PRIMARY KEY (`order_number`, `product_code`),
    KEY `productCode` (`product_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `order_number`    int(11)     NOT NULL,
    `order_date`      date        NOT NULL,
    `required_date`   date        NOT NULL,
    `shipped_date`    date DEFAULT NULL,
    `status`          varchar(15) NOT NULL,
    `comments`        text,
    `customer_number` int(11)     NOT NULL,
    PRIMARY KEY (`order_number`),
    KEY `customerNumber` (`customer_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `payments`
-- ----------------------------
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments`
(
    `customer_number` int(11)        NOT NULL,
    `check_number`    varchar(50)    NOT NULL,
    `payment_date`    date           NOT NULL,
    `amount`          decimal(10, 2) NOT NULL,
    PRIMARY KEY (`customer_number`, `check_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `productlines`
-- ----------------------------
DROP TABLE IF EXISTS `productlines`;
CREATE TABLE `productlines`
(
    `product_line`     varchar(50) NOT NULL,
    `text_description` varchar(4000) DEFAULT NULL,
    `html_description` mediumtext,
    `image`            mediumblob,
    PRIMARY KEY (`product_line`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `products`
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `product_code`        varchar(15)    NOT NULL DEFAULT '' COMMENT '产品代码',
    `product_name`        varchar(70)    NOT NULL COMMENT '产品名称',
    `product_line`        varchar(50)    NOT NULL COMMENT '产品线',
    `product_scale`       varchar(10)    NOT NULL,
    `product_vendor`      varchar(50)    NOT NULL,
    `product_description` text           NOT NULL,
    `quantity_in_stock`   smallint(6)    NOT NULL COMMENT '库存',
    `buy_price`           decimal(10, 2) NOT NULL COMMENT '价格',
    `msrp`                decimal(10, 2) NOT NULL COMMENT '建议零售价',
    PRIMARY KEY (`product_code`),
    KEY `productLine` (`product_line`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `tokens`
-- ----------------------------
DROP TABLE IF EXISTS `tokens`;
CREATE TABLE `tokens`
(
    `s` varchar(6) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `autokeytest`;
CREATE TABLE `autokeytest`
(
    `id` int NOT NULL AUTO_INCREMENT,
    `a`  varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
    `b`  varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS `rabbitmq_test`;
CREATE TABLE `rabbitmq_test`
(
    `uuid`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `msg`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT NULL,
    `time`   timestamp                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `status` varchar(255) COLLATE utf8mb4_bin                                DEFAULT NULL,
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;


-- auto-generated definition
DROP TABLE IF EXISTS `rabbitmq_log`;
create table rabbitmq_log
(
    uuid varchar(255)                        not null
        primary key,
    msg  varchar(255)                        null,
    time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    collate = utf8mb4_bin;

DROP TABLE IF EXISTS `sequence`;
create table sequence
(
    `key` varchar(32) not null,
    value int         not null,
    constraint sequence_uindex
        unique (`key`)
);

DROP TABLE IF EXISTS `users`;
CREATE TABLE users
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

DROP TABLE IF EXISTS `product_info`;
CREATE TABLE product_info
(
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category     VARCHAR(50),
    price        DECIMAL(10, 2),
    description  TEXT,
    UNIQUE INDEX unique_product (product_name, category)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0;
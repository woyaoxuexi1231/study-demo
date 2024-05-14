-- ----------------------------
-- Table structure for `customers`
-- ----------------------------
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers`
(
    `customerNumber`         int(11)     NOT NULL,
    `customerName`           varchar(50) NOT NULL,
    `contactLastName`        varchar(50) NOT NULL,
    `contactFirstName`       varchar(50) NOT NULL,
    `phone`                  varchar(50) NOT NULL,
    `addressLine1`           varchar(50) NOT NULL,
    `addressLine2`           varchar(50)    DEFAULT NULL,
    `city`                   varchar(50) NOT NULL,
    `state`                  varchar(50)    DEFAULT NULL,
    `postalCode`             varchar(15)    DEFAULT NULL,
    `country`                varchar(50) NOT NULL,
    `salesRepEmployeeNumber` int(11)        DEFAULT NULL,
    `creditLimit`            decimal(10, 2) DEFAULT NULL,
    PRIMARY KEY (`customerNumber`),
    KEY `salesRepEmployeeNumber` (`salesRepEmployeeNumber`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `employees`
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`
(
    `employeeNumber` int(11)      NOT NULL,
    `lastName`       varchar(50)  NOT NULL,
    `firstName`      varchar(50)  NOT NULL,
    `extension`      varchar(10)  NOT NULL,
    `email`          varchar(100) NOT NULL,
    `officeCode`     varchar(10)  NOT NULL,
    `reportsTo`      int(11) DEFAULT NULL,
    `jobTitle`       varchar(50)  NOT NULL,
    PRIMARY KEY (`employeeNumber`),
    KEY `reportsTo` (`reportsTo`),
    KEY `officeCode` (`officeCode`)
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
    `officeCode`   varchar(10) NOT NULL,
    `city`         varchar(50) NOT NULL,
    `phone`        varchar(50) NOT NULL,
    `addressLine1` varchar(50) NOT NULL,
    `addressLine2` varchar(50) DEFAULT NULL,
    `state`        varchar(50) DEFAULT NULL,
    `country`      varchar(50) NOT NULL,
    `postalCode`   varchar(15) NOT NULL,
    `territory`    varchar(10) NOT NULL,
    PRIMARY KEY (`officeCode`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `orderdetails`
-- ----------------------------
DROP TABLE IF EXISTS `orderdetails`;
CREATE TABLE `orderdetails`
(
    `orderNumber`     int(11)        NOT NULL,
    `productCode`     varchar(15)    NOT NULL,
    `quantityOrdered` int(11)        NOT NULL,
    `priceEach`       decimal(10, 2) NOT NULL,
    `orderLineNumber` smallint(6)    NOT NULL,
    PRIMARY KEY (`orderNumber`, `productCode`),
    KEY `productCode` (`productCode`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `orderNumber`    int(11)     NOT NULL,
    `orderDate`      date        NOT NULL,
    `requiredDate`   date        NOT NULL,
    `shippedDate`    date DEFAULT NULL,
    `status`         varchar(15) NOT NULL,
    `comments`       text,
    `customerNumber` int(11)     NOT NULL,
    PRIMARY KEY (`orderNumber`),
    KEY `customerNumber` (`customerNumber`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `payments`
-- ----------------------------
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments`
(
    `customerNumber` int(11)        NOT NULL,
    `checkNumber`    varchar(50)    NOT NULL,
    `paymentDate`    date           NOT NULL,
    `amount`         decimal(10, 2) NOT NULL,
    PRIMARY KEY (`customerNumber`, `checkNumber`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `productlines`
-- ----------------------------
DROP TABLE IF EXISTS `productlines`;
CREATE TABLE `productlines`
(
    `productLine`     varchar(50) NOT NULL,
    `textDescription` varchar(4000) DEFAULT NULL,
    `htmlDescription` mediumtext,
    `image`           mediumblob,
    PRIMARY KEY (`productLine`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for `products`
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `productCode`        varchar(15)    NOT NULL DEFAULT '' COMMENT '产品代码',
    `productName`        varchar(70)    NOT NULL COMMENT '产品名称',
    `productLine`        varchar(50)    NOT NULL COMMENT '产品线',
    `productScale`       varchar(10)    NOT NULL,
    `productVendor`      varchar(50)    NOT NULL,
    `productDescription` text           NOT NULL,
    `quantityInStock`    smallint(6)    NOT NULL COMMENT '库存',
    `buyPrice`           decimal(10, 2) NOT NULL COMMENT '价格',
    `MSRP`               decimal(10, 2) NOT NULL COMMENT '建议零售价',
    PRIMARY KEY (`productCode`),
    KEY `productLine` (`productLine`)
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
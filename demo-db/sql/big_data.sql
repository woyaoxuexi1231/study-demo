-- 用户表
CREATE TABLE big_data_users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100),
    email      VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 商品表
CREATE TABLE big_data_products
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(200),
    category        VARCHAR(100),
    price           DECIMAL(10, 2),
    quantity        INT,
    freeze_quantity INT,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE big_data_orders
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT,
    total_amount DECIMAL(12, 2),
    status       VARCHAR(50),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 订单明细表
CREATE TABLE big_data_order_items
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id   BIGINT,
    product_id BIGINT,
    quantity   INT,
    price      DECIMAL(10, 2)
);

-- 评论表
CREATE TABLE big_data_reviews
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT,
    product_id BIGINT,
    rating     INT,
    comment    TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

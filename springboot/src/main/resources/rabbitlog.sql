-- auto-generated definition
create table rabbitmq_log
(
    uuid varchar(255)                        not null
        primary key,
    msg  varchar(255)                        null,
    time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    collate = utf8mb4_bin;


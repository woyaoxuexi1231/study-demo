-- auto-generated definition
create table sequence
(
    `key` varchar(32) not null,
    value int         not null,
    constraint sequence_uindex
        unique (`key`)
);


DROP TABLE IF EXISTS security_role;
CREATE TABLE security_role
(
    `id`   bigint(20)                    NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;
DROP TABLE IF EXISTS security_user;
CREATE TABLE security_user
(
    `id`       bigint(20)                    NOT NULL AUTO_INCREMENT,
    `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `username` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS security_user_role;
CREATE TABLE security_user_role
(
    `user_id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL,
    KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
    KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
    CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES security_user (`id`),
    CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES security_role (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

INSERT INTO security_user (id, username, password)
VALUES (1, 'user', '123456');
INSERT INTO security_user (id, username, password)
VALUES (2, 'admin', '123456');

INSERT INTO security_role (id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO security_role (id, name)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO security_user_role (user_id, role_id)
VALUES (1, 1);
INSERT INTO security_user_role (user_id, role_id)
VALUES (2, 1);
INSERT INTO security_user_role (user_id, role_id)
VALUES (2, 2);

create table security_persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);


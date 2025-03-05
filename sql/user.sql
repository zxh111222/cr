-- 用户
drop table if exists `user`;
create table `user`
(
    `id`         bigint primary key,
    `name`       varchar(20) null COMMENT '用户名',
    `email`      varchar(50) null COMMENT '邮箱',
    `password`   varchar(50) null COMMENT '密码',
    `mobile`     varchar(11) not null comment '手机号',
    `created_at` datetime(3) null comment '创建时间', -- 精度为 3 位小数秒，表示可以存储到毫秒级别。
    `updated_at` datetime(3) null comment '更新时间', -- 例如，2025-01-01 12:34:56.789
    unique key `mobile_unique` (`mobile`)
) comment ='用户';
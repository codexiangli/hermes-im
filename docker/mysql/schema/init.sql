CREATE TABLE `user` (
                        `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
                        `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户名',
                        `password` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户密码',
                        PRIMARY KEY (`id`)
) COMMENT='用户表';
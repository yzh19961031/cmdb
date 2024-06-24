CREATE DATABASE cmdb DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_0900_ai_ci;

use cmdb;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

create table relation_type
(
    id          bigint auto_increment comment '主键id'
        primary key,
    name        varchar(255)                        null comment '名称',
    description text                                null comment '描述',
    create_at   timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '关系类型';

INSERT INTO cmdb.relation_type (id, name, description, create_at) VALUES (1, '属于', '属于', '2024-05-14 09:18:57');
INSERT INTO cmdb.relation_type (id, name, description, create_at) VALUES (2, '包含', '包含', '2024-05-14 09:18:57');


create table resource_attr
(
    id                 bigint auto_increment comment '主键'
        primary key,
    model_id           bigint                              null comment '配置id',
    name               varchar(255)                        null comment '属性名称',
    identifier         varchar(255)                        null comment '属性标识',
    options            text                                null comment '可选项 如果输入框类型是下拉框 单选框 保存选项列表',
    type               tinyint                             null comment '属性类型 0 输入框 1 下拉框 2 单选框',
    is_required        tinyint                             null comment '是否必填 0 非必填 1 必填',
    default_value      text                                null comment '默认值',
    validation_rule    varchar(255)                        null comment '正则表达式',
    validation_message varchar(255)                        null comment '校验失败错误提示',
    is_deleted         tinyint   default 0                 null comment '是否删除 0 未删除 1 已删除',
    create_at          timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    modify_at          timestamp                           null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '资源属性';

create index resource_attr_config_id_index
    on resource_attr (model_id);

create table resource_group
(
    id          bigint auto_increment
        primary key,
    name        varchar(255)                        null,
    description text                                null comment '描述',
    is_deleted  tinyint   default 0                 null comment '是否删除 0 未删除  1 已删除',
    create_at   timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    modify_at   timestamp                           null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '资源分组';

create table resource_instance_relation
(
    id                 bigint auto_increment
        primary key,
    source_instance_id bigint                              null comment '源端实例id',
    source_model_id    bigint                              null comment '源端模型id',
    target_instance_id bigint                              null comment '目标端实例id',
    target_model_id    bigint                              null comment '目标端模型id',
    relation_type_id   bigint                              null comment '关系类型',
    relation_bind      tinyint                             null comment '关系约束',
    is_deleted         tinyint   default 0                 null comment '是否删除',
    create_at          timestamp default CURRENT_TIMESTAMP null,
    modify_at          timestamp                           null on update CURRENT_TIMESTAMP
)
    comment '实例关联表';

create index resource_instance_relation_source_instance_id_index
    on resource_instance_relation (source_instance_id);

create index resource_instance_relation_target_instance_id_index
    on resource_instance_relation (target_instance_id);

create table resource_model
(
    id          bigint auto_increment comment '主键id'
        primary key,
    name        varchar(255)                        null comment '资源名称',
    unique_key  varchar(255)                        null comment '唯一的属性',
    group_id    bigint                              null comment '分组id',
    table_name  varchar(64)                         null comment '表名',
    description text                                null comment '描述',
    is_enabled  tinyint   default 0                 null comment '是否开启  0 未开启  1 开启',
    is_deleted  tinyint   default 0                 null comment '是否删除 0 未删除 1 已删除',
    create_at   timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    modify_at   timestamp                           null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '资源模型';

create table resource_relation
(
    id               bigint auto_increment
        primary key,
    source_id        bigint                              null comment '源端id',
    target_id        bigint                              null comment '目标端id',
    relation_type_id bigint                              null comment '关联类型id',
    relation_bind    tinyint                             null comment '关系约束 0 一对一  1 一对多  2 多对多 ',
    create_at        timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    modify_at        timestamp                           null on update CURRENT_TIMESTAMP,
    is_deleted       tinyint   default 0                 null
)
    comment '模型关系表';

create table resource_validation
(
    id               bigint auto_increment comment '主键'
        primary key,
    model_id         bigint                              null comment '配置id',
    validate_columns varchar(512)                        null comment '唯一校验的列名 使用属性的标识拼接 用,分开',
    is_deleted       tinyint   default 0                 null comment '是否删除 0 未删除 1 已删除',
    create_at        timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    modify_at        timestamp                           null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '模型唯一校验';


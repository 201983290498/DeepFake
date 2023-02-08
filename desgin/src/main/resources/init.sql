# 创建数据库的基本表格
create database if not exists graduate_design;
use graduate_design;
drop table if exists  `usr_tbl`,`image`;
create table if not exists `image`(
    image_id nvarchar(255) not null primary key,
    image_url nvarchar(255) default ''
);
create table if not exists `usr_tbl`(
    user_id nvarchar(255) not null primary key,
    username nvarchar(255) not null,
    password nvarchar(255) not null,
    email nvarchar(255) not null comment '注册邮箱' unique,
    image_id nvarchar(255) comment '用户头像',
    create_time datetime default now(),
    status INT default 1 comment '用户状态: 1.活跃; 2.冻结;',
    foreign key (image_id) references image(image_id)
);

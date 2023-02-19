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
    first_name nvarchar(255) default null,
    last_name nvarchar(255) default null,
    phone_number nvarchar(255) default null,
    company nvarchar(255) default null,
    city nvarchar(255) default null,
    country nvarchar(255) default null,
    zip_code nvarchar(255) default null,
    user_introduction nvarchar(500) default null,
    foreign key (image_id) references image(image_id)
);
drop table if exists `file_table`, `project_tbl`, `project_file_tbl`;
create table if not exists `project_tbl` (
    detect_id int auto_increment primary key comment  '项目id,单个图片也是项目',
    project_level nvarchar(20) default 'image' comment '项目类型, image or project',
    create_time datetime default now(),
    finish_time datetime,
    project_name nvarchar(50) default ''
);
create table if not exists `file_tbl` (
    file_id int primary key auto_increment,
    file_name nvarchar(50) not null,
    file_size Long not null comment '文件大小',
    file_type nvarchar(20) not null comment '文件类型zip, file',
    file_md5 nvarchar(50) not null comment '文件的md5码',
    file_location nvarchar(255) default '' comment '大文件存储在本地的地址',
    image_id nvarchar(50) comment '文件的oss存储地址',
    file_results nvarchar(255) default '' comment 'json字符串或者检测文本的地址',
    image_quantity int  default 1 comment '文件对应的图片数量'
);
create table if not exists `project_file_tbl` (
    file_id int not null,
    detect_id int not null,
    primary key (file_id, detect_id),
    foreign key (detect_id) references project_tbl(detect_id),
    foreign key (file_id) references file_tbl(file_id)
);
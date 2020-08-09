drop table if EXISTS `chapter`;

create table `chapter`(
 `id` char(8) not null comment 'ID',
 `course_id` char(8) comment 'course ID',
 `name` varchar(50) comment 'name',
 PRIMARY KEY (`id`)
)ENGINE=innodb DEFAULT CHARSET=utf8mb4;


insert into `chapter` (id, course_id, name) VALUES ('000000', '000000', 'Test Chapter2');
insert into `chapter` (id, course_id, name) VALUES ('000001', '000001', 'Test Chapter3');
insert into `chapter` (id, course_id, name) VALUES ('000002', '000002', 'Test Chapter4');
insert into `chapter` (id, course_id, name) VALUES ('000003', '000003', 'Test Chapter5');
insert into `chapter` (id, course_id, name) VALUES ('000004', '000004', 'Test Chapter6');


drop table if exists `section`;
create table `section`(
 `id` CHAR(8) not null default '' comment 'ID',
 `title` VARCHAR(50) not null,
 `course_id` char(8),
 `chapter_id` char(8),
 `video` varchar(200),
 `time` int,
 `charge` char(1),
 `sort` int,
 `create_at` DATETIME(3),
 `update_at` DATETIME(3),
 PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into `section` (id, title, course_id, chapter_id, video, time, charge, sort, create_at, update_at)
    VALUES ('0000003','test section 3', '0000003','0000000','',500,'F',1,now(), now());

drop table if exists `course`;
create table `course`(
    `id` CHAR(8) not null default '' comment 'ID',
    name varchar(50) not null comment 'course_name',
    summary varchar(2000) comment 'summary',
    time int default 0,
    price decimal(8, 2) not null default 0.00,
    image varchar(100),
    level char(1) not null ,
    charge char(1),
    status char(1),
    enroll integer default 0,
    sort int,
    create_at datetime(3),
    update_at datetime(3),
    primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- update course c set `time` = (select sum(`time`) from section where course_id = '000000')
-- where c.id = '000000';

drop table if exists `category`;
create table `category`(
    id char(8) not null default '',
    parent char(8) not null default '',
    name varchar(50) not null,
    sort int,
    primary key (id)
)ENGINE=InnoDB default CHARSET=utf8mb4;

insert into `category` (id, parent, name, sort) values ('00000100', '00000000', 'Front-end Technique', 100);
insert into `category` (id, parent, name, sort) values ('00000101', '00000100', 'React.js', 101);
insert into `category` (id, parent, name, sort) values ('00000102', '00000100', 'Node.js', 102);
insert into `category` (id, parent, name, sort) values ('00000103', '00000100', 'Angular.js', 103);
insert into `category` (id, parent, name, sort) values ('00000104', '00000100', 'Vue.js', 104);

insert into `category` (id, parent, name, sort) values ('00000200', '00000000', 'Backend-end Technique', 200);
insert into `category` (id, parent, name, sort) values ('00000201', '00000200', 'Spring Boot and Cloud', 201);
insert into `category` (id, parent, name, sort) values ('00000202', '00000200', 'Python Django', 202);
insert into `category` (id, parent, name, sort) values ('00000203', '00000200', 'Machine Learning', 203);
insert into `category` (id, parent, name, sort) values ('00000204', '00000200', 'Kafka and Hadoop', 204);

drop table if exists `course_category`;
create table `course_category`(
    `id` char(8) not null default '',
    `course_id` char(8),
    `category_id` char(8),
    primary key (`id`)
)engine=innodb default charset=utf8mb4;

drop table if exists `course_content`;
create table `course_content`(
    `id` char(8) not null default '',
    `content` mediumtext not null,
    primary key (id)
)engine=innodb default charset=utf8mb4;

drop table if exists `teacher`;
create table `teacher`(
    `id` char(8) not null default '',
    `name` varchar(50) not null,
    `nickname` varchar(50),
    `image` varchar(100),
    `position` varchar(50),
    `intro` varchar(500),
    primary key (id)
)engine=innodb default charset=utf8mb4;

drop table if exists `file`;
create table `file`(
    `id` char(8) not null default '',
    `path` varchar(200) not null ,
    `name` varchar(100),
    `suffix` varchar(10),
    `size` int,
    `use` char(1),
    `create_at` datetime(3),
    `update_at` datetime(3),
    primary key (id),
    unique key `path_unique` (path)
)engine=innodb default charset=utf8mb4;


alter table file add column (`shard_index` int);
alter table file add column (`shard_size` int);
alter table file add column (`shard_total` int);
alter table file add column (`key` varchar(32));
alter table file add unique key key_unique(`key`);
alter table file add column (`vod` char(32));

alter table section add column (`vod` char(32));


drop table if exists `user`;
create table `user`(
    `id` char(8) not null,
    `login_name` varchar(50) not null,
    `name` varchar(50),
    `password` char(32),
    primary key (id),
    unique key (login_name)
)engine=innodb default charset=utf8mb4;

insert into user (id, login_name, name, password) values ('100000', 'admin', 'admin', '31415926');

drop table if exists `resouece`;
create table `resource`(
    `id` char(6) not null,
    `parent` char(6),
    `name` varchar (100) not null,
    `page` varchar(100) null ,
    `request` varchar (200) null ,
    primary key (id)
)engine=innodb default charset=utf8mb4;

insert into resource values ('00', null, 'System Management', null, null);
insert into resource values ('0101', '00', 'User Management', '/system/user', null);
insert into resource values ('010101', '0101', 'Save', null, '["/system/admin/user/list", "/system/admin/user/save"]');
insert into resource values ('010102', '0101', 'Delete', null, '["/system/admin/user/delete"]');
insert into resource values ('010103', '0101', 'Reset Password', null, '["/system/admin/user/save-password"]');

insert into resource values ('0102', '00', 'Resource Management', '/system/resource', null);
insert into resource values ('010201', '0102', 'Save/Show', null, '["/system/admin/resource"]');
insert into resource values ('0103', '00', 'Role Management', '/system/role', null);
insert into resource values ('010301', '0103', 'Role/Auth', null, '["/system/admin/role"]')


drop table if exists `role`;
create table `role`(
    `id` char(6) not null ,
    `name` varchar(50) not null ,
    `desc` varchar(100),
    primary key (id)
)engine=innodb default charset=utf8mb4;


drop table if exists `role_resource`;
create table `role_resource`(
    `id` char(8) not null ,
    `role_id` char(6) not null ,
    `resource_id` char(6) not null,
    primary key (id)
)engine=innodb default charset=utf8mb4;

insert into role_resource values ('000000', '000000', '01');

insert into role_resource values ('000001', '000000', '0101');
insert into role_resource values ('000002', '000000', '010101');
insert into role_resource values ('000003', '000000', '010102');
insert into role_resource values ('000004', '000000', '010103');

insert into role_resource values ('000005', '000000', '0102');
insert into role_resource values ('000006', '000000', '010201');

insert into role_resource values ('000007', '000000', '0103');
insert into role_resource values ('000008', '000000', '010301');

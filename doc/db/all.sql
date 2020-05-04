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
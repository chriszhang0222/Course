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
    VALUES ('0000001','test section 1', '0000001','0000000','',500,'F',1,now(), now());
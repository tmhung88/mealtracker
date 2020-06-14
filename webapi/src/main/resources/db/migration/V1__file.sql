SET FOREIGN_KEY_CHECKS=0;
drop table if exists users;
create table users
(
	id bigint(20) auto_increment,
	email varchar(200) not null,
	deleted tinyint(1) default 1 not null,
	full_name varchar(200) not null,
	constraint users_pk
		primary key (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create unique index users_email_uindex
	on users (email);

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO users VALUES (1, 'admin@gmail.com', 0, 'Admin');
INSERT INTO users VALUES (2, 'user_manager@gmail.com', 0, 'User Manager');
INSERT INTO users VALUES (3, 'regular_user@gmail.com', 0, 'Regular User');
INSERT INTO users VALUES (4, 'hung@gmail.com', 0, 'Regular User');

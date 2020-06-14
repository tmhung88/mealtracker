SET FOREIGN_KEY_CHECKS=0;
drop table if exists users;
create table users
(
	id bigint(20) auto_increment,
	email varchar(200) not null,
	encrypted_password varchar(100) not null,
	role int(1) not null,
	deleted tinyint(1) default 1 not null,
	full_name varchar(200) not null,
	daily_calorie_limit int default 0 not null,
	constraint users_pk
		primary key (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create unique index users_email_uindex
	on users (email);

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO users VALUES (1, 'admin@gmail.com', '$2a$10$xiohcq/oqfYE281xFiB6Oub3X.9idVUplOT08iKX6zwP9bYrvxX4m', 2, 0, 'Admin', 0);
INSERT INTO users VALUES (2, 'user_manager@gmail.com', '$2a$10$xiohcq/oqfYE281xFiB6Oub3X.9idVUplOT08iKX6zwP9bYrvxX4m', 1, 0, 'User Manager', 0);
INSERT INTO users VALUES (3, 'regular_user@gmail.com', '$2a$10$xiohcq/oqfYE281xFiB6Oub3X.9idVUplOT08iKX6zwP9bYrvxX4m', 0, 0, 'Regular User', 0);
INSERT INTO users VALUES (4, 'hung@gmail.com', '$2a$10$xiohcq/oqfYE281xFiB6Oub3X.9idVUplOT08iKX6zwP9bYrvxX4m', 2, 0, 'Regular User', 0);

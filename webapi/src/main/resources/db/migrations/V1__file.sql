create table users
(
	id bigint(20) auto_increment,
	email varchar(200) not null,
	encrypted_password varchar(100) not null,
	role int(1) not null,
	enabled tinyint(1) default 1 not null,
	full_name varchar(200) not null,
	daily_calorie_limit int default 0 not null,
	constraint users_pk
		primary key (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create unique index users_email_uindex
	on users (email);


create table meals
(
	id bigint auto_increment,
	name varchar(100) not null,
	calories int not null,
	consumed_on datetime not null,
	consumed_by bigint not null,
	constraint meals_pk primary key (id),
	constraint meals_users_id_fk foreign key (consumed_by) references users (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create index meals_consumed_by_consumed_on_index
	on meals (consumed_by asc, consumed_on desc);

create index meals_consumed_by_index
	on meals (consumed_by);

INSERT INTO mealtracker.users (id, email, encrypted_password, role, enabled, full_name, daily_calorie_limit) VALUES (1, 'admin@gmail.com', '$2a$10$RCZw83ABG6tYUvI78L.Re.s9TEelb.zgu3nu5Mg0C7u/5/b8bRM1C', 2, 1, 'Admin', 0);
INSERT INTO mealtracker.users (id, email, encrypted_password, role, enabled, full_name, daily_calorie_limit) VALUES (2, 'user_manager@gmail.com', '$2a$10$TVs2QIjD0ZLVWUfHObt9Z.gG2fpOVKd4PgVTjHpziye6QP/xEZQ06', 1, 1, 'User Manager', 0);
INSERT INTO mealtracker.users (id, email, encrypted_password, role, enabled, full_name, daily_calorie_limit) VALUES (3, 'regular_user@gmail.com', '$2a$10$AapnAlo5V4zKK2qlm8lL4uTj7FPAPjknrYazfGW1QNNwZO8rYVhVu', 0, 1, 'Regular User', 0);


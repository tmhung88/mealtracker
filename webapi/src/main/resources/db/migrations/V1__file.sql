create table users
(
	id bigint(20) auto_increment,
	email varchar(100) not null,
	encrypted_password varchar(100) not null,
	role int(1) not null,
	enabled tinyint(1) default 1 not null,
	full_name varchar(100) not null,
	daily_calories_threshold int null,
	constraint users_pk
		primary key (id)
);

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
);

create index meals_consumed_by_consumed_on_index
	on meals (consumed_by asc, consumed_on desc);

create index meals_consumed_by_index
	on meals (consumed_by);

INSERT INTO mealtracker.users (id, email, encrypted_password, role, enabled, full_name, daily_calories_threshold) VALUES (1, 'admin@gmail.com', '$2a$10$RCZw83ABG6tYUvI78L.Re.s9TEelb.zgu3nu5Mg0C7u/5/b8bRM1C', 2, 1, 'Admin', null);
INSERT INTO mealtracker.users (id, email, encrypted_password, role, enabled, full_name, daily_calories_threshold) VALUES (2, 'usermanager@gmail.com', '$2a$10$Pnj7eyAmvoZMVNjULfbH6OFWqhi4scFHQwpUqfY0sS2C3HkuuWG9q', 1, 1, 'User Manager', null);
INSERT INTO mealtracker.users (id, email, encrypted_password, role, enabled, full_name, daily_calories_threshold) VALUES (3, 'user@gmail.com', '$2a$10$4Cp2SfaAnrs3ISyCICVooOLXk9fbjVcUMNLWa6i04Wai8nVib2iom', 0, 1, 'Regular User', null);

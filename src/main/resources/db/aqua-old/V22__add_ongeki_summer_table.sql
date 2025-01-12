create table ongeki_user_boss
(
	id bigint auto_increment
		primary key,
	music_id int not null,
	damage int not null,
	is_clear bit not null,
	user_id bigint null,
    constraint UKkXe5S9552jrSJP65
        unique (user_id, music_id),
	constraint FKRvEJ2eAwDd3br6bv
		foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_scenario
(
	id bigint auto_increment
		primary key,
	scenario_id int not null,
	play_count int not null,
	user_id bigint null,
    constraint UKCovwoDYcZ532HDvs
        unique (user_id, scenario_id),
	constraint FKFyD2tqndcCe9qQMA
		foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_tech_count
(
	id bigint auto_increment
		primary key,
	level_id int not null,
	all_break_count int not null,
	all_break_plus_count int not null,
	user_id bigint null,
    constraint UKvREetXbYLNAtX5G7
        unique (user_id, level_id),
	constraint FKkg4dYVKWYr8tGkDk
		foreign key (user_id) references ongeki_user_data (id)
);
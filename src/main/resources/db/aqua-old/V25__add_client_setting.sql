create table sega_game_version
(
	uuid varchar(255)
		primary key,
	rom_version varchar(255) not null,
	data_version varchar(255) not null,
	last_time DATETIME not null
);
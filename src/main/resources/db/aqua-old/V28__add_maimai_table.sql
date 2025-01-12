create table maimai_game_event
(
	id bigint auto_increment
		primary key,
	end_date varchar(255) null,
	event_id int not null,
	start_date varchar(255) null,
	type int not null
);

create table maimai_user_data
(
	id bigint auto_increment
		primary key,
	challenge_track_phase int not null,
	combo_count int not null,
	event_point int not null,
	event_watched_date varchar(255) null,
	fever_count int not null,
	first_play_bits int not null,
	frame_id int not null,
	help_count int not null,
	highest_rating int not null,
	icon_id int not null,
	last_client_id varchar(255) null,
	last_country_code varchar(255) null,
	last_data_version int not null,
	last_login_bonus_day int not null,
	last_place_id int not null,
	last_place_name varchar(255) null,
	last_play_date varchar(255) null,
	last_region_id int not null,
	last_region_name varchar(255) null,
	last_survival_bonus_day int not null,
	login_bonus_lv int not null,
	nameplate_id int not null,
	play_count int not null,
	play_sync_count int not null,
	play_vs_count int not null,
	player_rating int not null,
	point int not null,
	rank_auth_tail_id int not null,
	total_advanced_high_score int not null,
	total_advanced_sync int not null,
	total_basic_high_score int not null,
	total_basic_sync int not null,
	total_easy_high_score int not null,
	total_easy_sync int not null,
	total_expert_high_score int not null,
	total_expert_sync int not null,
	total_hi_score int not null,
	total_high_sync int not null,
	total_lv int not null,
	total_master_high_score int not null,
	total_master_sync int not null,
	total_point int not null,
	total_re_master_high_score int not null,
	total_re_master_sync int not null,
	trophy_id int not null,
	user_name varchar(255) null,
	web_limit_date varchar(255) null,
	win_count int not null,
	aime_card_id bigint null,
	constraint FKm7wtt6742a7lnpsbvxtk2pkxj
		foreign key (aime_card_id) references sega_card (id)
);

create table maimai_user_activity
(
	id bigint auto_increment
		primary key,
	activity_id int not null,
	kind int not null,
	param1 int not null,
	param2 int not null,
	param3 int not null,
	param4 int not null,
	sort_number bigint not null,
	user_id bigint null,
	constraint FK1slyt111h8m3pkvp67nh6cq4f
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_boss
(
	id bigint auto_increment
		primary key,
	emblem_flag_list bigint not null,
	pandora_flag_list0 bigint not null,
	pandora_flag_list1 bigint not null,
	pandora_flag_list2 bigint not null,
	pandora_flag_list3 bigint not null,
	pandora_flag_list4 bigint not null,
	pandora_flag_list5 bigint not null,
	pandora_flag_list6 bigint not null,
	user_id bigint null,
	constraint FKyyl9l2hpsf9scdi0vu48i4uo
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_character
(
	id bigint auto_increment
		primary key,
	character_id int not null,
	level int not null,
	point int not null,
	user_id bigint null,
	constraint FKnpktuaib0wg0rfopcqmp6xsxq
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_general_data
(
	id bigint auto_increment
		primary key,
	property_key varchar(255) null,
	property_value text null,
	user_id bigint null,
	constraint FKn8uxwqcng96dqsv05tpvq6en6
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_item
(
	id bigint auto_increment
		primary key,
	item_id int not null,
	item_kind int not null,
	stock int not null,
	user_id bigint null,
	constraint FK78o1t4n3bx96888dgvgyiqi97
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_music_detail
(
	id bigint auto_increment
		primary key,
	achievement int not null,
	full_combo int not null,
	is_all_perfect bit not null,
	is_all_perfect_plus int not null,
	level int not null,
	max_fever int not null,
	music_id int not null,
	play_count int not null,
	score_max int not null,
	sync_rate_max int not null,
	user_id bigint null,
	constraint FKi0serv52ls8x4dnlngxnenoe2
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_option
(
	id bigint auto_increment
		primary key,
	adjust_timing int not null,
	ans_vol int not null,
	appeal_flame int not null,
	bg_info int not null,
	break_se int not null,
	break_se_vol int not null,
	brightness int not null,
	disp_judge int not null,
	disp_timing int not null,
	dmg_vol int not null,
	filter_all_perfect int not null,
	filter_difficulty int not null,
	filter_full_combo int not null,
	filter_full_sync int not null,
	filter_genre int not null,
	filter_level int not null,
	filter_max_fever int not null,
	filter_rank int not null,
	filter_re_master int not null,
	filter_rec int not null,
	filter_version int not null,
	final_select_category int not null,
	final_select_id int not null,
	guide_speed int not null,
	hard_judge int not null,
	is_fever_disp int not null,
	is_star_rot int not null,
	is_tag_jump int not null,
	is_upper_disp int not null,
	judge_pos int not null,
	mirror_mode int not null,
	note_vol int not null,
	option_mode int not null,
	rating_guard int not null,
	select_chara int not null,
	simple_option_param int not null,
	slide_se int not null,
	slide_se_vol int not null,
	sort_type int not null,
	soud_effect int not null,
	timing_pos int not null,
	track_skip int not null,
	user_id bigint null,
	constraint FKgf0cpopptvya82s8mj406fpot
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_playlog
(
	id bigint auto_increment
		primary key,
	achievement int not null,
	break_bad int not null,
	break_good int not null,
	break_great int not null,
	break_perfect int not null,
	break_score int not null,
	challenge_life int not null,
	challenge_remain int not null,
	country varchar(255) null,
	event_id int not null,
	full_combo int not null,
	game_mode int not null,
	hold_bad int not null,
	hold_good int not null,
	hold_great int not null,
	hold_perfect int not null,
	hold_score int not null,
	is_all_perfect bit not null,
	is_all_perfect_plus int not null,
	is_challenge_track bit not null,
	is_free_to_play bit not null,
	is_high_score bit not null,
	is_track_skip bit not null,
	level int not null,
	max_combo int not null,
	max_fever int not null,
	music_id int not null,
	order_id int not null,
	place_id int not null,
	place_name varchar(255) null,
	play_date varchar(255) null,
	played_music_level1 int not null,
	played_music_level2 int not null,
	played_music_level3 int not null,
	played_user_id1 bigint not null,
	played_user_id2 bigint not null,
	played_user_id3 bigint not null,
	played_user_name1 varchar(255) null,
	played_user_name2 varchar(255) null,
	played_user_name3 varchar(255) null,
	player_rating int not null,
	region_id int not null,
	rival_num int not null,
	score int not null,
	slide_bad int not null,
	slide_good int not null,
	slide_great int not null,
	slide_perfect int not null,
	slide_score int not null,
	sort_number bigint not null,
	sync_rate int not null,
	tap_bad int not null,
	tap_good int not null,
	tap_great int not null,
	tap_perfect int not null,
	tap_score int not null,
	track int not null,
	user_play_date varchar(255) null,
	vs_win int not null,
	user_id bigint null,
	constraint FK8wwroomy1dbdube88ym4rirp
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_present_event
(
	id bigint auto_increment
		primary key,
	point int not null,
	present_count int not null,
	present_event_id int not null,
	rate int not null,
	user_id bigint null,
	constraint FK662nxt52xnmf1ai8415o7phnc
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_survival
(
	id bigint auto_increment
		primary key,
	is_clear bit not null,
	is_no_damage bit not null,
	survival_id int not null,
	total_achieve int not null,
	total_score int not null,
	user_id bigint null,
	constraint FK73wots53hgxo1cww3tfr8dh61
		foreign key (user_id) references maimai_user_data (id)
);

create table maimai_user_web_option
(
	id bigint auto_increment
		primary key,
	disp_home_ranker int not null,
	disp_judge_style int not null,
	disp_rank int not null,
	disp_rate int not null,
	disp_total_lv int not null,
	is_net_member bit not null,
	user_id bigint null,
	constraint FKn1keedu2fw9i6n6ek38nwkei5
		foreign key (user_id) references maimai_user_data (id)
);


INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (1, '2129-12-31 23:59:59.0', 100108, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (2, '2129-12-31 23:59:59.0', 100109, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (3, '2129-12-31 23:59:59.0', 100110, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (4, '2129-12-31 23:59:59.0', 100111, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (5, '2129-12-31 23:59:59.0', 100208, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (6, '2129-12-31 23:59:59.0', 100209, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (7, '2129-12-31 23:59:59.0', 100210, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (8, '2129-12-31 23:59:59.0', 100211, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (9, '2129-12-31 23:59:59.0', 100308, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (10, '2129-12-31 23:59:59.0', 100309, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (11, '2129-12-31 23:59:59.0', 100310, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (12, '2129-12-31 23:59:59.0', 100311, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (13, '2129-12-31 23:59:59.0', 100408, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (14, '2129-12-31 23:59:59.0', 100409, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (15, '2129-12-31 23:59:59.0', 100410, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (16, '2129-12-31 23:59:59.0', 100411, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (17, '2129-12-31 23:59:59.0', 100501, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (18, '2129-12-31 23:59:59.0', 100502, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (19, '2129-12-31 23:59:59.0', 100503, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (20, '2129-12-31 23:59:59.0', 100504, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (21, '2129-12-31 23:59:59.0', 100505, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (22, '2129-12-31 23:59:59.0', 100506, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (23, '2129-12-31 23:59:59.0', 100507, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (24, '2129-12-31 23:59:59.0', 100508, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (25, '2129-12-31 23:59:59.0', 100509, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (26, '2129-12-31 23:59:59.0', 100510, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (27, '2129-12-31 23:59:59.0', 100511, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (28, '2129-12-31 23:59:59.0', 100601, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (29, '2129-12-31 23:59:59.0', 100602, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (30, '2129-12-31 23:59:59.0', 100603, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (31, '2129-12-31 23:59:59.0', 100604, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (32, '2129-12-31 23:59:59.0', 100605, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (33, '2129-12-31 23:59:59.0', 100606, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (34, '2129-12-31 23:59:59.0', 100607, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (35, '2129-12-31 23:59:59.0', 100608, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (36, '2129-12-31 23:59:59.0', 100609, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (37, '2129-12-31 23:59:59.0', 100610, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (38, '2129-12-31 23:59:59.0', 100611, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (39, '2129-12-31 23:59:59.0', 100700, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (40, '2129-12-31 23:59:59.0', 100701, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (41, '2129-12-31 23:59:59.0', 100702, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (42, '2129-12-31 23:59:59.0', 100703, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (43, '2129-12-31 23:59:59.0', 100800, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (44, '2129-12-31 23:59:59.0', 100801, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (45, '2129-12-31 23:59:59.0', 100802, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (46, '2129-12-31 23:59:59.0', 100803, '2019-05-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (47, '2129-12-31 23:59:59.0', 300006, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (48, '2129-12-31 23:59:59.0', 300007, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (49, '2129-12-31 23:59:59.0', 300008, '2018-08-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (50, '2129-12-31 23:59:59.0', 300009, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (51, '2129-12-31 23:59:59.0', 300010, '2018-10-16 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (52, '2129-12-31 23:59:59.0', 300011, '2018-11-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (53, '2129-12-31 23:59:59.0', 400001, '2018-12-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (54, '2129-12-31 23:59:59.0', 400002, '2019-01-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (55, '2129-12-31 23:59:59.0', 400003, '2019-02-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (56, '2129-12-31 23:59:59.0', 400004, '2019-03-08 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (57, '2129-12-31 23:59:59.0', 400005, '2019-04-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (58, '2129-12-31 23:59:59.0', 400006, '2019-04-12 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (59, '2129-12-31 23:59:59.0', 400007, '2019-04-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (60, '2129-12-31 23:59:59.0', 400101, '2018-12-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (61, '2129-12-31 23:59:59.0', 400102, '2019-05-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (62, '2129-12-31 23:59:59.0', 400103, '2019-06-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (63, '2129-12-31 23:59:59.0', 400104, '2019-06-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (64, '2129-12-31 23:59:59.0', 400105, '2019-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (65, '2129-12-31 23:59:59.0', 400109, '2019-04-12 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (66, '2129-12-31 23:59:59.0', 400110, '2019-04-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (67, '2129-12-31 23:59:59.0', 999125, '2017-04-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (68, '2129-12-31 23:59:59.0', 999146, '2017-04-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (69, '2129-12-31 23:59:59.0', 1000000, '2017-04-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (70, '2129-12-31 23:59:59.0', 18500004, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (71, '2129-12-31 23:59:59.0', 18500006, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (72, '2129-12-31 23:59:59.0', 18500008, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (73, '2129-12-31 23:59:59.0', 18500010, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (74, '2129-12-31 23:59:59.0', 18500012, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (75, '2129-12-31 23:59:59.0', 18500020, '2017-08-10 13:30:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (76, '2129-12-31 23:59:59.0', 18500024, '2017-08-10 13:30:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (77, '2129-12-31 23:59:59.0', 18500028, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (78, '2129-12-31 23:59:59.0', 18500030, '2017-06-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (79, '2129-12-31 23:59:59.0', 18501000, '2017-07-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (80, '2129-12-31 23:59:59.0', 18501002, '2017-07-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (81, '2129-12-31 23:59:59.0', 18501004, '2017-07-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (82, '2129-12-31 23:59:59.0', 18501006, '2017-07-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (83, '2129-12-31 23:59:59.0', 18501008, '2017-07-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (84, '2129-12-31 23:59:59.0', 18501010, '2017-07-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (85, '2129-12-31 23:59:59.0', 18501012, '2017-07-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (86, '2129-12-31 23:59:59.0', 18501014, '2017-07-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (87, '2129-12-31 23:59:59.0', 18502000, '2017-08-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (88, '2129-12-31 23:59:59.0', 18502002, '2017-09-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (89, '2129-12-31 23:59:59.0', 18502004, '2017-08-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (90, '2129-12-31 23:59:59.0', 18502006, '2017-08-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (91, '2129-12-31 23:59:59.0', 18502012, '2017-08-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (92, '2129-12-31 23:59:59.0', 18502014, '2017-08-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (93, '2129-12-31 23:59:59.0', 18502016, '2017-08-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (94, '2129-12-31 23:59:59.0', 18502018, '2017-08-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (95, '2129-12-31 23:59:59.0', 18503000, '2017-09-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (96, '2129-12-31 23:59:59.0', 18503002, '2017-09-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (97, '2129-12-31 23:59:59.0', 18503008, '2017-09-19 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (98, '2129-12-31 23:59:59.0', 18503010, '2017-09-19 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (99, '2129-12-31 23:59:59.0', 18503012, '2017-09-19 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (100, '2129-12-31 23:59:59.0', 18504000, '2017-10-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (101, '2129-12-31 23:59:59.0', 18504002, '2017-10-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (102, '2129-12-31 23:59:59.0', 18504004, '2017-10-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (103, '2129-12-31 23:59:59.0', 18504010, '2017-10-17 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (104, '2129-12-31 23:59:59.0', 18504012, '2017-10-17 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (105, '2129-12-31 23:59:59.0', 18504014, '2017-10-31 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (106, '2129-12-31 23:59:59.0', 18505000, '2017-11-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (107, '2129-12-31 23:59:59.0', 18505002, '2017-11-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (108, '2129-12-31 23:59:59.0', 18505004, '2017-11-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (109, '2129-12-31 23:59:59.0', 18505006, '2017-11-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (110, '2129-12-31 23:59:59.0', 18505008, '2017-11-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (111, '2129-12-31 23:59:59.0', 18505010, '2017-11-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (112, '2129-12-31 23:59:59.0', 18505012, '2017-11-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (113, '2129-12-31 23:59:59.0', 18505014, '2017-11-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (114, '2129-12-31 23:59:59.0', 18505016, '2017-11-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (115, '2129-12-31 23:59:59.0', 18506000, '2017-12-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (116, '2129-12-31 23:59:59.0', 18506002, '2017-12-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (117, '2129-12-31 23:59:59.0', 18506004, '2017-12-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (118, '2129-12-31 23:59:59.0', 18506006, '2017-12-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (119, '2129-12-31 23:59:59.0', 18506008, '2017-12-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (120, '2129-12-31 23:59:59.0', 19000000, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (121, '2129-12-31 23:59:59.0', 19000002, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (122, '2129-12-31 23:59:59.0', 19000004, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (123, '2129-12-31 23:59:59.0', 19000006, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (124, '2129-12-31 23:59:59.0', 19000016, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (125, '2129-12-31 23:59:59.0', 19000018, '2018-01-23 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (126, '2129-12-31 23:59:59.0', 19000020, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (127, '2129-12-31 23:59:59.0', 19000022, '2017-12-14 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (128, '2129-12-31 23:59:59.0', 19000024, '2017-12-26 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (129, '2129-12-31 23:59:59.0', 19001000, '2018-01-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (130, '2129-12-31 23:59:59.0', 19001002, '2018-01-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (131, '2129-12-31 23:59:59.0', 19001004, '2018-01-23 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (132, '2129-12-31 23:59:59.0', 19001014, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (133, '2129-12-31 23:59:59.0', 19002000, '2018-02-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (134, '2129-12-31 23:59:59.0', 19002002, '2018-02-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (135, '2129-12-31 23:59:59.0', 19002004, '2018-02-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (136, '2129-12-31 23:59:59.0', 19002006, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (137, '2129-12-31 23:59:59.0', 19002008, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (138, '2129-12-31 23:59:59.0', 19002016, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (139, '2129-12-31 23:59:59.0', 19002018, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (140, '2129-12-31 23:59:59.0', 19002020, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (141, '2129-12-31 23:59:59.0', 19002022, '2018-03-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (142, '2129-12-31 23:59:59.0', 19002024, '2018-02-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (143, '2129-12-31 23:59:59.0', 19002026, '2018-02-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (144, '2129-12-31 23:59:59.0', 19003000, '2018-03-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (145, '2129-12-31 23:59:59.0', 19003002, '2018-03-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (146, '2129-12-31 23:59:59.0', 19003004, '2018-03-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (147, '2129-12-31 23:59:59.0', 19003006, '2018-03-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (148, '2129-12-31 23:59:59.0', 19003014, '2018-04-17 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (149, '2129-12-31 23:59:59.0', 19004000, '2018-05-08 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (150, '2129-12-31 23:59:59.0', 19004002, '2018-04-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (151, '2129-12-31 23:59:59.0', 19004006, '2018-04-17 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (152, '2129-12-31 23:59:59.0', 19004008, '2018-04-17 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (153, '2129-12-31 23:59:59.0', 19004010, '2018-04-17 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (154, '2129-12-31 23:59:59.0', 19004020, '2018-05-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (155, '2129-12-31 23:59:59.0', 19004022, '2018-04-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (156, '2129-12-31 23:59:59.0', 19005000, '2018-05-08 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (157, '2129-12-31 23:59:59.0', 19005004, '2018-05-08 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (158, '2129-12-31 23:59:59.0', 19005006, '2018-05-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (159, '2129-12-31 23:59:59.0', 19005012, '2018-05-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (160, '2129-12-31 23:59:59.0', 19005014, '2018-06-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (161, '2129-12-31 23:59:59.0', 19006000, '2018-06-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (162, '2129-12-31 23:59:59.0', 19006002, '2018-06-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (163, '2129-12-31 23:59:59.0', 19006006, '2018-06-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (164, '2129-12-31 23:59:59.0', 19500000, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (165, '2129-12-31 23:59:59.0', 19500002, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (166, '2129-12-31 23:59:59.0', 19500004, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (167, '2129-12-31 23:59:59.0', 19500006, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (168, '2129-12-31 23:59:59.0', 19500008, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (169, '2129-12-31 23:59:59.0', 19500012, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (170, '2129-12-31 23:59:59.0', 19500020, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (171, '2129-12-31 23:59:59.0', 19500022, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (172, '2129-12-31 23:59:59.0', 19500024, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (173, '2129-12-31 23:59:59.0', 19500026, '2018-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (174, '2129-12-31 23:59:59.0', 19501000, '2018-07-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (175, '2129-12-31 23:59:59.0', 19501002, '2018-07-03 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (176, '2129-12-31 23:59:59.0', 19501004, '2018-07-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (177, '2129-12-31 23:59:59.0', 19501008, '2018-07-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (178, '2129-12-31 23:59:59.0', 19501010, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (179, '2129-12-31 23:59:59.0', 19501012, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (180, '2129-12-31 23:59:59.0', 19501014, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (181, '2129-12-31 23:59:59.0', 19501016, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (182, '2129-12-31 23:59:59.0', 19501020, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (183, '2129-12-31 23:59:59.0', 19501024, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (184, '2129-12-31 23:59:59.0', 19501026, '2018-07-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (185, '2129-12-31 23:59:59.0', 19502000, '2018-08-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (186, '2129-12-31 23:59:59.0', 19502002, '2018-08-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (187, '2129-12-31 23:59:59.0', 19502004, '2018-08-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (188, '2129-12-31 23:59:59.0', 19502006, '2018-08-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (189, '2129-12-31 23:59:59.0', 19502008, '2018-08-09 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (190, '2129-12-31 23:59:59.0', 19502010, '2018-08-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (191, '2129-12-31 23:59:59.0', 19502012, '2018-08-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (192, '2129-12-31 23:59:59.0', 19502014, '2018-08-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (193, '2129-12-31 23:59:59.0', 19502018, '2018-08-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (194, '2129-12-31 23:59:59.0', 19503000, '2018-09-04 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (195, '2129-12-31 23:59:59.0', 19503002, '2018-09-04 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (196, '2129-12-31 23:59:59.0', 19503006, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (197, '2129-12-31 23:59:59.0', 19503008, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (198, '2129-12-31 23:59:59.0', 19503010, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (199, '2129-12-31 23:59:59.0', 19503014, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (200, '2129-12-31 23:59:59.0', 19503018, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (201, '2129-12-31 23:59:59.0', 19503022, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (202, '2129-12-31 23:59:59.0', 19503024, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (203, '2129-12-31 23:59:59.0', 19503026, '2018-09-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (204, '2129-12-31 23:59:59.0', 19504000, '2018-10-02 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (205, '2129-12-31 23:59:59.0', 19504004, '2018-10-16 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (206, '2129-12-31 23:59:59.0', 19504006, '2018-10-16 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (207, '2129-12-31 23:59:59.0', 19504010, '2018-10-16 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (208, '2129-12-31 23:59:59.0', 19504014, '2018-10-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (209, '2129-12-31 23:59:59.0', 19504018, '2018-10-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (210, '2129-12-31 23:59:59.0', 19505000, '2018-11-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (211, '2129-12-31 23:59:59.0', 19505002, '2018-11-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (212, '2129-12-31 23:59:59.0', 19505006, '2018-11-06 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (213, '2129-12-31 23:59:59.0', 19505008, '2018-11-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (214, '2129-12-31 23:59:59.0', 19505010, '2018-11-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (215, '2129-12-31 23:59:59.0', 19505014, '2018-11-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (216, '2129-12-31 23:59:59.0', 19505016, '2018-11-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (217, '2129-12-31 23:59:59.0', 19506000, '2018-12-04 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (218, '2129-12-31 23:59:59.0', 19506002, '2018-12-04 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (219, '2129-12-31 23:59:59.0', 19506004, '2018-12-04 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (220, '2129-12-31 23:59:59.0', 19598014, '2018-10-16 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (221, '2129-12-31 23:59:59.0', 19598018, '2018-11-20 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (222, '2129-12-31 23:59:59.0', 19900000, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (223, '2129-12-31 23:59:59.0', 19900002, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (224, '2129-12-31 23:59:59.0', 19900004, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (225, '2129-12-31 23:59:59.0', 19900006, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (226, '2129-12-31 23:59:59.0', 19900008, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (227, '2129-12-31 23:59:59.0', 19900010, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (228, '2129-12-31 23:59:59.0', 19900012, '2018-12-18 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (229, '2129-12-31 23:59:59.0', 19900020, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (230, '2129-12-31 23:59:59.0', 19900022, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (231, '2129-12-31 23:59:59.0', 19900024, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (232, '2129-12-31 23:59:59.0', 19900026, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (233, '2129-12-31 23:59:59.0', 19900028, '2018-12-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (234, '2129-12-31 23:59:59.0', 19900030, '2018-12-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (235, '2129-12-31 23:59:59.0', 19900032, '2018-12-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (236, '2129-12-31 23:59:59.0', 19900034, '2018-12-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (237, '2129-12-31 23:59:59.0', 19900036, '2018-12-13 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (238, '2129-12-31 23:59:59.0', 19900038, '2018-12-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (239, '2129-12-31 23:59:59.0', 19901000, '2019-01-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (240, '2129-12-31 23:59:59.0', 19901002, '2019-01-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (241, '2129-12-31 23:59:59.0', 19901004, '2019-01-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (242, '2129-12-31 23:59:59.0', 19901006, '2019-01-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (243, '2129-12-31 23:59:59.0', 19901008, '2019-01-25 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (244, '2129-12-31 23:59:59.0', 19902000, '2019-02-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (245, '2129-12-31 23:59:59.0', 19902002, '2019-02-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (246, '2129-12-31 23:59:59.0', 19902004, '2019-02-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (247, '2129-12-31 23:59:59.0', 19902006, '2019-02-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (248, '2129-12-31 23:59:59.0', 19902008, '2019-02-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (249, '2129-12-31 23:59:59.0', 19902012, '2019-02-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (250, '2129-12-31 23:59:59.0', 19903000, '2019-03-08 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (251, '2129-12-31 23:59:59.0', 19903002, '2019-03-08 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (252, '2129-12-31 23:59:59.0', 19903004, '2019-03-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (253, '2129-12-31 23:59:59.0', 19903006, '2019-03-22 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (254, '2129-12-31 23:59:59.0', 19904000, '2019-04-01 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (255, '2129-12-31 23:59:59.0', 19904002, '2019-04-01 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (256, '2129-12-31 23:59:59.0', 19904004, '2019-04-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (257, '2129-12-31 23:59:59.0', 19904006, '2019-04-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (258, '2129-12-31 23:59:59.0', 19904010, '2019-04-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (259, '2129-12-31 23:59:59.0', 19904016, '2019-04-11 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (260, '2129-12-31 23:59:59.0', 19904020, '2019-04-26 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (261, '2129-12-31 23:59:59.0', 19904022, '2019-04-26 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (262, '2129-12-31 23:59:59.0', 19904024, '2019-04-26 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (263, '2129-12-31 23:59:59.0', 19905000, '2019-05-10 09:50:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (264, '2129-12-31 23:59:59.0', 19905004, '2019-05-10 09:50:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (265, '2129-12-31 23:59:59.0', 19905006, '2019-05-10 09:50:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (266, '2129-12-31 23:59:59.0', 19905008, '2019-05-10 09:50:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (267, '2129-12-31 23:59:59.0', 19905010, '2019-05-10 09:50:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (268, '2129-12-31 23:59:59.0', 19905012, '2019-05-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (269, '2129-12-31 23:59:59.0', 19905014, '2019-05-24 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (270, '2129-12-31 23:59:59.0', 19906000, '2019-06-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (271, '2129-12-31 23:59:59.0', 19906006, '2019-06-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (272, '2129-12-31 23:59:59.0', 19906008, '2019-06-07 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (273, '2129-12-31 23:59:59.0', 19906010, '2019-06-21 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (274, '2129-12-31 23:59:59.0', 19907000, '2019-07-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (275, '2129-12-31 23:59:59.0', 19907002, '2019-07-05 07:00:00.0', 1);
INSERT INTO maimai_game_event (id, end_date, event_id, start_date, type) VALUES (276, '2129-12-31 23:59:59.0', 19907004, '2019-07-05 07:00:00.0', 1);

INSERT INTO property (property_key, property_value) VALUES ('maimai_game_ranking_1', '607:2221:,288:1490:,475:1098:,792:983:,749:906:,336:830:,793:811:,125:798:,411:705:,299:653:,831:578:,829:498:,838:458:,791:432:,552:429:,181:413:,200:408:,710:388:,363:352:,647:351:,789:343:,500:335:,507:333:,521:320:,841:310:,706:307:,772:293:,21:272:,844:248:,711:221:,508:183:,447:176:,832:162:,501:151:,738:150:,499:147:,752:144:,675:143:,743:141:,188:132:,56:131:,750:131:,731:127:,811:127:,553:127:,191:120:,761:111:,315:109:,830:109:,409:105:,720:96:,204:94:,709:94:,717:93:,742:90:,843:89:,525:89:,282:89:,725:86:,371:84:,381:84:,38:82:,510:82:,412:82:,726:81:,432:80:,754:79:,670:78:,751:77:,356:74:,107:73:,536:73:,809:73:,198:72:,643:69:,199:68:,837:67:,580:66:,496:66:,535:65:,417:64:,258:64:,655:64:,694:64:,836:64:,746:62:,712:62:,781:62:,747:61:,734:60:,187:60:,509:59:,106:59:,382:58:,305:58:,379:58:,782:57:,513:57:,389:56:,227:55:');
INSERT INTO property (property_key, property_value) VALUES ('maimai_game_ranking_2', '607:2248:,288:1504:,475:1128:,792:984:,749:896:,336:890:,125:816:,793:778:,411:724:,299:717:,831:608:,829:533:,838:476:,791:459:,181:434:,552:431:,200:408:,789:363:,710:357:,507:354:,647:346:,363:337:,841:316:,706:316:,500:309:,521:303:,772:281:,21:265:,844:236:,711:215:,447:178:,508:177:,499:157:,832:154:,738:154:,501:146:,743:145:,188:142:,675:142:,56:138:,553:137:,811:131:,752:129:,750:123:,731:122:,761:114:,191:113:,409:112:,315:106:,843:104:,830:99:,282:97:,720:94:,717:90:,725:90:,709:89:,356:89:,525:87:,726:87:,204:86:,809:83:,38:81:,381:80:,198:78:,107:77:,754:77:,643:76:,747:76:,742:76:,536:75:,670:75:,432:74:,510:73:,751:73:,509:71:,199:71:,412:70:,496:69:,746:68:,187:68:,371:68:,382:66:,305:64:,379:64:,655:64:,733:63:,837:63:,258:62:,580:61:,847:61:,601:60:,781:60:,389:59:,227:59:,598:58:,691:58:,712:57:,280:56:,836:56:,417:56:');
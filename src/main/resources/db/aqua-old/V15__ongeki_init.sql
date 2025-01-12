create table ongeki_user_data
(
    id bigint auto_increment
        primary key,
    battle_point int not null,
    card_category_setting int not null,
    card_id int not null,
    card_sort_setting int not null,
    character_id int not null,
    compatible_cm_version varchar(255) null,
    event_watched_date varchar(255) null,
    exp bigint not null,
    first_data_version varchar(255) null,
    first_game_id varchar(255) null,
    first_play_date varchar(255) null,
    first_rom_version varchar(255) null,
    first_tutorial_cancel_num int not null,
    highest_rating int not null,
    jewel_count int not null,
    last_all_net_id int not null,
    last_client_id varchar(255) null,
    last_data_version varchar(255) null,
    last_game_id varchar(255) null,
    last_place_id int not null,
    last_place_name varchar(255) null,
    last_play_date varchar(255) null,
    last_play_music_level int not null,
    last_region_id int not null,
    last_region_name varchar(255) null,
    last_rom_version varchar(255) null,
    last_used_deck_id int not null,
    level int not null,
    nameplate_id int not null,
    play_count int not null,
    played_tutorial_bit int not null,
    player_rating int not null,
    point bigint not null,
    reincarnation_num int not null,
    sum_battle_advanced_high_score bigint not null,
    sum_battle_basic_high_score bigint not null,
    sum_battle_expert_high_score bigint not null,
    sum_battle_high_score bigint not null,
    sum_battle_lunatic_high_score bigint not null,
    sum_battle_master_high_score bigint not null,
    sum_tech_advanced_high_score bigint not null,
    sum_tech_basic_high_score bigint not null,
    sum_tech_expert_high_score bigint not null,
    sum_tech_high_score bigint not null,
    sum_tech_lunatic_high_score bigint not null,
    sum_tech_master_high_score bigint not null,
    tab_setting int not null,
    tab_sort_setting int not null,
    total_jewel_count int not null,
    total_point bigint not null,
    trophy_id int not null,
    user_name varchar(255) null,
    aime_card_id bigint null,
    constraint FKpbyt2rg48gq371c7pbn04r3vq
        foreign key (aime_card_id) references sega_card (id)
);

create table ongeki_user_activity
(
    id bigint auto_increment
        primary key,
    activity_id int null,
    kind int not null,
    param1 int not null,
    param2 int not null,
    param3 int not null,
    param4 int not null,
    sort_number int not null,
    user_id bigint null,
    constraint UK8upnsa6nokvlfrshwa45mn62i
        unique (user_id, kind, activity_id),
    constraint FKj1v48sg7iyelf1v95hdnnvvej
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_card
(
    id bigint auto_increment
        primary key,
    analog_stock int not null,
    card_id int not null,
    cho_kaika_date varchar(255) null,
    created varchar(255) null,
    digital_stock int not null,
    exp int not null,
    is_acquired bit not null,
    is_new bit not null,
    kaika_date varchar(255) null,
    level int not null,
    max_level int not null,
    print_count int not null,
    skill_id int not null,
    use_count int not null,
    user_id bigint null,
    constraint FK24hse5iqkcc9pr5uosdmj4tf1
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_chapter
(
    id bigint auto_increment
        primary key,
    chapter_id int not null,
    is_clear bit not null,
    is_story_watched bit not null,
    jewel_count int not null,
    last_play_music_category int not null,
    last_play_music_id int not null,
    skip_timing1 int not null,
    skip_timing2 int not null,
    user_id bigint null,
    constraint FK15v21ek6k2v978bs9gli88qxv
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_character
(
    id bigint auto_increment
        primary key,
    character_id int not null,
    intimate_count int not null,
    intimate_count_date varchar(255) null,
    intimate_count_rewarded int not null,
    intimate_level int not null,
    is_new bit not null,
    play_count int not null,
    user_id bigint null,
    constraint FKbe1tydupjuaerig417kawumvn
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_deck
(
    id bigint auto_increment
        primary key,
    card_id1 int not null,
    card_id2 int not null,
    card_id3 int not null,
    deck_id int not null,
    user_id bigint null,
    constraint FKd4g23ogtcs3u7ft88v28px21u
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_event_point
(
    id bigint auto_increment
        primary key,
    event_id int not null,
    is_ranking_rewarded bit not null,
    point bigint not null,
    user_id bigint null,
    constraint FK867x07l202cic4k1tj3fthhqb
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_item
(
    id bigint auto_increment
        primary key,
    is_valid bit not null,
    item_id int not null,
    item_kind int not null,
    stock int not null,
    user_id bigint null,
    constraint FKt4t8o65rovcopdpvf1o21hwj5
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_login_bonus
(
    id bigint auto_increment
        primary key,
    bonus_count int not null,
    bonus_id int not null,
    user_id bigint null,
    constraint FKnxfh42w1oeia9ccmrx4sx701j
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_music_detail
(
    id bigint auto_increment
        primary key,
    battle_score_max int not null,
    battle_score_rank int not null,
    clear_status int not null,
    is_all_breake bit not null,
    is_full_bell bit not null,
    is_full_combo bit not null,
    is_lock bit not null,
    is_story_watched bit not null,
    level int not null,
    max_combo_count int not null,
    max_over_kill int not null,
    max_team_over_kill int not null,
    music_id int not null,
    play_count int not null,
    tech_score_max int not null,
    tech_score_rank int not null,
    user_id bigint null,
    constraint FKe3ixhshy6d323i6eq6oc4n7c3
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_music_item
(
    id bigint auto_increment
        primary key,
    music_id int not null,
    status int not null,
    user_id bigint null,
    constraint FK5eo7lmbf3xkwl3yln76k0v6k3
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_option
(
    id bigint auto_increment
        primary key,
    abort int not null,
    color_field int not null,
    color_lane int not null,
    color_lane_bright int not null,
    color_side int not null,
    dispbp int not null,
    disp_player_lv int not null,
    disp_rating int not null,
    effect_damage int not null,
    effect_pos int not null,
    headphone int not null,
    judge_break int not null,
    judge_disp int not null,
    judge_hit int not null,
    judge_pos int not null,
    judge_timing int not null,
    matching int not null,
    mirror int not null,
    option_set int not null,
    speed int not null,
    tap_sound int not null,
    vol_all int not null,
    vol_bell int not null,
    vol_cr_tap int not null,
    vol_damage int not null,
    vol_enemy int not null,
    vol_flick int not null,
    vol_guide int not null,
    vol_hold int not null,
    vol_side int not null,
    vol_skill int not null,
    vol_tap int not null,
    user_id bigint null,
    constraint FKbnoj66ua8ce2i90i13br6dg9h
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_playlog
(
    id bigint auto_increment
        primary key,
    battle_point int not null,
    battle_score int not null,
    battle_score_rank int not null,
    bell_count int not null,
    boss_attribute int not null,
    boss_chara_id int not null,
    boss_level int not null,
    card_attack1 int not null,
    card_attack2 int not null,
    card_attack3 int not null,
    card_id1 int not null,
    card_id2 int not null,
    card_id3 int not null,
    card_level1 int not null,
    card_level2 int not null,
    card_level3 int not null,
    clear_status int not null,
    damage_count int not null,
    event_id int not null,
    event_name varchar(255) null,
    event_point int not null,
    is_all_break bit not null,
    is_battle_new_record bit not null,
    is_full_bell bit not null,
    is_full_combo bit not null,
    is_over_damage_new_record bit not null,
    is_tech_new_record bit not null,
    judge_break int not null,
    judge_critical_break int not null,
    judge_hit int not null,
    judge_miss int not null,
    level int not null,
    max_combo int not null,
    music_id int not null,
    over_damage int not null,
    place_id int not null,
    place_name varchar(255) null,
    play_date varchar(255) null,
    play_kind int not null,
    played_music_level1 int not null,
    played_music_level2 int not null,
    played_music_level3 int not null,
    played_user_id1 int not null,
    played_user_id2 int not null,
    played_user_id3 int not null,
    played_user_name1 varchar(255) null,
    played_user_name2 varchar(255) null,
    played_user_name3 varchar(255) null,
    player_rating int not null,
    rate_flick int not null,
    rate_hold int not null,
    rate_side_hold int not null,
    rate_side_tap int not null,
    rate_tap int not null,
    sort_number int not null,
    tech_score int not null,
    tech_score_rank int not null,
    total_bell_count int not null,
    user_play_date varchar(255) null,
    user_id bigint null,
    constraint FKltrwbtx3jfv3gdlk11q4fl311
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_story
(
    id bigint auto_increment
        primary key,
    last_chapter_id int not null,
    story_id int not null,
    user_id bigint null,
    constraint FKrjs8eu3c3ottsk9ogqf0s6p44
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_user_training_room
(
    id bigint auto_increment
        primary key,
    auth_key varchar(255) null,
    card_id int not null,
    room_id int not null,
    value_date varchar(255) null,
    user_id bigint null,
    constraint FK6ixn4aqiny02kxt4kq0tu8fk
        foreign key (user_id) references ongeki_user_data (id)
);


SET FOREIGN_KEY_CHECKS=0;
DROP TABLE wacca_friend, wacca_user_bingo, wacca_user_favorite_song, wacca_user_gate, wacca_user_item, wacca_user_option, wacca_user_playlog, wacca_user_score, wacca_user_stageup, wacca_user;
SET FOREIGN_KEY_CHECKS=1;

create table wacca_user
(
    id                      bigint auto_increment
        primary key,
    aime_card_id            bigint       not null,
    user_name               varchar(8)   not null,
    xp                      int          not null,
    wp                      int          not null,
    wp_total                int          not null,
    wp_spent                int          not null,
    dan_type                int          not null,
    dan_level               int          not null,
    titles                  varchar(255) not null,
    player_rating           int          not null,
    highest_rating          int          not null,
    always_vip              bit          not null,
    login_count             int          not null,
    login_count_days        int          not null,
    login_count_days_consec int          not null,
    login_count_today       int          not null,
    play_counts             varchar(255) not null,
    friend_views            varchar(255) not null,
    last_rom_version        varchar(50)  not null,
    last_song_info          varchar(255) not null,
    gate_tutorial_flags     varchar(255) not null,
    last_play_date          datetime     not null,
    first_play_date         datetime     not null,
    vip_expire_time         datetime     not null,
    last_consec_date        datetime     not null,
    favorite_songs          TEXT         not null,
    total_score             bigint       not null,
    constraint wacca_user_detail_unique
        unique (aime_card_id),
    constraint wacca_user_detail_fk
        foreign key (aime_card_id) references sega_card (id)
);

create table wacca_friend
(
    id          bigint auto_increment
        primary key,
    user_id     bigint not null,
    `with`      bigint not null,
    is_accepted bit    not null,
    constraint wacca_friend_unique
        unique (user_id, `with`),
    constraint fku_wacca_friend
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade,
    constraint fku_wacca_friend_2
        foreign key (`with`) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_bingo
(
    id            bigint auto_increment
        primary key,
    user_id       bigint       not null,
    page_number   int          not null,
    page_progress varchar(255) null,
    constraint wacca_user_bingo_unique
        unique (user_id, page_number),
    constraint fku_wacca_user_bingo
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_gate
(
    id           bigint auto_increment
        primary key,
    user_id      bigint   not null,
    gate_id      int      not null,
    page         int      not null,
    progress     int      not null,
    loops        int      not null,
    mission_flag int      not null,
    total_points int      not null,
    last_used    datetime not null,
    constraint wacca_user_gate_unique
        unique (user_id, gate_id),
    constraint fku_wacca_user_gate
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_item
(
    id            bigint auto_increment
        primary key,
    user_id       bigint   not null,
    item_id       int      not null,
    type          int      not null,
    p1            bigint   not null,
    p2            bigint   not null,
    p3            bigint   not null,
    acquired_date datetime not null,
    constraint wacca_user_item_unique
        unique (user_id, item_id, type),
    constraint fku_wacca_user_item
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_option
(
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    opt_id  int    not null,
    value   int    not null,
    constraint wacca_user_option_unique
        unique (user_id, opt_id),
    constraint fku_wacca_user_option
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_playlog
(
    id          bigint auto_increment
        primary key,
    user_id     bigint       not null,
    music_id    int          not null,
    level       int          not null,
    level_const double       not null,
    achievement int          not null,
    grade       int          not null,
    max_combo   int          not null,
    fast_ct     int          not null,
    late_ct     int          not null,
    is_clear    bit          not null,
    is_missless bit          not null,
    is_full_combo bit        not null,
    is_all_perfect bit       not null,
    give_up     bit          not null,
    judgements  varchar(255) not null,
    new_record  bit          not null,
    skill_pt    int          not null,
    user_play_date datetime  not null,
    after_rating int         not null,
    before_rating int        not null,
    constraint wacca_user_playlog_unique
        unique (user_id, music_id, level, user_play_date),
    constraint fku_wacca_user_playlog
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_score
(
    id             bigint auto_increment
        primary key,
    user_id        bigint       not null,
    music_id       int          not null,
    level          int          not null,
    achievement    int          not null,
    best_combo     int          not null,
    lowest_miss_ct int          not null,
    rating         int          not null,
    clears         varchar(255) not null,
    grades         varchar(255) not null,
    constraint wacca_user_score_unique
        unique (user_id, music_id, level),
    constraint fku_wacca_user_score
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);

create table wacca_user_stageup
(
    id            bigint auto_increment
        primary key,
    user_id       bigint not null,
    stage_id      int    not null,
    clear_status  int    not null,
    clear_song_ct int    not null,
    song_scores   varchar(255) not null,
    play_ct       int    not null,
    constraint wacca_user_stageup_unique
        unique (user_id, stage_id),
    constraint fku_wacca_user_stageup
        foreign key (user_id) references wacca_user (id)
            on update cascade on delete cascade
);
CREATE TABLE maimai2_user_detail (
    id BIGINT auto_increment PRIMARY KEY,
    aime_card_id                BIGINT,
    user_name                   VARCHAR (255),
    is_net_member               INTEGER,
    icon_id                     INTEGER,
    plate_id                    INTEGER,
    title_id                    INTEGER,
    partner_id                  INTEGER,
    frame_id                    INTEGER,
    select_map_id               INTEGER,
    total_awake                 INTEGER,
    grade_rating                INTEGER,
    music_rating                INTEGER,
    player_rating               INTEGER,
    highest_rating              INTEGER,
    grade_rank                  INTEGER,
    chara_slot                  VARCHAR (255),
    chara_lock_slot             VARCHAR (255),
    content_bit                 BIGINT,
    play_count                  INTEGER,
    event_watched_date          VARCHAR (255),
    last_game_id                VARCHAR (255),
    last_rom_version            VARCHAR (255),
    last_data_version           VARCHAR (255),
    last_login_date             VARCHAR (255),
    last_play_date              VARCHAR (255),
    last_place_id               INTEGER,
    last_place_name             VARCHAR (255),
    last_all_net_id             INTEGER,
    last_region_id              INTEGER,
    last_region_name            VARCHAR (255),
    last_client_id              VARCHAR (255),
    last_country_code           VARCHAR (255),
    first_game_id               VARCHAR (255),
    first_rom_version           VARCHAR (255),
    first_data_version          VARCHAR (255),
    first_play_date             VARCHAR (255),
    compatible_cm_version       VARCHAR (255),
    daily_bonus_date            VARCHAR (255),
    play_vs_count               INTEGER,
    play_sync_count             INTEGER,
    win_count                   INTEGER,
    help_count                  INTEGER,
    combo_count                 INTEGER,
    total_deluxscore            BIGINT,
    total_basic_deluxscore      BIGINT,
    total_advanced_deluxscore   BIGINT,
    total_expert_deluxscore     BIGINT,
    total_master_deluxscore     BIGINT,
    total_re_master_deluxscore  BIGINT,
    total_sync                  INTEGER,
    total_basic_sync            INTEGER,
    total_advanced_sync         INTEGER,
    total_expert_sync           INTEGER,
    total_master_sync           INTEGER,
    total_re_master_sync        INTEGER,
    total_achievement           BIGINT,
    total_basic_achievement     BIGINT,
    total_advanced_achievement  BIGINT,
    total_expert_achievement    BIGINT,
    total_master_achievement    BIGINT,
    total_re_master_achievement BIGINT,
    date_time                   BIGINT,
    constraint FKbv9jxq8qw3vvgvio
        foreign key (aime_card_id) references sega_card (id)
);


CREATE TABLE maimai2_user_activity (
    id BIGINT auto_increment PRIMARY KEY,
    kind        INTEGER,
    activity_id INTEGER,
    sort_number BIGINT,
    param1      INTEGER,
    param2      INTEGER,
    param3      INTEGER,
    param4      INTEGER,
    user_id     BIGINT,
    constraint FKzosmkjggsr4kbwp8
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_character (
    id BIGINT auto_increment PRIMARY KEY,
    character_id INTEGER,
    level        INTEGER,
    awakening    INTEGER,
    use_count    INTEGER,
    user_id      BIGINT,
    constraint FK7pwqw2iax9tkqsio
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_extend (
    id BIGINT auto_increment PRIMARY KEY,
    select_music_id       INTEGER,
    select_difficulty_id  INTEGER,
    category_index        INTEGER,
    music_index           INTEGER,
    extra_flag            INTEGER,
    select_score_type     INTEGER,
    extend_content_bit    BIGINT,
    is_photo_agree        BOOLEAN,
    is_goto_code_read     BOOLEAN,
    select_result_details BOOLEAN,
    sort_category_setting INTEGER,
    sort_music_setting    INTEGER,
    selected_card_list    VARCHAR (255),
    user_id               BIGINT,
    constraint FKkohdzk55oj46xyeq
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_favorite (
    id BIGINT auto_increment PRIMARY KEY,
    fav_user_id  BIGINT,
    item_kind    INTEGER,
    item_id_list VARCHAR (255),
    user_id      BIGINT,
    constraint FKhedsx72h28k53obe
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_item (
    id BIGINT auto_increment PRIMARY KEY,
    item_kind INTEGER,
    item_id   INTEGER,
    stock     INTEGER,
    is_valid  BOOLEAN,
    user_id   BIGINT,
    constraint FKopxaz95c966q7pys
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_login_bonus (
    id BIGINT auto_increment PRIMARY KEY,
    bonus_id    INTEGER,
    point       INTEGER,
    is_current  BOOLEAN,
    is_complete BOOLEAN,
    user_id     BIGINT,
    constraint FKmwccayih2sv2smw7
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_map (
    id BIGINT auto_increment PRIMARY KEY,
    map_id      INTEGER,
    distance    INTEGER,
    is_lock     BOOLEAN,
    is_clear    BOOLEAN,
    is_complete BOOLEAN,
    user_id     BIGINT,
    constraint FKjva7jtg96nwt9539
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_music_detail (
    id BIGINT auto_increment PRIMARY KEY,
    music_id       INTEGER,
    level          INTEGER,
    play_count     INTEGER,
    achievement    INTEGER,
    combo_status   INTEGER,
    sync_status    INTEGER,
    deluxscore_max INTEGER,
    score_rank     INTEGER,
    user_id        BIGINT,
    constraint FK8hsx2tb67q8nqxgk
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_npc_encount (
    id BIGINT auto_increment PRIMARY KEY,
    npc_id    INTEGER,
    music_id  INTEGER,
    extend_id BIGINT,
    user_id   BIGINT,
    constraint FKi6sfpfh45h98qjnh
        foreign key (user_id) references maimai2_user_detail (id),
    constraint FKmfy3j84f6k2ymgxb
        foreign key (extend_id) references maimai2_user_extend (id)
);


CREATE TABLE maimai2_user_option (
    id BIGINT auto_increment PRIMARY KEY,
    option_kind          INTEGER,
    note_speed           INTEGER,
    slide_speed          INTEGER,
    touch_speed          INTEGER,
    tap_design           INTEGER,
    hold_design          INTEGER,
    slide_design         INTEGER,
    star_type            INTEGER,
    outline_design       INTEGER,
    note_size            INTEGER,
    slide_size           INTEGER,
    touch_size           INTEGER,
    star_rotate          INTEGER,
    disp_center          INTEGER,
    disp_chain           INTEGER,
    disp_rate            INTEGER,
    disp_bar             INTEGER,
    touch_effect         INTEGER,
    submonitor_animation INTEGER,
    submonitor_achive    INTEGER,
    submonitor_appeal    INTEGER,
    matching             INTEGER,
    track_skip           INTEGER,
    brightness           INTEGER,
    mirror_mode          INTEGER,
    disp_judge           INTEGER,
    disp_judge_pos       INTEGER,
    disp_judge_touch_pos INTEGER,
    adjust_timing        INTEGER,
    ans_volume           INTEGER,
    tap_hold_volume      INTEGER,
    critical_se          INTEGER,
    break_se             INTEGER,
    break_volume         INTEGER,
    ex_se                INTEGER,
    ex_volume            INTEGER,
    slide_se             INTEGER,
    slide_volume         INTEGER,
    touch_hold_volume    INTEGER,
    damage_se_volume     INTEGER,
    head_phone_volume    INTEGER,
    sort_tab             INTEGER,
    sort_music           INTEGER,
    user_id              BIGINT,
    constraint FKp3r3s8f6mwfvoyzf
        foreign key (user_id) references maimai2_user_detail (id)
);



CREATE TABLE maimai2_user_rate (
    id BIGINT auto_increment PRIMARY KEY,
    music_id    INTEGER,
    level       INTEGER,
    rom_version INTEGER,
    achievement INTEGER,
    user_id     BIGINT,
    constraint FKfaewgvanchzwo8um
        foreign key (user_id) references maimai2_user_detail (id)
);


CREATE TABLE maimai2_user_udemae (
    id BIGINT auto_increment PRIMARY KEY,
    rate               INTEGER,
    max_rate           INTEGER,
    total_win_num      INTEGER,
    total_lose_num     INTEGER,
    max_win_num        INTEGER,
    max_lose_num       INTEGER,
    win_num            INTEGER,
    lose_num           INTEGER,
    npc_total_win_num  INTEGER,
    npc_total_lose_num INTEGER,
    npc_max_win_num    INTEGER,
    npc_max_lose_num   INTEGER,
    npc_win_num        INTEGER,
    npc_lose_num       INTEGER,
    user_id            BIGINT,
    constraint FK9g2niydg6r5796gg
        foreign key (user_id) references maimai2_user_detail (id)
);


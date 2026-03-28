CREATE TABLE wacca_user
(
    id                      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    aime_card_id            BIGINT                NOT NULL,
    username                VARCHAR(8)            NOT NULL,
    xp                      INT                   NOT NULL,
    wp                      INT                   NOT NULL,
    wp_total                INT                   NOT NULL,
    wp_spent                INT                   NOT NULL,
    dan_type                INT                   NOT NULL,
    dan_level               INT                   NOT NULL,
    title0                  INT                   NOT NULL,
    title1                  INT                   NOT NULL,
    title2                  INT                   NOT NULL,
    rating                  INT                   NOT NULL,
    vip_expire_time         VARCHAR(255)          NULL,
    always_vip              BIT(1)                NOT NULL,
    login_count             INT                   NOT NULL,
    login_count_consec      INT                   NOT NULL,
    login_count_days        INT                   NOT NULL,
    login_count_days_consec INT                   NOT NULL,
    login_count_today       INT                   NOT NULL,
    playcount_single        INT                   NOT NULL,
    playcount_multi_vs      INT                   NOT NULL,
    playcount_multi_coop    INT                   NOT NULL,
    playcount_stageup       INT                   NOT NULL,
    playcount_time_free     INT                   NOT NULL,
    friend_view1            INT                   NOT NULL,
    friend_view2            INT                   NOT NULL,
    friend_view3            INT                   NOT NULL,
    last_game_ver           VARCHAR(50)           NULL,
    last_song_id            INT                   NOT NULL,
    last_song_difficulty    INT                   NOT NULL,
    last_folder_order       INT                   NOT NULL,
    last_folder_id          INT                   NOT NULL,
    last_song_order         INT                   NOT NULL,
    last_login_date         VARCHAR(255)          NULL,
    gate_tutorial_flags     VARCHAR(255)          NULL,
    CONSTRAINT wacca_user_detail_unique UNIQUE (aime_card_id),
    CONSTRAINT wacca_user_detail_fk FOREIGN KEY (aime_card_id) REFERENCES sega_card (id)
);

CREATE TABLE wacca_user_bingo
(
    id            BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id       BIGINT                NOT NULL,
    page_number   INT                   NOT NULL,
    page_progress VARCHAR(255)          NULL,
    CONSTRAINT wacca_user_bingo_unique UNIQUE (user_id, page_number),
    CONSTRAINT fku_wacca_user_bingo FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_favorite_song
(
    id      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id BIGINT                NOT NULL,
    song_id INT                   NOT NULL,
    CONSTRAINT wacca_user_favorite_song_unique UNIQUE (user_id, song_id),
    CONSTRAINT fku_wacca_user_favorite_song FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_friend
(
    id               BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id          BIGINT                NOT NULL,
    `with`           BIGINT                NOT NULL,
    is_accepted      BIT(1)                NOT NULL,
    CONSTRAINT wacca_friend_unique UNIQUE (user_id, `with`),
    CONSTRAINT fku_wacca_friend FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fku_wacca_friend_2 FOREIGN KEY (`with`) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_gate
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id      BIGINT                NOT NULL,
    gate_id      INT                   NOT NULL,
    page         INT                   NOT NULL,
    progress     INT                   NOT NULL,
    loops        INT                   NOT NULL,
    mission_flag INT                   NOT NULL,
    total_points INT                   NOT NULL,
    CONSTRAINT wacca_user_gate_unique UNIQUE (user_id, gate_id),
    CONSTRAINT fku_wacca_user_gate FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_item
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id      BIGINT                NOT NULL,
    item_id      INT                   NOT NULL,
    type         INT                   NOT NULL,
    acquire_date VARCHAR(255)          NULL,
    use_count    INT                   NOT NULL,
    CONSTRAINT wacca_user_item_unique UNIQUE (user_id, item_id),
    CONSTRAINT fku_wacca_user_item FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_score
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id         BIGINT                NOT NULL,
    song_id         INT                   NOT NULL,
    chart_id        INT                   NOT NULL,
    score           INT                   NOT NULL,
    play_ct         INT                   NOT NULL,
    clear_ct        INT                   NOT NULL,
    missless_ct     INT                   NOT NULL,
    fullcombo_ct    INT                   NOT NULL,
    allmarv_ct      INT                   NOT NULL,
    gradedct        INT                   NOT NULL,
    gradecct        INT                   NOT NULL,
    gradebct        INT                   NOT NULL,
    gradeact        INT                   NOT NULL,
    gradeaact       INT                   NOT NULL,
    gradeaaact      INT                   NOT NULL,
    gradesct        INT                   NOT NULL,
    gradessct       INT                   NOT NULL,
    gradesssct      INT                   NOT NULL,
    grade_master_ct INT                   NOT NULL,
    grade_sp_ct     INT                   NOT NULL,
    grade_ssp_ct    INT                   NOT NULL,
    grade_sssp_ct   INT                   NOT NULL,
    best_combo      INT                   NOT NULL,
    lowest_miss_ct  INT                   NOT NULL,
    rating          INT                   NOT NULL,
    CONSTRAINT wacca_user_score_unique UNIQUE (user_id, song_id, chart_id),
    CONSTRAINT fku_wacca_user_score FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_playlog
(
    id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id     BIGINT                NOT NULL,
    song_id     INT                   NOT NULL,
    chart_id    INT                   NOT NULL,
    score       INT                   NOT NULL,
    clear       INT                   NOT NULL,
    grade       INT                   NOT NULL,
    max_combo   INT                   NOT NULL,
    marv_ct     INT                   NOT NULL,
    great_ct    INT                   NOT NULL,
    good_ct     INT                   NOT NULL,
    miss_ct     INT                   NOT NULL,
    fast_ct     INT                   NOT NULL,
    late_ct     INT                   NOT NULL,
    season      INT                   NOT NULL,
    date_scored VARCHAR(255)          NULL,
    CONSTRAINT wacca_user_playlog_unique UNIQUE (user_id, song_id, chart_id),
    CONSTRAINT fku_wacca_user_playlog FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_stageup
(
    id            BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id       BIGINT                NOT NULL,
    version       INT                   NOT NULL,
    stage_id      INT                   NOT NULL,
    clear_status  INT                   NOT NULL,
    clear_song_ct INT                   NOT NULL,
    song1score    INT                   NOT NULL,
    song2score    INT                   NOT NULL,
    song3score    INT                   NOT NULL,
    play_ct       INT                   NOT NULL,
    CONSTRAINT wacca_user_stageup_unique UNIQUE (user_id, version, stage_id),
    CONSTRAINT fku_wacca_user_stageup FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_song_unlock
(
    id                 BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id            BIGINT                NOT NULL,
    song_id            INT                   NOT NULL,
    highest_difficulty INT                   NOT NULL,
    acquire_date       VARCHAR(255)          NULL,
    CONSTRAINT wacca_user_song_unlock_unique UNIQUE (user_id, song_id),
    CONSTRAINT fku_wacca_user_song_unlock FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_ticket
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id      BIGINT                NOT NULL,
    ticket_id    INT                   NOT NULL,
    acquire_date VARCHAR(255)          NULL,
    expire_date  VARCHAR(255)          NULL,
    CONSTRAINT wacca_user_ticket_unique UNIQUE (user_id, ticket_id),
    CONSTRAINT fku_wacca_user_ticket FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_trophy
(
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id    BIGINT                NOT NULL,
    trophy_id  INT                   NOT NULL,
    season     INT                   NOT NULL,
    progress   INT                   NOT NULL,
    badge_type INT                   NOT NULL,
    CONSTRAINT wacca_user_trophy_unique UNIQUE (user_id, trophy_id, season),
    CONSTRAINT fku_wacca_user_trophy FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_option
(
    id      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id BIGINT                NOT NULL,
    opt_id  INT                   NOT NULL,
    value   INT                   NOT NULL,
    CONSTRAINT wacca_user_option_unique UNIQUE (user_id, opt_id),
    CONSTRAINT fku_wacca_user_option FOREIGN KEY (user_id) REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);
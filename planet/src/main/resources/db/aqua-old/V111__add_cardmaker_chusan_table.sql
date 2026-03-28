CREATE TABLE chusan_game_gacha (
    id                BIGINT auto_increment PRIMARY KEY,
    gacha_id          INTEGER NOT NULL,
    gacha_name        VARCHAR(255),
    `type`            INTEGER,
    kind              INTEGER,
    is_ceiling        BOOLEAN,
    ceiling_cnt       INTEGER,
    change_rate_cnt1  INTEGER,
    change_rate_cnt2  INTEGER,
    start_date        DATETIME,
    end_date          DATETIME,
    notice_start_date DATETIME,
    notice_end_date   DATETIME
);

CREATE TABLE chusan_game_gacha_card (
    id        BIGINT auto_increment PRIMARY KEY,
    gacha_id  INTEGER NOT NULL,
    card_id   INTEGER NOT NULL,
    rarity    INTEGER,
    weight    INTEGER,
    is_pickup BOOLEAN
);

CREATE TABLE chusan_user_gacha (
    id                BIGINT auto_increment PRIMARY KEY,
    gacha_id          INTEGER,
    total_gacha_cnt   INTEGER,
    ceiling_gacha_cnt INTEGER,
    daily_gacha_cnt   INTEGER,
    five_gacha_cnt    INTEGER,
    eleven_gacha_cnt  INTEGER,
    daily_gacha_date  DATETIME,
    user_id           BIGINT,
    constraint FKKgEwDqNazYzQYXQ6aM
        foreign key (user_id) references chusan_user_data (id)
);

CREATE TABLE chusan_user_print_state (
    id            BIGINT auto_increment PRIMARY KEY,
    has_completed BOOLEAN,
    limit_date    DATETIME,
    place_id      INTEGER,
    card_id       INTEGER,
    gacha_id      INTEGER,
    user_id       BIGINT,
    constraint FKYWeiCGeXh6fYTym8KY
        foreign key (user_id) references chusan_user_data (id)
);

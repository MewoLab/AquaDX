CREATE TABLE maimai2_game_charge (
    id         BIGINT auto_increment PRIMARY KEY,
    order_id   INTEGER,
    charge_id  INTEGER,
    price      INTEGER,
    start_date VARCHAR (255),
    end_date   VARCHAR (255),
    constraint UKu8vb9gsi4qvaqdcn
        unique (charge_id)
);

CREATE TABLE maimai2_user_charge (
    id            BIGINT auto_increment PRIMARY KEY,
    charge_id     INTEGER,
    stock         INTEGER,
    purchase_date VARCHAR (255),
    valid_date    VARCHAR (255),
    user_id       BIGINT,
    constraint UKm7xas9pcf3hf7grv
        unique (charge_id, user_id),
    constraint FKzxm95bz5qiqmzof9
        foreign key (user_id) references maimai2_user_detail (id)
);

CREATE TABLE maimai2_user_course (
    id                    BIGINT auto_increment PRIMARY KEY,
    course_id             INTEGER,
    is_last_clear         BOOLEAN,
    total_rest_life       INTEGER,
    total_achievement     INTEGER,
    total_deluxscore      INTEGER,
    play_count            INTEGER,
    clear_date            VARCHAR (255),
    last_play_date        VARCHAR (255),
    best_achievement      INTEGER,
    best_achievement_date VARCHAR (255),
    best_deluxscore       INTEGER,
    best_deluxscore_date  VARCHAR (255),
    user_id               BIGINT,
    constraint FKe7xdxfwuxr4ik5vz
        foreign key (user_id) references maimai2_user_detail (id)
);

ALTER TABLE `maimai2_user_detail`
    ADD COLUMN `class_rank` int DEFAULT 0,
    ADD COLUMN `course_rank` int DEFAULT 0,
    ADD COLUMN `last_play_credit` int DEFAULT 1,
    ADD COLUMN `last_play_mode` int DEFAULT 0,
    ADD COLUMN `last_selectemoney` int DEFAULT 0,
    ADD COLUMN `last_select_ticket` int DEFAULT 0,
    ADD COLUMN `last_select_course` int DEFAULT 0,
    ADD COLUMN `last_count_course` int DEFAULT 0,
    ADD COLUMN `daily_course_bonus_date` VARCHAR (255) DEFAULT "2000-01-01 05:00:00.0";

ALTER TABLE `maimai2_user_option`
    ADD COLUMN `judge_timing` int DEFAULT 0;

ALTER TABLE `maimai2_user_udemae`
    ADD COLUMN `class_value` int DEFAULT 0,
    ADD COLUMN `max_class_value` int DEFAULT 0;
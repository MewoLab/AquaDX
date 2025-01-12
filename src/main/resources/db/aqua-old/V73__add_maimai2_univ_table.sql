ALTER TABLE `maimai2_user_detail`
    ADD COLUMN `player_old_rating` BIGINT DEFAULT 0,
    ADD COLUMN `player_new_rating` BIGINT DEFAULT 0;

CREATE TABLE maimai2_user_friend_season_ranking (
    id          BIGINT auto_increment PRIMARY KEY,
    season_id   INTEGER,
    point       INTEGER,
    `rank`        INTEGER,
    reward_get  BOOLEAN,
    user_name   VARCHAR (255),
    record_date VARCHAR (255),
    user_id     BIGINT,
    constraint FKcTHZpS3jrefY5NMf
        foreign key (user_id) references maimai2_user_detail (id)
);
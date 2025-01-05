CREATE TABLE chusan_user_misc
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    user_id          BIGINT                NULL,
    recent_nb_select VARCHAR(1023)          NULL,
    recent_nb_music  VARCHAR(1023)          NULL,
    fav_music        VARCHAR(1023)          NULL,
    CONSTRAINT pk_chusanusermisc PRIMARY KEY (id)
);

ALTER TABLE chusan_user_misc
    ADD CONSTRAINT FK_CHUSANUSERMISC_ON_USER FOREIGN KEY (user_id) REFERENCES chusan_user_data (id);

ALTER TABLE chusan_user_misc
    ADD CONSTRAINT UNQ_CHUSANUSERMISC_ON_USER UNIQUE (user_id);

DROP TABLE chusan_matching_member

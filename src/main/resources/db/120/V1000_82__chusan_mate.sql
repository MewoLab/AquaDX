ALTER TABLE chusan_user_data
    ADD mate_id INT NOT NULL DEFAULT 0;

CREATE TABLE chusan_user_mate
(
    id                      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id                 BIGINT                NOT NULL,
    mate_id                 INTEGER               NOT NULL,
    play_count              INTEGER               NOT NULL,
    enter_garden_count      INTEGER               NOT NULL,
    friendship_level        INTEGER               NOT NULL,
    total_friendship_exp    INTEGER               NOT NULL,
    total_use_point         INTEGER               NOT NULL,

    is_valid                BOOLEAN               NOT NULL,

    CONSTRAINT fku_chusan_user_mate FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_user_mate UNIQUE (user_id, mate_id)
);

CREATE TABLE chusan_user_vote
(
    id                      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id                 BIGINT                NOT NULL,
    vote_id                 INTEGER               NOT NULL,
    point                   INTEGER               NOT NULL,
    total_point             INTEGER               NOT NULL,

    is_valid                BOOLEAN               NOT NULL,

    CONSTRAINT fku_chusan_user_vote FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_user_vote UNIQUE (user_id, vote_id)
);
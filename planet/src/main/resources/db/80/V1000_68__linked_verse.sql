ALTER TABLE aqua_game_options
    ADD chusan_lv_difficulty INT NOT NULL DEFAULT 5;
ALTER TABLE aqua_game_options
    ADD chusan_lv_unlock_all BOOLEAN DEFAULT FALSE;

CREATE TABLE chusan_game_linked_verse (
    id              BIGINT auto_increment PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    start_date      DATETIME NOT NULL,
    end_date        DATETIME NOT NULL,
    music_id        INTEGER NOT NULL
);

CREATE TABLE chusan_user_linked_verse
(
    id                  BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    progress            VARCHAR(64),
    user_id             BIGINT                NOT NULL,
    linked_verse_id     INTEGER               NOT NULL,
    clear_course_id     INTEGER               NOT NULL,
    clear_course_level  INTEGER               NOT NULL,
    clear_date          DATETIME              NOT NULL,
    clear_user_id1      BIGINT                NOT NULL,
    clear_user_id2      BIGINT                NOT NULL,
    clear_user_id3      BIGINT                NOT NULL,
    clear_user_name0    VARCHAR(8)            NOT NULL,
    clear_user_name1    VARCHAR(8)            NOT NULL,
    clear_user_name2    VARCHAR(8)            NOT NULL,
    clear_user_name3    VARCHAR(8)            NOT NULL,
    is_first_clear      BOOLEAN               NOT NULL DEFAULT FALSE,
    num_clear           INTEGER               NOT NULL,
    status_open         INTEGER               NOT NULL,
    status_unlock       INTEGER               NOT NULL,
    CONSTRAINT fku_chusan_user_linked_verse FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_user_linked_verse UNIQUE (user_id, linked_verse_id)
);

ALTER TABLE chusan_user_playlog
    MODIFY COLUMN played_user_id1 BIGINT;
ALTER TABLE chusan_user_playlog
    MODIFY COLUMN played_user_id2 BIGINT;
ALTER TABLE chusan_user_playlog
    MODIFY COLUMN played_user_id3 BIGINT;

INSERT INTO chusan_game_linked_verse (id, name, start_date, end_date, music_id)
VALUES
    (10001, 'Linked GATE ORIGIN', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2838),
    (10002, 'Linked GATE AIR', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2846),
    (10003, 'Linked GATE STAR', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2858),
    (10004, 'Linked GATE AMAZON', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2869),
    (10005, 'Linked GATE CRYSTAL', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2880),
    (10006, 'Linked GATE PARADISE', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2891),
    (10007, 'Linked GATE NEW', '2019-01-01 00:00:00.000000', '2029-01-01 00:00:00.000000',2919);
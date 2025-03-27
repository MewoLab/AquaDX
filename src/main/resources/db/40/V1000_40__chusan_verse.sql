ALTER TABLE chusan_user_data ADD COLUMN trophy_id_sub1 INT NOT NULL DEFAULT 0;
ALTER TABLE chusan_user_data ADD COLUMN trophy_id_sub2 INT NOT NULL DEFAULT 0;

CREATE TABLE chusan_user_challenge
(
    id                  BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id             BIGINT                NOT NULL,
    unlock_challenge_id INT                   NOT NULL,
    status              INT                   NOT NULL,
    clear_course_id     INT                   NOT NULL,
    condition_type      INT                   NOT NULL,
    score               INT                   NOT NULL,
    life                INT                   NOT NULL,
    clear_date          VARCHAR(20)           NULL,
    CONSTRAINT fku_chusan_user_challenge FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_user_challenge UNIQUE (user_id, unlock_challenge_id)
);

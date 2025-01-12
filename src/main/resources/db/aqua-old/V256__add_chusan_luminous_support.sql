CREATE TABLE chusan_user_cmission
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NOT NULL ,
    mission_id INT                   NULL,
    point      INT                   NULL,
    CONSTRAINT pk_chusan_user_cmission PRIMARY KEY (id),
    CONSTRAINT fku_chusan_user_cmission FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chusan_user_cmission_progress
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NULL,
    mission_id INT                   NULL,
    `order`    INT                   NOT NULL,
    stage      INT                   NULL,
    progress   INT                   NULL,
    CONSTRAINT pk_chusan_user_cmission_progress PRIMARY KEY (id),
    CONSTRAINT fku_chusan_user_cmission_progress FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unque_chusan_user_cmission_progress UNIQUE (user_id, mission_id, `order`)
);
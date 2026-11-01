ALTER TABLE maimai2_user_detail
    ADD COLUMN last_map_bonus_date VARCHAR(64) NOT NULL DEFAULT '';

CREATE TABLE maimai2_user_pass
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user_id      BIGINT                NOT NULL,
    pass_type_id INT                   NOT NULL,
    pass_pack_id INT                   NOT NULL,
    pass_chara_id INT                   NOT NULL,
    map_id       INT                   NOT NULL DEFAULT 0,
    start_date   VARCHAR(64)           NOT NULL DEFAULT '',
    end_date     VARCHAR(64)           NOT NULL DEFAULT '',
    CONSTRAINT pk_maimai2_user_pass PRIMARY KEY (id),
    CONSTRAINT uc_maimai2_user_pass_type UNIQUE (user_id, pass_type_id),
    CONSTRAINT fk_maimai2_user_pass_user FOREIGN KEY (user_id)
        REFERENCES maimai2_user_detail (id) ON DELETE CASCADE
);

CREATE TABLE maimai2_user_ticket_limit_date
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                NOT NULL,
    item_id        INT                   NOT NULL,
    limit_date     VARCHAR(64)           NOT NULL DEFAULT '',
    last_used_date VARCHAR(64)           NOT NULL DEFAULT '',
    CONSTRAINT pk_maimai2_user_ticket_limit_date PRIMARY KEY (id),
    CONSTRAINT uc_maimai2_user_ticket_limit_date_item UNIQUE (user_id, item_id),
    CONSTRAINT fk_maimai2_user_ticket_limit_date_user FOREIGN KEY (user_id)
        REFERENCES maimai2_user_detail (id) ON DELETE CASCADE
);

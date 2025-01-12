CREATE TABLE aqua_net_user
(
    au_id            BIGINT AUTO_INCREMENT NOT NULL,
    username         VARCHAR(32)        NOT NULL,
    email            VARCHAR(255)       NOT NULL,
    email_confirmed  BOOLEAN            NOT NULL,
    pw_hash          VARCHAR(255)       NOT NULL,
    display_name     VARCHAR(32)        NULL,
    country          VARCHAR(3)         NULL,
    last_login       BIGINT             NOT NULL,
    reg_time         BIGINT             NOT NULL,
    profile_location VARCHAR(255)       NULL,
    profile_bio      VARCHAR(255)       NULL,
    ghost_card       BIGINT             NOT NULL,
    CONSTRAINT pk_aqua_net_user PRIMARY KEY (au_id)
);

ALTER TABLE sega_card
    ADD net_user_id BIGINT NULL;

ALTER TABLE sega_card
    ADD is_ghost BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE aqua_net_user
    ADD CONSTRAINT uc_aqua_net_user_email UNIQUE (email);

ALTER TABLE aqua_net_user
    ADD CONSTRAINT uc_aqua_net_user_username UNIQUE (username);

ALTER TABLE sega_card
    ADD CONSTRAINT FK_SEGA_CARD_ON_NET_USER FOREIGN KEY (net_user_id) REFERENCES aqua_net_user (au_id);

ALTER TABLE aqua_net_user
    ADD CONSTRAINT uc_aqua_net_user_canonical_card UNIQUE (ghost_card);

ALTER TABLE aqua_net_user
    ADD CONSTRAINT FK_AQUA_NET_USER_ON_CANONICAL_CARD FOREIGN KEY (ghost_card) REFERENCES sega_card (id);

CREATE TABLE aqua_net_email_confirmation
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    token        VARCHAR(255)          NOT NULL,
    created_at   datetime              NOT NULL,
    au_id        BIGINT                NULL,
    CONSTRAINT pk_email_confirmation PRIMARY KEY (id)
);

ALTER TABLE aqua_net_email_confirmation
    ADD CONSTRAINT FK_EMAIL_CONFIRMATION_ON_AQUA_USER FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id);
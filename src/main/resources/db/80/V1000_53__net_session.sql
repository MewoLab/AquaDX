CREATE TABLE aqua_net_session
(
    token        VARCHAR(36)          NOT NULL,
    expiry       datetime              NOT NULL,
    au_id        BIGINT                NULL,
    CONSTRAINT pk_session PRIMARY KEY (token)
);

ALTER TABLE aqua_net_session
    ADD CONSTRAINT FK_SESSION FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id);

CREATE TABLE aqua_net_email_reset_password
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    token        VARCHAR(255)          NOT NULL,
    created_at   datetime              NOT NULL,
    au_id        BIGINT                NULL,
    CONSTRAINT pk_email_reset_password PRIMARY KEY (id)
);

ALTER TABLE aqua_net_email_reset_password
    ADD CONSTRAINT FK_EMAIL_RESET_PASSWORD_ON_AQUA_USER FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id);
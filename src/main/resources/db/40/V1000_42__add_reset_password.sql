CREATE TABLE aqua_net_email_password_reset
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    token        VARCHAR(255)          NOT NULL,
    created_at   datetime              NOT NULL,
    au_id        BIGINT                NULL,
    CONSTRAINT pk_email_password_reset PRIMARY KEY (id)
);
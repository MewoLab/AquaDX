CREATE TABLE aqua_net_session
(
    token        VARCHAR(36)          NOT NULL,
    expiry       datetime              NOT NULL,
    au_id        BIGINT                NULL,
    CONSTRAINT pk_session PRIMARY KEY (token)
);
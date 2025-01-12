CREATE TABLE allnet_keychip_sessions
(
    token    VARCHAR(32) NOT NULL,
    au_id    BIGINT      NULL,
    last_use BIGINT      NOT NULL,
    CONSTRAINT pk_allnet_keychip_sessions PRIMARY KEY (token)
);

ALTER TABLE aqua_net_user
    ADD keychip VARCHAR(32) NULL;

ALTER TABLE aqua_net_user
    ADD CONSTRAINT uc_aqua_net_user_keychip UNIQUE (keychip);

# Optimization on session cleanup
CREATE INDEX idx_last_use ON allnet_keychip_sessions (last_use);

ALTER TABLE allnet_keychip_sessions
    ADD CONSTRAINT FK_ALLNET_KEYCHIP_SESSIONS_ON_AU FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id);
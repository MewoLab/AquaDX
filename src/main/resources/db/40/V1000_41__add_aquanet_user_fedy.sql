CREATE TABLE aqua_net_user_fedy
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    au_id        BIGINT                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_fedy_on_aqua_net_user FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unq_fedy_on_aqua_net_user UNIQUE (au_id)
);

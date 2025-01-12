CREATE TABLE aqua_game_options
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    unlock_music        BIT(1)                NOT NULL,
    unlock_chara        BIT(1)                NOT NULL,
    unlock_collectables BIT(1)                NOT NULL,
    unlock_tickets      BIT(1)                NOT NULL,
    CONSTRAINT pk_aquagameoptions PRIMARY KEY (id)
);

ALTER TABLE aqua_net_user
    ADD game_options BIGINT NULL;

ALTER TABLE aqua_net_user
    ADD CONSTRAINT uc_aquanetuser_gameoptions UNIQUE (game_options);

ALTER TABLE aqua_net_user
    ADD CONSTRAINT FK_AQUANETUSER_ON_GAMEOPTIONS FOREIGN KEY (game_options) REFERENCES aqua_game_options (id);
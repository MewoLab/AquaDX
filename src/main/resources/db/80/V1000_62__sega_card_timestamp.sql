CREATE TABLE sega_card_timestamp
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime(3)           NOT NULL,
    updated_at   datetime(3)           NOT NULL,
    game         VARCHAR(255)          NOT NULL,
    card_id      BIGINT                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_sega_card_timestamp_on_sega_card FOREIGN KEY (card_id) REFERENCES sega_card (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unq_sega_card_timestamp_on_game_card UNIQUE (game, card_id)
);

ALTER TABLE maimai2_game_event ADD COLUMN disable_area VARCHAR(20) DEFAULT '';
ALTER TABLE maimai2_user_playlog ADD COLUMN ext_bool2 BIT(1) DEFAULT 0;

CREATE TABLE maimai2_user_kaleidx
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    user_id               BIGINT                NULL,
    gate_id               INT                   NOT NULL,
    is_gate_found         BIT(1)                NOT NULL,
    is_key_found          BIT(1)                NOT NULL,
    is_clear              BIT(1)                NOT NULL,
    total_rest_life       INT                   NOT NULL,
    total_achievement     INT                   NOT NULL,
    total_deluxscore      INT                   NOT NULL,
    best_achievement      INT                   NOT NULL,
    best_deluxscore       INT                   NOT NULL,
    best_achievement_date datetime              NULL,
    best_deluxscore_date  datetime              NULL,
    play_count            INT                   NOT NULL,
    clear_date            datetime              NULL,
    last_play_date        datetime              NULL,
    is_info_watched       BIT(1)                NOT NULL,
    CONSTRAINT fku_maimai2_user_kaleidx FOREIGN KEY (user_id) REFERENCES maimai2_user_detail (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_maimai2_user_kaleidx UNIQUE (user_id, gate_id)
);


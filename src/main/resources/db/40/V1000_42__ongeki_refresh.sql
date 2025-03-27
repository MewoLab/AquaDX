-- ongeki_user_option
ALTER TABLE ongeki_user_option ADD COLUMN effect_attack INT NOT NULL DEFAULT 0;

-- ongeki_user_data
ALTER TABLE ongeki_user_data ADD COLUMN new_highest_rating INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN new_player_rating INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN shizuku_count INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN sum_advanced_platinum_score_star INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN sum_basic_platinum_score_star INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN sum_expert_platinum_score_star INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN sum_lunatic_platinum_score_star INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN sum_master_platinum_score_star INT NOT NULL DEFAULT 0;
ALTER TABLE ongeki_user_data ADD COLUMN sum_platinum_score_star INT NOT NULL DEFAULT 0;

-- ongeki_user_music_detail
ALTER TABLE ongeki_user_music_detail ADD COLUMN platinum_score_star INT NOT NULL DEFAULT 0;

-- ongeki_user_memory_chapter
ALTER TABLE ongeki_user_memory_chapter ADD COLUMN is_ending_watched BIT NOT NULL;

-- ongeki_user_event_map
CREATE TABLE ongeki_user_event_map
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT                NULL,
    event_id        INT                   NOT NULL,
    map_id          INT                   NOT NULL,
    map_data        VARCHAR(255)          NULL,
    total_point     INT                   NOT NULL,
    total_use_point INT                   NOT NULL
);

ALTER TABLE ongeki_user_event_map
    ADD CONSTRAINT FKU_ONGEKI_USER_EVENT_MAP FOREIGN KEY (user_id) REFERENCES ongeki_user_data (id);

-- ongeki_user_skin
CREATE TABLE ongeki_user_skin
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT                NULL,
    is_valid BIT                   NOT NULL,
    deck_id  INT                   NOT NULL,
    card_id1 INT                   NOT NULL,
    card_id2 INT                   NOT NULL,
    card_id3 INT                   NOT NULL
);

ALTER TABLE ongeki_user_skin
    ADD CONSTRAINT FKU_ONGEKI_USER_SKIN FOREIGN KEY (user_id) REFERENCES ongeki_user_data (id);
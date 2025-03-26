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

ALTER TABLE diva_player_profile MODIFY COLUMN pd_id BIGINT NOT NULL;
ALTER TABLE diva_player_profile MODIFY COLUMN rival_pd_id BIGINT NOT NULL;
ALTER TABLE diva_player_screen_shot MODIFY COLUMN pd_id BIGINT NOT NULL;
ALTER TABLE diva_player_screen_shot MODIFY COLUMN pv_id BIGINT NOT NULL;

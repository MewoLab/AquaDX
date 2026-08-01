-- Diva's user foreign keys were removed in V1000.7 and never recreated.
-- Remove any rows that can no longer be associated with a profile before restoring them.
DELETE child FROM diva_game_session child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_play_log child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_contest child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_customize child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_inventory child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_module child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_pv_customize child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_pv_record child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;
DELETE child FROM diva_player_screen_shot child
    LEFT JOIN diva_player_profile profile ON child.pd_id = profile.id
    WHERE profile.id IS NULL;

ALTER TABLE diva_game_session
    ADD CONSTRAINT fku_diva_game_session_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_play_log
    ADD CONSTRAINT fku_diva_play_log_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_contest
    ADD CONSTRAINT fku_diva_player_contest_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_customize
    ADD CONSTRAINT fku_diva_player_customize_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_inventory
    ADD CONSTRAINT fku_diva_player_inventory_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_module
    ADD CONSTRAINT fku_diva_player_module_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_pv_customize
    ADD CONSTRAINT fku_diva_player_pv_customize_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_pv_record
    ADD CONSTRAINT fku_diva_player_pv_record_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE diva_player_screen_shot
    ADD CONSTRAINT fku_diva_player_screen_shot_profile
        FOREIGN KEY (pd_id) REFERENCES diva_player_profile (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

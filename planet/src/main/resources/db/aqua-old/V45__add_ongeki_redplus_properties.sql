ALTER TABLE `ongeki_user_option`
    ADD COLUMN `platinum_break_disp` int DEFAULT 1,
    ADD COLUMN `judge_critical_break` int DEFAULT 0,
    MODIFY COLUMN `judge_adjustment` int NULL;

ALTER TABLE `ongeki_user_login_bonus`
    ADD COLUMN `last_update_date` varchar(255) DEFAULT "2000-01-01 05:00:00.0";

ALTER TABLE `ongeki_user_data`
    ADD COLUMN `medal_count` int DEFAULT 0,
    ADD COLUMN `cm_event_watched_date` varchar(255) DEFAULT "2000-01-01 05:00:00.0";

ALTER TABLE `ongeki_user_character`
    ADD COLUMN `costume_id` int DEFAULT 0,
    ADD COLUMN `attachment_id` int DEFAULT 0;

ALTER TABLE `ongeki_user_chapter`
    ADD COLUMN `last_play_music_level` int DEFAULT 0;

ALTER TABLE `ongeki_user_boss`
    ADD COLUMN `event_id` int DEFAULT 0;
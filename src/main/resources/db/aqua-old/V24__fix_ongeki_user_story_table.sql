ALTER TABLE `ongeki_user_story`
    ADD COLUMN `jewel_count` int(11) DEFAULT 0,
    ADD COLUMN `last_play_music_id` int(11) DEFAULT 0,
    ADD COLUMN `last_play_music_category` int(11) DEFAULT 0,
    ADD COLUMN `last_play_music_level` int(11) DEFAULT 0;

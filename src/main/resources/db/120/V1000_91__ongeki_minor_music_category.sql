ALTER TABLE ongeki_user_chapter
    ADD COLUMN last_play_music_minor_category VARCHAR(255) NOT NULL DEFAULT '';

ALTER TABLE ongeki_user_story
    ADD COLUMN last_play_music_minor_category VARCHAR(255) NOT NULL DEFAULT '';

-- ongeki_user_data
ALTER TABLE ongeki_user_data ADD COLUMN is_dialog_watched_suggest_memory BOOLEAN;
ALTER TABLE ongeki_user_data ADD COLUMN character_voice_no INTEGER;
UPDATE ongeki_user_data SET is_dialog_watched_suggest_memory=false;
UPDATE ongeki_user_data SET character_voice_no=0;

-- ongeki_user_playlog
ALTER TABLE ongeki_user_playlog ADD COLUMN platinum_score INTEGER;
UPDATE ongeki_user_playlog SET platinum_score=0;

-- ongeki_user_memory_chapter
CREATE TABLE ongeki_user_memory_chapter
(
    id                       BIGINT auto_increment PRIMARY KEY,
    chapter_id               INTEGER NOT NULL,
    is_clear                 BOOLEAN NOT NULL,
    is_story_watched         BOOLEAN NOT NULL,
    is_dialog_watched        BOOLEAN NOT NULL,
    is_boss_watched          BOOLEAN NOT NULL,
    jewel_count              INTEGER NOT NULL,
    last_play_music_category INTEGER NOT NULL,
    last_play_music_id       INTEGER NOT NULL,
    last_play_music_level    INTEGER NOT NULL,
    gauge_id                 INTEGER NOT NULL,
    gauge_num                INTEGER NOT NULL,
    user_id                  BIGINT,
    constraint FKkbNRqobZgn7cDysE
        foreign key (user_id) references ongeki_user_data (id)
);

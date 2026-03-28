-- ongeki_user_music_detail
ALTER TABLE ongeki_user_music_detail ADD COLUMN platinum_score_max INTEGER;
UPDATE ongeki_user_music_detail SET platinum_score_max=0;

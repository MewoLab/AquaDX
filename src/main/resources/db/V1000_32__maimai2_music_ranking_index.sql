CREATE INDEX idx_play_date_music_user
ON maimai2_user_playlog (user_play_date, music_id, user_id);

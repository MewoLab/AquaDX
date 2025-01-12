-- maimai2_user_detail
ALTER TABLE maimai2_user_detail ADD COLUMN last_pair_login_date VARCHAR(255);
ALTER TABLE maimai2_user_detail ADD COLUMN last_trial_play_date VARCHAR(255);
ALTER TABLE maimai2_user_detail ADD COLUMN ban_state INTEGER;

UPDATE maimai2_user_detail SET ban_state=0;
UPDATE maimai2_user_detail SET last_pair_login_date="2019-01-01 09:00:00.0";
UPDATE maimai2_user_detail SET last_trial_play_date="2019-01-01 09:00:00.0";

-- maimai2_user_playlog
ALTER TABLE maimai2_user_playlog ADD COLUMN trial_play_achievement INTEGER;
UPDATE maimai2_user_playlog SET trial_play_achievement=0;
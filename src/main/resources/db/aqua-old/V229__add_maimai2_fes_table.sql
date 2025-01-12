-- maimai2_user_option
ALTER TABLE maimai2_user_option ADD COLUMN tap_se INTEGER;
UPDATE maimai2_user_option SET tap_se=0;

-- maimai2_user_music_detail
ALTER TABLE maimai2_user_music_detail ADD COLUMN ext_num1 INTEGER;
UPDATE maimai2_user_music_detail SET ext_num1=0;

-- maimai2_user_extend
ALTER TABLE maimai2_user_extend ADD COLUMN play_status_setting INTEGER;
UPDATE maimai2_user_extend SET play_status_setting=0;
-- maimai2_user_playlog
ALTER TABLE maimai2_user_playlog ADD COLUMN ext_bool1 BOOLEAN;
ALTER TABLE maimai2_user_playlog ADD COLUMN ext_num4 INTEGER;
UPDATE maimai2_user_playlog SET ext_num4=0;

-- maimai2_user_detail
ALTER TABLE maimai2_user_detail ADD COLUMN current_play_count INTEGER;
ALTER TABLE maimai2_user_detail ADD COLUMN rename_credit INTEGER;
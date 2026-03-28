-- maimai2_user_playlog
-- Set ext_bool1 as NOT NULL and give it a default value (e.g., FALSE)
UPDATE maimai2_user_playlog SET ext_bool1 = FALSE WHERE ext_bool1 IS NULL;
ALTER TABLE maimai2_user_playlog MODIFY COLUMN ext_bool1 BOOLEAN NOT NULL DEFAULT FALSE;

-- Set ext_num4 as NOT NULL (assuming it already has a default value of 0)
UPDATE maimai2_user_playlog SET ext_num4 = 0 WHERE ext_num4 IS NULL;
ALTER TABLE maimai2_user_playlog MODIFY COLUMN ext_num4 INTEGER NOT NULL DEFAULT 0;

-- maimai2_user_detail
-- Add default value for current_play_count and set it as NOT NULL
UPDATE maimai2_user_detail SET current_play_count = 0 WHERE current_play_count IS NULL;
ALTER TABLE maimai2_user_detail MODIFY COLUMN current_play_count INTEGER NOT NULL DEFAULT 0;

-- Add default value for rename_credit and set it as NOT NULL
UPDATE maimai2_user_detail SET rename_credit = 0 WHERE rename_credit IS NULL;
ALTER TABLE maimai2_user_detail MODIFY COLUMN rename_credit INTEGER NOT NULL DEFAULT 0;

ALTER TABLE maimai2_user_option ADD COLUMN out_frame_type INTEGER;
ALTER TABLE maimai2_user_option ADD COLUMN break_slide_volume INTEGER;
ALTER TABLE maimai2_user_option ADD COLUMN touch_volume INTEGER;

ALTER TABLE maimai2_user_extend ADD COLUMN select_result_score_view_type INTEGER;

ALTER TABLE maimai2_user_detail ADD COLUMN map_stock INTEGER;

UPDATE maimai2_user_option SET out_frame_type=0;
UPDATE maimai2_user_option SET break_slide_volume=0;
UPDATE maimai2_user_option SET touch_volume=0;
UPDATE maimai2_user_extend SET select_result_score_view_type=0;
UPDATE maimai2_user_detail SET map_stock=0;

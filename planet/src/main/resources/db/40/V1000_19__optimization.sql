-- Create views to unify some aspects of the user data and playlog tables

DROP VIEW IF EXISTS maimai2_user_playlog_view;
CREATE VIEW maimai2_user_playlog_view AS
SELECT
    *,
    IF((tap_miss + tap_good + tap_great = 0) AND
       (hold_miss + hold_good + hold_great = 0) AND
       (slide_miss + slide_good + slide_great = 0) AND
       (touch_miss + touch_good + touch_great = 0) AND
       (break_miss + break_good + break_great = 0), 1, 0) AS is_all_perfect,
    IF(max_combo = total_combo, 1, 0) AS is_full_combo
FROM maimai2_user_playlog;

DROP VIEW IF EXISTS maimai2_user_data_view;
CREATE VIEW maimai2_user_data_view AS
SELECT * FROM maimai2_user_detail;

DROP VIEW IF EXISTS chusan_user_playlog_view;
CREATE VIEW chusan_user_playlog_view AS
SELECT
    *,
    score as achievement,
    is_all_justice as is_all_perfect
FROM chusan_user_playlog;

DROP VIEW IF EXISTS chusan_user_data_view;
CREATE VIEW chusan_user_data_view AS
SELECT *, card_id as aime_card_id FROM chusan_user_data;

DROP VIEW IF EXISTS ongeki_user_playlog_view;
CREATE VIEW ongeki_user_playlog_view AS
SELECT
    *,
    tech_score as achievement,
    is_all_break as is_all_perfect
FROM ongeki_user_playlog;

DROP VIEW IF EXISTS ongeki_user_data_view;
CREATE VIEW ongeki_user_data_view AS
SELECT * FROM ongeki_user_data;

DROP VIEW IF EXISTS wacca_user_playlog_view;
CREATE VIEW wacca_user_playlog_view AS
SELECT * FROM wacca_user_playlog;

DROP VIEW IF EXISTS wacca_user_data_view;
CREATE VIEW wacca_user_data_view AS
SELECT * FROM wacca_user;
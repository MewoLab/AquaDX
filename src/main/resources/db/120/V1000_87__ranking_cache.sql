-- Ranking previously aggregated every playlog every 20 minutes. Keep exact,
-- disposable aggregates in game-specific cache tables with real foreign keys.
CREATE TABLE maimai2_user_ranking_cache
(
    user_id            BIGINT NOT NULL,
    achievement_sum    BIGINT NOT NULL DEFAULT 0,
    play_count         BIGINT NOT NULL DEFAULT 0,
    full_combo_count   BIGINT NOT NULL DEFAULT 0,
    all_perfect_count  BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_maimai2_user_ranking_cache PRIMARY KEY (user_id),
    CONSTRAINT fk_maimai2_user_ranking_cache_user FOREIGN KEY (user_id)
        REFERENCES maimai2_user_detail (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chusan_user_ranking_cache
(
    user_id            BIGINT NOT NULL,
    achievement_sum    BIGINT NOT NULL DEFAULT 0,
    play_count         BIGINT NOT NULL DEFAULT 0,
    full_combo_count   BIGINT NOT NULL DEFAULT 0,
    all_perfect_count  BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_chusan_user_ranking_cache PRIMARY KEY (user_id),
    CONSTRAINT fk_chusan_user_ranking_cache_user FOREIGN KEY (user_id)
        REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ongeki_user_ranking_cache
(
    user_id            BIGINT NOT NULL,
    achievement_sum    BIGINT NOT NULL DEFAULT 0,
    play_count         BIGINT NOT NULL DEFAULT 0,
    full_combo_count   BIGINT NOT NULL DEFAULT 0,
    all_perfect_count  BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_ongeki_user_ranking_cache PRIMARY KEY (user_id),
    CONSTRAINT fk_ongeki_user_ranking_cache_user FOREIGN KEY (user_id)
        REFERENCES ongeki_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wacca_user_ranking_cache
(
    user_id            BIGINT NOT NULL,
    achievement_sum    BIGINT NOT NULL DEFAULT 0,
    play_count         BIGINT NOT NULL DEFAULT 0,
    full_combo_count   BIGINT NOT NULL DEFAULT 0,
    all_perfect_count  BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_wacca_user_ranking_cache PRIMARY KEY (user_id),
    CONSTRAINT fk_wacca_user_ranking_cache_user FOREIGN KEY (user_id)
        REFERENCES wacca_user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO maimai2_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
SELECT
    user_id,
    SUM(achievement),
    COUNT(*),
    SUM(IF(max_combo = total_combo, 1, 0)),
    SUM(IF(
        (tap_miss + tap_good + tap_great = 0) AND
        (hold_miss + hold_good + hold_great = 0) AND
        (slide_miss + slide_good + slide_great = 0) AND
        (touch_miss + touch_good + touch_great = 0) AND
        (break_miss + break_good + break_great = 0),
        1, 0
    ))
FROM maimai2_user_playlog
GROUP BY user_id;

INSERT INTO chusan_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
SELECT user_id, SUM(score), COUNT(*), SUM(is_full_combo), SUM(is_all_justice)
FROM chusan_user_playlog
GROUP BY user_id;

INSERT INTO ongeki_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
SELECT user_id, SUM(tech_score), COUNT(*), SUM(is_full_combo), SUM(is_all_break)
FROM ongeki_user_playlog
GROUP BY user_id;

INSERT INTO wacca_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
SELECT user_id, SUM(achievement), COUNT(*), SUM(is_full_combo), SUM(is_all_perfect)
FROM wacca_user_playlog
GROUP BY user_id;

CREATE TRIGGER trg_maimai2_rank_insert
AFTER INSERT ON maimai2_user_playlog
FOR EACH ROW
INSERT INTO maimai2_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES
    (NEW.user_id, NEW.achievement, 1,
     IF(NEW.max_combo = NEW.total_combo, 1, 0),
     IF(
        (NEW.tap_miss + NEW.tap_good + NEW.tap_great = 0) AND
        (NEW.hold_miss + NEW.hold_good + NEW.hold_great = 0) AND
        (NEW.slide_miss + NEW.slide_good + NEW.slide_great = 0) AND
        (NEW.touch_miss + NEW.touch_good + NEW.touch_great = 0) AND
        (NEW.break_miss + NEW.break_good + NEW.break_great = 0),
        1, 0
     ))
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.achievement,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + IF(NEW.max_combo = NEW.total_combo, 1, 0),
    all_perfect_count = all_perfect_count + IF(
        (NEW.tap_miss + NEW.tap_good + NEW.tap_great = 0) AND
        (NEW.hold_miss + NEW.hold_good + NEW.hold_great = 0) AND
        (NEW.slide_miss + NEW.slide_good + NEW.slide_great = 0) AND
        (NEW.touch_miss + NEW.touch_good + NEW.touch_great = 0) AND
        (NEW.break_miss + NEW.break_good + NEW.break_great = 0),
        1, 0
    );

CREATE TRIGGER trg_maimai2_rank_delete
AFTER DELETE ON maimai2_user_playlog
FOR EACH ROW
UPDATE maimai2_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.achievement,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - IF(OLD.max_combo = OLD.total_combo, 1, 0),
    all_perfect_count = all_perfect_count - IF(
        (OLD.tap_miss + OLD.tap_good + OLD.tap_great = 0) AND
        (OLD.hold_miss + OLD.hold_good + OLD.hold_great = 0) AND
        (OLD.slide_miss + OLD.slide_good + OLD.slide_great = 0) AND
        (OLD.touch_miss + OLD.touch_good + OLD.touch_great = 0) AND
        (OLD.break_miss + OLD.break_good + OLD.break_great = 0),
        1, 0
    )
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_maimai2_rank_update_remove
AFTER UPDATE ON maimai2_user_playlog
FOR EACH ROW
UPDATE maimai2_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.achievement,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - IF(OLD.max_combo = OLD.total_combo, 1, 0),
    all_perfect_count = all_perfect_count - IF(
        (OLD.tap_miss + OLD.tap_good + OLD.tap_great = 0) AND
        (OLD.hold_miss + OLD.hold_good + OLD.hold_great = 0) AND
        (OLD.slide_miss + OLD.slide_good + OLD.slide_great = 0) AND
        (OLD.touch_miss + OLD.touch_good + OLD.touch_great = 0) AND
        (OLD.break_miss + OLD.break_good + OLD.break_great = 0),
        1, 0
    )
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_maimai2_rank_update_add
AFTER UPDATE ON maimai2_user_playlog
FOR EACH ROW FOLLOWS trg_maimai2_rank_update_remove
INSERT INTO maimai2_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES
    (NEW.user_id, NEW.achievement, 1,
     IF(NEW.max_combo = NEW.total_combo, 1, 0),
     IF(
        (NEW.tap_miss + NEW.tap_good + NEW.tap_great = 0) AND
        (NEW.hold_miss + NEW.hold_good + NEW.hold_great = 0) AND
        (NEW.slide_miss + NEW.slide_good + NEW.slide_great = 0) AND
        (NEW.touch_miss + NEW.touch_good + NEW.touch_great = 0) AND
        (NEW.break_miss + NEW.break_good + NEW.break_great = 0),
        1, 0
     ))
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.achievement,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + IF(NEW.max_combo = NEW.total_combo, 1, 0),
    all_perfect_count = all_perfect_count + IF(
        (NEW.tap_miss + NEW.tap_good + NEW.tap_great = 0) AND
        (NEW.hold_miss + NEW.hold_good + NEW.hold_great = 0) AND
        (NEW.slide_miss + NEW.slide_good + NEW.slide_great = 0) AND
        (NEW.touch_miss + NEW.touch_good + NEW.touch_great = 0) AND
        (NEW.break_miss + NEW.break_good + NEW.break_great = 0),
        1, 0
    );

CREATE TRIGGER trg_chusan_rank_insert
AFTER INSERT ON chusan_user_playlog
FOR EACH ROW
INSERT INTO chusan_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES (NEW.user_id, NEW.score, 1, NEW.is_full_combo, NEW.is_all_justice)
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.score,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + NEW.is_full_combo,
    all_perfect_count = all_perfect_count + NEW.is_all_justice;

CREATE TRIGGER trg_chusan_rank_delete
AFTER DELETE ON chusan_user_playlog
FOR EACH ROW
UPDATE chusan_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.score,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - OLD.is_full_combo,
    all_perfect_count = all_perfect_count - OLD.is_all_justice
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_chusan_rank_update_remove
AFTER UPDATE ON chusan_user_playlog
FOR EACH ROW
UPDATE chusan_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.score,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - OLD.is_full_combo,
    all_perfect_count = all_perfect_count - OLD.is_all_justice
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_chusan_rank_update_add
AFTER UPDATE ON chusan_user_playlog
FOR EACH ROW FOLLOWS trg_chusan_rank_update_remove
INSERT INTO chusan_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES (NEW.user_id, NEW.score, 1, NEW.is_full_combo, NEW.is_all_justice)
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.score,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + NEW.is_full_combo,
    all_perfect_count = all_perfect_count + NEW.is_all_justice;

CREATE TRIGGER trg_ongeki_rank_insert
AFTER INSERT ON ongeki_user_playlog
FOR EACH ROW
INSERT INTO ongeki_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES (NEW.user_id, NEW.tech_score, 1, NEW.is_full_combo, NEW.is_all_break)
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.tech_score,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + NEW.is_full_combo,
    all_perfect_count = all_perfect_count + NEW.is_all_break;

CREATE TRIGGER trg_ongeki_rank_delete
AFTER DELETE ON ongeki_user_playlog
FOR EACH ROW
UPDATE ongeki_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.tech_score,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - OLD.is_full_combo,
    all_perfect_count = all_perfect_count - OLD.is_all_break
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_ongeki_rank_update_remove
AFTER UPDATE ON ongeki_user_playlog
FOR EACH ROW
UPDATE ongeki_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.tech_score,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - OLD.is_full_combo,
    all_perfect_count = all_perfect_count - OLD.is_all_break
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_ongeki_rank_update_add
AFTER UPDATE ON ongeki_user_playlog
FOR EACH ROW FOLLOWS trg_ongeki_rank_update_remove
INSERT INTO ongeki_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES (NEW.user_id, NEW.tech_score, 1, NEW.is_full_combo, NEW.is_all_break)
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.tech_score,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + NEW.is_full_combo,
    all_perfect_count = all_perfect_count + NEW.is_all_break;

CREATE TRIGGER trg_wacca_rank_insert
AFTER INSERT ON wacca_user_playlog
FOR EACH ROW
INSERT INTO wacca_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES (NEW.user_id, NEW.achievement, 1, NEW.is_full_combo, NEW.is_all_perfect)
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.achievement,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + NEW.is_full_combo,
    all_perfect_count = all_perfect_count + NEW.is_all_perfect;

CREATE TRIGGER trg_wacca_rank_delete
AFTER DELETE ON wacca_user_playlog
FOR EACH ROW
UPDATE wacca_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.achievement,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - OLD.is_full_combo,
    all_perfect_count = all_perfect_count - OLD.is_all_perfect
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_wacca_rank_update_remove
AFTER UPDATE ON wacca_user_playlog
FOR EACH ROW
UPDATE wacca_user_ranking_cache SET
    achievement_sum = achievement_sum - OLD.achievement,
    play_count = play_count - 1,
    full_combo_count = full_combo_count - OLD.is_full_combo,
    all_perfect_count = all_perfect_count - OLD.is_all_perfect
WHERE user_id = OLD.user_id;

CREATE TRIGGER trg_wacca_rank_update_add
AFTER UPDATE ON wacca_user_playlog
FOR EACH ROW FOLLOWS trg_wacca_rank_update_remove
INSERT INTO wacca_user_ranking_cache
    (user_id, achievement_sum, play_count, full_combo_count, all_perfect_count)
VALUES (NEW.user_id, NEW.achievement, 1, NEW.is_full_combo, NEW.is_all_perfect)
ON DUPLICATE KEY UPDATE
    achievement_sum = achievement_sum + NEW.achievement,
    play_count = play_count + 1,
    full_combo_count = full_combo_count + NEW.is_full_combo,
    all_perfect_count = all_perfect_count + NEW.is_all_perfect;

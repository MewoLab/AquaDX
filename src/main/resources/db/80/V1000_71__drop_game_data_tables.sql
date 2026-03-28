-- Drop game data tables that have been migrated to JSON
SET FOREIGN_KEY_CHECKS = 0;

-- Remove FK from surviving user tables
ALTER TABLE ongeki_user_gacha DROP FOREIGN KEY IF EXISTS FK_ONGEKI_USER_GACHA_ON_GACHA;

DROP TABLE IF EXISTS chusan_game_charge;
DROP TABLE IF EXISTS chusan_game_event;
DROP TABLE IF EXISTS chusan_game_gacha_card;
DROP TABLE IF EXISTS chusan_game_gacha;
DROP TABLE IF EXISTS chusan_game_login_bonus;
DROP TABLE IF EXISTS chusan_game_login_bonus_preset;
DROP TABLE IF EXISTS chusan_game_linked_verse;

DROP TABLE IF EXISTS maimai2_game_charge;
DROP TABLE IF EXISTS maimai2_game_event;
DROP TABLE IF EXISTS maimai2_game_selling_card;
DROP TABLE IF EXISTS maimai2_game_ticket;

DROP TABLE IF EXISTS ongeki_game_card;
DROP TABLE IF EXISTS ongeki_game_chara;
DROP TABLE IF EXISTS ongeki_game_event;
DROP TABLE IF EXISTS ongeki_game_gacha_card;
DROP TABLE IF EXISTS ongeki_game_gacha;
DROP TABLE IF EXISTS ongeki_game_music;
DROP TABLE IF EXISTS ongeki_game_point;
DROP TABLE IF EXISTS ongeki_game_present;
DROP TABLE IF EXISTS ongeki_game_reward;
DROP TABLE IF EXISTS ongeki_game_skill;

SET FOREIGN_KEY_CHECKS = 1;

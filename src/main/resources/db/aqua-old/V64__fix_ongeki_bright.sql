-- Ongeki user option: stealth_field, color_wall_bright
ALTER TABLE `ongeki_user_option`
    ADD COLUMN `color_wall_bright` int DEFAULT 0,
    ADD COLUMN `stealth_field` int DEFAULT 3;

-- Ongeki user data: best_battle_point, over_damage_battle_point, rival_score_category_setting, last_emoney_brand
ALTER TABLE `ongeki_user_data`
    ADD COLUMN `best_battle_point` int DEFAULT 0,
    ADD COLUMN `over_damage_battle_point` int DEFAULT 0,
    ADD COLUMN `rival_score_category_setting` int DEFAULT 0,
    ADD COLUMN `last_emoney_brand` int DEFAULT 0;
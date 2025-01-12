
ALTER TABLE `chuni_user_character`
    ADD UNIQUE KEY `UK_chuni_user_character` (`user_id`, `character_id`);

ALTER TABLE `chuni_user_charge`
    ADD UNIQUE KEY `UK_chuni_user_charge` (`user_id`, `charge_id`);

ALTER TABLE `chuni_user_course`
    ADD UNIQUE KEY `UK_chuni_user_course` (`user_id`, `course_id`);

ALTER TABLE `chuni_user_duel`
    ADD UNIQUE KEY `UK_chuni_user_duel` (`user_id`, `duel_id`);

ALTER TABLE `chuni_user_item`
    ADD UNIQUE KEY `UK_chuni_user_item` (`user_id`, `item_id`, `item_kind`);

ALTER TABLE `chuni_user_map`
    ADD UNIQUE KEY `UK_chuni_user_map` (`user_id`, `map_id`);

ALTER TABLE `chuni_user_music_detail`
    ADD UNIQUE KEY `UK_chuni_user_music_detail` (`user_id`, `music_id`, `level`);

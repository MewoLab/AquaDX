UPDATE `chuni_game_event` SET `enable` = b'0' WHERE `type` = 1 AND `enable` = b'1';
DELETE FROM `chuni_game_event` WHERE `id` = 19000;

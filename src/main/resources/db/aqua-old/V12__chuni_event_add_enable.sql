ALTER TABLE `chuni_game_event`
    ADD COLUMN `enable` bit(1) NOT NULL DEFAULT true;

UPDATE `chuni_game_event` SET `enable`=false WHERE `type` = 1;
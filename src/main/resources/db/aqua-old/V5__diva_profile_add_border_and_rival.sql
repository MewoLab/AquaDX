ALTER TABLE `diva_player_profile`
    ADD COLUMN `show_excellent_border` bit(1),
    ADD COLUMN `show_rival_border` bit(1),
    ADD COLUMN `rival_pd_id` int(11);

UPDATE `diva_player_profile` SET
    `show_excellent_border` = 1,
    `show_rival_border` = 1,
    `rival_pd_id` = -1;


ALTER TABLE `diva_player_profile`
    MODIFY COLUMN `show_excellent_border` bit(1) NOT NULL,
    MODIFY COLUMN `show_rival_border` bit(1) NOT NULL,
    MODIFY COLUMN `rival_pd_id` int(11) NOT NULL;

ALTER TABLE `diva_player_profile`
    CHANGE COLUMN `show_clear_border` `show_great_border` bit(1) NOT NULL;
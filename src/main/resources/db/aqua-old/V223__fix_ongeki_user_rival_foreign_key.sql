ALTER TABLE `ongeki_user_rival` DROP FOREIGN KEY `FK__ongeki_user_data_2`;
ALTER TABLE `ongeki_user_rival` ADD CONSTRAINT `FK__ongeki_user_data_2` FOREIGN KEY (rival_user_id) REFERENCES `sega_card` (`ext_id`) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE `ongeki_user_rival` RENAME COLUMN `rival_user_id` TO `rival_user_ext_id`;
ALTER TABLE `diva_contest`
    ADD COLUMN `pv_list` varchar(255) DEFAULT NULL;

UPDATE `diva_contest` SET `stage_limit`='0';

ALTER TABLE `diva_contest`
    ADD COLUMN `pv_diff_list` varchar(255) DEFAULT NULL;

ALTER TABLE `diva_contest`
    ADD COLUMN `bronze_contest_reward` varchar(255) DEFAULT NULL,
    ADD COLUMN `sliver_contest_reward` varchar(255) DEFAULT NULL,
    ADD COLUMN `gold_contest_reward` varchar(255) DEFAULT NULL,
    ADD COLUMN `contest_entry_reward` varchar(255) DEFAULT NULL;

CREATE TABLE `diva_player_inventory`
(
    `id`               bigint(20) NOT NULL,
    `type`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `value`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `pd_id`            bigint(20)                              DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

ALTER TABLE `diva_player_inventory`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK1bace6j9oebd80bqw7hfad3c` (`pd_id`, `type`, `value`);

ALTER TABLE `diva_player_inventory`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `diva_player_inventory`
    ADD CONSTRAINT `FK4328de6j9oebd899qw7hbxh6s` FOREIGN KEY (`pd_id`) REFERENCES `diva_player_profile` (`id`);
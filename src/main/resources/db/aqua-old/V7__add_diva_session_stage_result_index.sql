TRUNCATE TABLE `diva_game_session`;

ALTER TABLE `diva_game_session`
    ADD  `stage_result_index` int(11) DEFAULT NULL;
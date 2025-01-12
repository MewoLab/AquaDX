INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2101, '稲葉曇', 'なし', 2, 'ラグトレイン', 'v1 1.55.00', 'ラクトレイン');
INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2102, 'Ayase', 'なし', 2, '幽霊東京', 'v1 1.55.00', 'ユウレイトウキヨウ');
INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2103, 'DECO*27', '楽曲「ポジティブ・パレード」', 2, 'ポジティブ・パレード', 'v1 1.55.00', 'ホシテイフハレウト');

INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 2, 0, 2101);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 5, 0, 2101);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 8, 70, 2101);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 11, 70, 2101);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2101);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 2, 0, 2102);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 5, 0, 2102);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 8, 0, 2102);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 11, 60, 2102);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2102);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 2, 0, 2103);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 6, 0, 2103);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 8, 70, 2103);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 12, 20, 2103);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2103);

INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6550, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',1, b'0');
INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6551, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',3, b'1');

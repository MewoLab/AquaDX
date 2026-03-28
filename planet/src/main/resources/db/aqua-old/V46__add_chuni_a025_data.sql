INSERT INTO chuni_game_skill (id, name, category) VALUES (105109, '電子の息吹', 'Support');

INSERT INTO chuni_game_character (id, name, release_tag, works_name, illustrator_name, first_skill_id, skills, add_images) VALUES (11970, '星街すいせい(ホロライブ)／自分勝手Dazzling', 'v1 1.50.00', 'CHUNITHM PARADISE', '成海七海', '105109', '5:0,10:0,15:0,25:0,50:0', '-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:');
INSERT INTO chuni_game_character (id, name, release_tag, works_name, illustrator_name, first_skill_id, skills, add_images) VALUES (11980, 'memex／Utopithm', 'v1 1.50.00', 'CHUNITHM PARADISE', 'U10', '105109', '5:0,10:0,15:0,25:0,50:0', '-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:');
INSERT INTO chuni_game_character (id, name, release_tag, works_name, illustrator_name, first_skill_id, skills, add_images) VALUES (11990, 'ぼっちぼろまる／妖怪ですから', 'v1 1.50.00', 'CHUNITHM PARADISE', '作之介', '105109', '5:0,10:0,15:0,25:0,50:0', '-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:');
INSERT INTO chuni_game_character (id, name, release_tag, works_name, illustrator_name, first_skill_id, skills, add_images) VALUES (12000, 'KMNZ／Glory Days', 'v1 1.50.00', 'CHUNITHM PARADISE', 'しゅがお', '105109', '5:0,10:0,15:0,25:0,50:0', '-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:');
INSERT INTO chuni_game_character (id, name, release_tag, works_name, illustrator_name, first_skill_id, skills, add_images) VALUES (12010, 'Marpril／Spiral Fortune', 'v1 1.50.00', 'CHUNITHM PARADISE', 'みくら', '105109', '5:0,10:0,15:0,25:0,50:0', '-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:,-1:');

INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2070, '星街すいせい(ホロライブ)', 'なし', 5, '自分勝手Dazzling', 'v1 1.55.00', 'シフンカツテDAZZLING');
INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2071, 'memex', 'なし', 5, 'Utopithm', 'v1 1.55.00', 'UTOPITHM');
INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2072, 'ぼっちぼろまる', 'なし', 5, '妖怪ですから', 'v1 1.55.00', 'ヨウカイテスカラ');
INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2073, 'KMNZ', 'なし', 5, 'Glory Days', 'v1 1.55.00', 'GLORYDAYS');
INSERT INTO chuni_music (music_id, artist_name, copyright, genre, name, release_version, sort_name) VALUES (2074, 'Marpril', 'なし', 5, 'Spiral Fortune', 'v1 1.55.00', 'SPIRALFORTUNE');

INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 3, 0, 2070);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 5, 0, 2070);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 9, 70, 2070);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 12, 10, 2070);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2070);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 3, 0, 2071);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 6, 0, 2071);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 10, 0, 2071);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 12, 70, 2071);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2071);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 3, 0, 2072);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 6, 0, 2072);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 9, 0, 2072);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 12, 50, 2072);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2072);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 3, 0, 2073);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 5, 0, 2073);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 10, 0, 2073);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 12, 10, 2073);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2073);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (0, 1, 3, 0, 2074);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (1, 1, 6, 0, 2074);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (2, 1, 9, 0, 2074);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (3, 1, 12, 90, 2074);
INSERT INTO chuni_music_level (diff, enable, level, level_decimal, music_id) VALUES (4, 0, 0, 0, 2074);

INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6350, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',1, b'0');
INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6351, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',2, b'1');
INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6352, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',1, b'0');
INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6353, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',2, b'1');
INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6354, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',7, b'1');
INSERT INTO `chuni_game_event` (`id`, `end_date`, `start_date`, `type`, `enable`) VALUES (6355, '2029-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',4, b'1');

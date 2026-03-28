create table ongeki_game_card
(
    id bigint not null
        primary key,
    attribute varchar(255) null,
    card_number varchar(255) null,
    chara_id int not null,
    cho_kaika_skill_id int not null,
    gakunen varchar(255) null,
    level_param varchar(255) null,
    name varchar(255) null,
    nick_name varchar(255) null,
    rarity varchar(255) null,
    school varchar(255) null,
    skill_id int not null,
    version varchar(255) null
);

create table ongeki_game_chara
(
    id bigint not null
        primary key,
    cv varchar(255) null,
    model_id int not null,
    name varchar(255) null
);

create table ongeki_game_event
(
    id bigint not null
        primary key
);

create table ongeki_game_music
(
    id bigint not null
        primary key,
    artist_name varchar(255) null,
    boss_card_id int not null,
    boss_level int not null,
    genre varchar(255) null,
    level0 varchar(255) null,
    level1 varchar(255) null,
    level2 varchar(255) null,
    level3 varchar(255) null,
    level4 varchar(255) null,
    name varchar(255) null,
    sort_name varchar(255) null
);

create table ongeki_game_skill
(
    id bigint not null
        primary key,
    category varchar(255) null,
    info varchar(255) null,
    name varchar(255) null
);

INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (1, '【SR】あかニャン[ボス専用]', 'ボス専用', 'Fire', 1, '-', '-', 'SR', '55,227,242,257,272,287,0,0,0,287', 0, 0, '[O.N.G.E.K.I.]*.**-****', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (2, '【SR】あおニャン[ボス専用]', 'ボス専用', 'Aqua', 2, '-', '-', 'SR', '55,227,242,257,272,287,0,0,0,287', 0, 0, '[O.N.G.E.K.I.]*.**-****', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (3, '【SR】みどニャン[ボス専用]', 'ボス専用', 'Leaf', 3, '-', '-', 'SR', '55,227,242,257,272,287,0,0,0,287', 0, 0, '[O.N.G.E.K.I.]*.**-****', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100001, '【N】星咲 あかり', '', 'Fire', 1000, '奏坂学園', '高校2年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0001', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100002, '【R】星咲 あかり[シューター・ドレス]', 'シューター・ドレス', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100001, 100042, '[O.N.G.E.K.I.]1.00-0005', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100003, '【R】星咲 あかり[体操着]', '体操着', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0004', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100004, '【R】星咲 あかり[私服]', '私服', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.00-0003', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100005, '【SR】星咲 あかり[目指せ！　いっぱいのエール！]', '目指せ！　いっぱいのエール！', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100008, 100049, '[O.N.G.E.K.I.]1.00-0008', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100006, '【SR】星咲 あかり[ようこそ！奏坂へ！]', 'ようこそ！奏坂へ！', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100026, 100067, '[O.N.G.E.K.I.]1.00-0007', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100007, '【SR】星咲 あかり[ドジっ子トラブル]', 'ドジっ子トラブル', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100032, 100073, '[O.N.G.E.K.I.]1.00-0104', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100008, '【SSR】星咲 あかり[揺るぎない想い]', '揺るぎない想い', 'Fire', 1000, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100034, 100075, '[O.N.G.E.K.I.]1.00-0009', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100009, '【SSR】星咲 あかり[デイドリーム・フェアリーズ]', 'デイドリーム・フェアリーズ', 'Fire', 1000, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100038, 100079, '[O.N.G.E.K.I.]1.00-0010', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100010, '【N】藤沢 柚子', '', 'Leaf', 1001, '奏坂学園', '高校2年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0011', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100011, '【R】藤沢 柚子[シューター・ドレス]', 'シューター・ドレス', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.00-0015', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100012, '【R】藤沢 柚子[体操着]', '体操着', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0014', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100013, '【R】藤沢 柚子[私服]', '私服', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.00-0013', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100014, '【SR】藤沢 柚子[目指せ！　いっぱいのエール！]', '目指せ！　いっぱいのエール！', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100008, 100049, '[O.N.G.E.K.I.]1.00-0018', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100015, '【SR】藤沢 柚子[笑顔のお裾分け]', '笑顔のお裾分け', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100027, 100068, '[O.N.G.E.K.I.]1.00-0017', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100016, '【SR】藤沢 柚子[お着替えターイム！]', 'お着替えターイム！', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100032, 100073, '[O.N.G.E.K.I.]1.00-0105', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100017, '【SSR】藤沢 柚子[ハッピー☆マジック]', 'ハッピー☆マジック', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100034, 100075, '[O.N.G.E.K.I.]1.00-0019', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100018, '【SSR】藤沢 柚子[デイドリーム・フェアリーズ]', 'デイドリーム・フェアリーズ', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100039, 100080, '[O.N.G.E.K.I.]1.00-0020', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100019, '【N】三角 葵', '', 'Aqua', 1002, '奏坂学園', '高校2年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0021', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100020, '【R】三角 葵[シューター・ドレス]', 'シューター・ドレス', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100003, 100044, '[O.N.G.E.K.I.]1.00-0025', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100021, '【R】三角 葵[体操着]', '体操着', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0024', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100022, '【R】三角 葵[私服]', '私服', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.00-0023', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100023, '【SR】三角 葵[目指せ！　いっぱいのエール！]', '目指せ！　いっぱいのエール！', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100008, 100049, '[O.N.G.E.K.I.]1.00-0028', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100024, '【SR】三角 葵[ね、簡単でしょ？]', 'ね、簡単でしょ？', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100028, 100069, '[O.N.G.E.K.I.]1.00-0027', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100025, '【SR】三角 葵[かちこちストレッチ]', 'かちこちストレッチ', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100032, 100073, '[O.N.G.E.K.I.]1.00-0106', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100026, '【SSR】三角 葵[ワールズエンド・ブルー]', 'ワールズエンド・ブルー', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100034, 100075, '[O.N.G.E.K.I.]1.00-0029', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100027, '【SSR】三角 葵[デイドリーム・フェアリーズ]', 'デイドリーム・フェアリーズ', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100040, 100081, '[O.N.G.E.K.I.]1.00-0030', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100028, '【N】高瀬 梨緒', '', 'Aqua', 1003, '奏坂学園', '高校2年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0031', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100029, '【R】高瀬 梨緒[シューター・ドレス]', 'シューター・ドレス', 'Aqua', 1003, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0034', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100030, '【R】高瀬 梨緒[体操着]', '体操着', 'Aqua', 1003, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0033', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100031, '【SR】高瀬 梨緒[自称・超絶最強、登場！]', '自称・超絶最強、登場！', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100015, 100056, '[O.N.G.E.K.I.]1.00-0037', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100032, '【SR】高瀬 梨緒[カフェ de スクランブル]', 'カフェ de スクランブル', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100021, 100062, '[O.N.G.E.K.I.]1.00-0038', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100033, '【SR】高瀬 梨緒[素直になれないお年頃]', '素直になれないお年頃', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100029, 100070, '[O.N.G.E.K.I.]1.00-0036', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100034, '【SSR】高瀬 梨緒[ジェットストライクリッパー]', 'ジェットストライクリッパー', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100108, 100109, '[O.N.G.E.K.I.]1.00-0039', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100035, '【N】結城 莉玖', '', 'Fire', 1004, '奏坂学園', '高校1年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0040', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100036, '【R】結城 莉玖[シューター・ドレス]', 'シューター・ドレス', 'Fire', 1004, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0043', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100037, '【R】結城 莉玖[体操着]', '体操着', 'Fire', 1004, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0042', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100038, '【SR】結城 莉玖[嘘とがおー！とかたき討ち]', '嘘とがおー！とかたき討ち', 'Fire', 1004, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100015, 100056, '[O.N.G.E.K.I.]1.00-0046', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100039, '【SR】結城 莉玖[カフェ de スクランブル]', 'カフェ de スクランブル', 'Fire', 1004, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100019, 100060, '[O.N.G.E.K.I.]1.00-0047', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100040, '【SR】結城 莉玖[未来のロックスター]', '未来のロックスター', 'Fire', 1004, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100029, 100070, '[O.N.G.E.K.I.]1.00-0045', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100041, '【SSR】結城 莉玖[リトルギターモンスター]', 'リトルギターモンスター', 'Fire', 1004, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100104, 100105, '[O.N.G.E.K.I.]1.00-0048', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100042, '【N】藍原 椿', '', 'Leaf', 1005, '奏坂学園', '高校1年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0049', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100043, '【R】藍原 椿[シューター・ドレス]', 'シューター・ドレス', 'Leaf', 1005, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0052', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100044, '【R】藍原 椿[体操着]', '体操着', 'Leaf', 1005, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0051', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100045, '【SR】藍原 椿[嘘とがおー！とかたき討ち]', '嘘とがおー！とかたき討ち', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100015, 100056, '[O.N.G.E.K.I.]1.00-0055', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100046, '【SR】藍原 椿[カフェ de スクランブル]', 'カフェ de スクランブル', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100020, 100061, '[O.N.G.E.K.I.]1.00-0056', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100047, '【SR】藍原 椿[夕暮れノスタルジー]', '夕暮れノスタルジー', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100029, 100070, '[O.N.G.E.K.I.]1.00-0054', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100048, '【SSR】藍原 椿[サディスティック・スマイル]', 'サディスティック・スマイル', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100106, 100107, '[O.N.G.E.K.I.]1.00-0057', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100049, '【N】桜井 春菜', '', 'Fire', 1007, '奏坂学園', '高校2年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0058', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100050, '【R】桜井 春菜[シューター・ドレス]', 'シューター・ドレス', 'Fire', 1007, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0072', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100051, '【R】桜井 春菜[体操着]', '体操着', 'Fire', 1007, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100001, 100042, '[O.N.G.E.K.I.]1.00-0060', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100052, '【SR】桜井 春菜[ハルちゃん印のお弁当]', 'ハルちゃん印のお弁当', 'Fire', 1007, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100016, 100057, '[O.N.G.E.K.I.]1.00-0073', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100053, '【SR】桜井 春菜[これは味見だから…！]', 'これは味見だから…！', 'Fire', 1007, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100025, 100066, '[O.N.G.E.K.I.]1.00-0062', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100054, '【SSR】桜井 春菜[フラワリーブライド]', 'フラワリーブライド', 'Fire', 1007, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100036, 100077, '[O.N.G.E.K.I.]1.00-0074', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100055, '【N】早乙女 彩華', '', 'Aqua', 1006, '奏坂学園', '高校3年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0063', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100056, '【R】早乙女 彩華[シューター・ドレス]', 'シューター・ドレス', 'Aqua', 1006, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0075', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100057, '【R】早乙女 彩華[体操着]', '体操着', 'Aqua', 1006, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100003, 100044, '[O.N.G.E.K.I.]1.00-0065', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100058, '【SR】早乙女 彩華[ハルちゃん印のお弁当]', 'ハルちゃん印のお弁当', 'Aqua', 1006, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100018, 100059, '[O.N.G.E.K.I.]1.00-0076', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100059, '【SR】早乙女 彩華[しーっ！内緒だよ？]', 'しーっ！内緒だよ？', 'Aqua', 1006, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100025, 100066, '[O.N.G.E.K.I.]1.00-0067', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100060, '【SSR】早乙女 彩華[爆アゲ☆バニーガール]', '爆アゲ☆バニーガール', 'Aqua', 1006, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100036, 100077, '[O.N.G.E.K.I.]1.00-0077', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100061, '【N】九條 楓', '', 'Leaf', 1008, '奏坂学園', '高校3年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0068', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100062, '【R】九條 楓[シューター・ドレス]', 'シューター・ドレス', 'Leaf', 1008, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0078', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100063, '【R】九條 楓[体操着]', '体操着', 'Leaf', 1008, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.00-0070', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100064, '【SR】九條 楓[波乱！副会長の通告！]', '波乱！副会長の通告！', 'Leaf', 1008, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100017, 100058, '[O.N.G.E.K.I.]1.00-0080', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100065, '【SR】九條 楓[雨傘を差して]', '雨傘を差して', 'Leaf', 1008, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100025, 100066, '[O.N.G.E.K.I.]1.00-0079', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100066, '【SSR】九條 楓[紫電一閃]', '紫電一閃', 'Leaf', 1008, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100036, 100077, '[O.N.G.E.K.I.]1.00-0095', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100067, '【N】井之原 小星', '', 'Leaf', 1010, '奏坂学園', '高校1年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0081', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100068, '【R】井之原 小星[シューター・ドレス]', 'シューター・ドレス', 'Leaf', 1010, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0084', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100069, '【R】井之原 小星[体操着]', '体操着', 'Leaf', 1010, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100007, 100048, '[O.N.G.E.K.I.]1.00-0083', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100070, '【SR】井之原 小星[小星と咲姫が あらわれた！]', '小星と咲姫が あらわれた！', 'Leaf', 1010, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100023, 100064, '[O.N.G.E.K.I.]1.00-0087', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100071, '【SR】井之原 小星[ぐーたらゲーマー]', 'ぐーたらゲーマー', 'Leaf', 1010, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100031, 100072, '[O.N.G.E.K.I.]1.00-0086', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100072, '【SSR】井之原 小星[お任せオートプレイ]', 'お任せオートプレイ', 'Leaf', 1010, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100036, 100077, '[O.N.G.E.K.I.]1.00-0107', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100073, '【N】柏木 咲姫', '', 'Aqua', 1009, '奏坂学園', '高校3年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0088', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100074, '【R】柏木 咲姫[シューター・ドレス]', 'シューター・ドレス', 'Aqua', 1009, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100007, 100048, '[O.N.G.E.K.I.]1.00-0091', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100075, '【R】柏木 咲姫[体操着]', '体操着', 'Aqua', 1009, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0090', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100076, '【SR】柏木 咲姫[小星と咲姫が あらわれた！]', '小星と咲姫が あらわれた！', 'Aqua', 1009, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100024, 100065, '[O.N.G.E.K.I.]1.00-0094', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100077, '【SR】柏木 咲姫[お姫様は隙だらけ]', 'お姫様は隙だらけ', 'Aqua', 1009, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100018, 100059, '[O.N.G.E.K.I.]1.00-0093', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100078, '【SSR】柏木 咲姫[清く、凛々しく、美しく]', '清く、凛々しく、美しく', 'Aqua', 1009, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100036, 100077, '[O.N.G.E.K.I.]1.00-0096', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100079, '【N】逢坂 茜', '', 'Fire', 1011, '奏坂学園', '高校3年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0097', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100080, '【R】逢坂 茜[シューター・ドレス]', 'シューター・ドレス', 'Fire', 1011, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100006, 100047, '[O.N.G.E.K.I.]1.00-0100', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100081, '【R】逢坂 茜[体操着]', '体操着', 'Fire', 1011, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100136, 100137, '[O.N.G.E.K.I.]1.00-0099', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100082, '【SR】逢坂 茜[世界征服のお誘い]', '世界征服のお誘い', 'Fire', 1011, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100022, 100063, '[O.N.G.E.K.I.]1.00-0103', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100083, '【SR】逢坂 茜[はちゃめちゃ生徒会長]', 'はちゃめちゃ生徒会長', 'Fire', 1011, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100030, 100071, '[O.N.G.E.K.I.]1.00-0102', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100084, '【SSR】逢坂 茜[ショット＆デストロイ]', 'ショット＆デストロイ', 'Fire', 1011, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100036, 100077, '[O.N.G.E.K.I.]1.00-0108', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100085, '【N】珠洲島 有栖', '', 'Aqua', 1012, '奏坂学園', '高校1年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100000, 100041, '[O.N.G.E.K.I.]1.00-0109', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100086, '【R】珠洲島 有栖[シューター・ドレス]', 'シューター・ドレス', 'Aqua', 1012, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100008, 100049, '[O.N.G.E.K.I.]1.00-0112', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100087, '【R】珠洲島 有栖[体操着]', '体操着', 'Aqua', 1012, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100014, 100055, '[O.N.G.E.K.I.]1.00-0111', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100088, '【SR】珠洲島 有栖[黒幕はぬいぐるみ？]', '黒幕はぬいぐるみ？', 'Aqua', 1012, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100025, 100066, '[O.N.G.E.K.I.]1.00-0115', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100089, '【SR】珠洲島 有栖[優雅なティータイム]', '優雅なティータイム', 'Aqua', 1012, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100033, 100074, '[O.N.G.E.K.I.]1.00-0114', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100090, '【SSR】珠洲島 有栖[もふもふシューター]', 'もふもふシューター', 'Aqua', 1012, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100037, 100078, '[O.N.G.E.K.I.]1.05-0018', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100091, '【R】霧雨 魔理沙[東方Project]', '東方Project', 'Leaf', 2001, '東方Project', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0019', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100092, '【SR】霧雨 魔理沙[普通の魔法使い]', '普通の魔法使い', 'Leaf', 2001, '東方Project', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0020', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100093, '【SSR】霧雨 魔理沙[恋符「マスタースパーク」]', '恋符「マスタースパーク」', 'Leaf', 2001, '東方Project', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100144, 100145, '[O.N.G.E.K.I.]1.00-E-0046', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100094, '【R】博麗 霊夢[東方Project]', '東方Project', 'Fire', 2000, '東方Project', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100140, 100141, '[O.N.G.E.K.I.]1.00-E-0017', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100095, '【SR】博麗 霊夢[楽園の素敵な巫女]', '楽園の素敵な巫女', 'Fire', 2000, '東方Project', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0018', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100096, '【SSR】博麗 霊夢[霊符「夢想封印」]', '霊符「夢想封印」', 'Fire', 2000, '東方Project', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100138, 100139, '[O.N.G.E.K.I.]1.00-E-0045', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100097, '【R】十六夜 咲夜[東方紅魔郷]', '東方紅魔郷', 'Aqua', 2002, '東方紅魔郷', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0026', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100098, '【SR】十六夜 咲夜[完全で瀟洒な従者]', '完全で瀟洒な従者', 'Aqua', 2002, '東方紅魔郷', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100028, 100069, '[O.N.G.E.K.I.]1.00-E-0027', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100100, '【R】レミリア・スカーレット[東方紅魔郷]', '東方紅魔郷', 'Fire', 2003, '東方紅魔郷', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0028', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100101, '【SR】レミリア・スカーレット[永遠に紅い幼き月]', '永遠に紅い幼き月', 'Fire', 2003, '東方紅魔郷', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0029', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100102, '【SSR】レミリア・スカーレット[神槍「スピア・ザ・グングニル」]', '神槍「スピア・ザ・グングニル」', 'Fire', 2003, '東方紅魔郷', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100033, 100074, '[O.N.G.E.K.I.]1.00-E-0032', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100103, '【R】フランドール・スカーレット[東方紅魔郷]', '東方紅魔郷', 'Fire', 2004, '東方紅魔郷', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0030', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100104, '【SR】フランドール・スカーレット[悪魔の妹]', '悪魔の妹', 'Fire', 2004, '東方紅魔郷', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0031', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100105, '【SSR】フランドール・スカーレット[禁忌「レーヴァテイン」]', '禁忌「レーヴァテイン」', 'Fire', 2004, '東方紅魔郷', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100036, 100077, '[O.N.G.E.K.I.]1.00-E-0033', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100106, '【R】紅 美鈴[東方紅魔郷]', '東方紅魔郷', 'Leaf', 2005, '東方紅魔郷', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0022', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100107, '【SR】紅 美鈴[華人小娘]', '華人小娘', 'Leaf', 2005, '東方紅魔郷', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100016, 100057, '[O.N.G.E.K.I.]1.00-E-0023', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100109, '【R】パチュリー・ノーレッジ[東方紅魔郷]', '東方紅魔郷', 'Leaf', 2006, '東方紅魔郷', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0024', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100110, '【SR】パチュリー・ノーレッジ[動かない大図書館]', '動かない大図書館', 'Leaf', 2006, '東方紅魔郷', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.00-E-0025', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100112, '【R】チルノ[東方Project]', '東方Project', 'Aqua', 2007, '東方Project', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0062', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100113, '【SR】チルノ[湖上の氷精]', '湖上の氷精', 'Aqua', 2007, '東方Project', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105026, 105027, '[O.N.G.E.K.I.]1.05-E-0063', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100117, '【R】めぐみん[アークウィザード]', 'アークウィザード', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0034', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100118, '【SR】めぐみん[水辺にて]', '水辺にて', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100082, 100083, '[O.N.G.E.K.I.]1.00-E-0035', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100119, '【SR】めぐみん[我が名はめぐみん]', '我が名はめぐみん', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0040', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100120, '【SSR】めぐみん[紅魔族の休息]', '紅魔族の休息', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100084, 100085, '[O.N.G.E.K.I.]1.00-E-0044', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100121, '【R】アクア[アークプリースト]', 'アークプリースト', 'Aqua', 11002, 'この素晴らしい世界に祝福を！2', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-E-0036', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100122, '【SR】アクア[セイクリッド飲みくらべ]', 'セイクリッド飲みくらべ', 'Aqua', 11002, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100142, 100143, '[O.N.G.E.K.I.]1.00-E-0037', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100123, '【SR】アクア[めぐみんのいたずら]', 'めぐみんのいたずら', 'Aqua', 11002, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100028, 100069, '[O.N.G.E.K.I.]1.00-E-0041', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100124, '【R】ダクネス[クルセイダー]', 'クルセイダー', 'Leaf', 11003, 'この素晴らしい世界に祝福を！2', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.00-E-0038', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100125, '【SR】ダクネス[爆風に吹かれて]', '爆風に吹かれて', 'Leaf', 11003, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100017, 100058, '[O.N.G.E.K.I.]1.00-E-0039', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100126, '【SR】ダクネス[露天風呂]', '露天風呂', 'Leaf', 11003, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.00-E-0042', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100127, '【R】戸山香澄[ステージ]', 'ステージ', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0001', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100128, '【SR】戸山香澄[約束のキャンディ]', '約束のキャンディ', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0003', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100129, '【SR】戸山香澄[最高のステージに！]', '最高のステージに！', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100015, 100056, '[O.N.G.E.K.I.]1.00-E-0047', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100130, '【SR】戸山香澄[気合の円陣]', '気合の円陣', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100026, 100067, '[O.N.G.E.K.I.]1.00-E-0048', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100131, '【SSR】戸山香澄[ガールズバンドパーティ！]', 'ガールズバンドパーティ！', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100033, 100074, '[O.N.G.E.K.I.]1.00-E-0016', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100132, '【R】美竹蘭[ステージ]', 'ステージ', 'Fire', 18002, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0004', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100133, '【SR】美竹蘭[断ち切った迷い]', '断ち切った迷い', 'Fire', 18002, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100016, 100057, '[O.N.G.E.K.I.]1.00-E-0006', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100134, '【SR】美竹蘭[等身大の夜空]', '等身大の夜空', 'Fire', 18002, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100019, 100060, '[O.N.G.E.K.I.]1.00-E-0049', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100135, '【SR】美竹蘭[夕焼けの先]', '夕焼けの先', 'Fire', 18002, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0050', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100136, '【R】丸山彩[ステージ]', 'ステージ', 'Leaf', 18003, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0007', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100137, '【SR】丸山彩[必殺アイドルポーズ☆]', '必殺アイドルポーズ☆', 'Leaf', 18003, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0009', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100138, '【SR】丸山彩[私達のポスター]', '私達のポスター', 'Leaf', 18003, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.00-E-0051', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100139, '【SR】丸山彩[イベント、来てください！]', 'イベント、来てください！', 'Leaf', 18003, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0052', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100140, '【R】湊友希那[ステージ]', 'ステージ', 'Aqua', 18004, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0010', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100141, '【SR】湊友希那[歌姫の覚悟]', '歌姫の覚悟', 'Aqua', 18004, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0012', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100142, '【SR】湊友希那[秋晴れ、その先に]', '秋晴れ、その先に', 'Aqua', 18004, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100018, 100059, '[O.N.G.E.K.I.]1.00-E-0053', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100143, '【SR】湊友希那[見守る目線]', '見守る目線', 'Aqua', 18004, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100028, 100069, '[O.N.G.E.K.I.]1.00-E-0054', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100144, '【R】弦巻こころ[ステージ]', 'ステージ', 'Leaf', 18005, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0013', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100145, '【SR】弦巻こころ[無敵のヒーロー]', '無敵のヒーロー', 'Leaf', 18005, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.00-E-0015', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100146, '【SR】弦巻こころ[見習い魔女]', '見習い魔女', 'Leaf', 18005, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100017, 100058, '[O.N.G.E.K.I.]1.00-E-0055', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100147, '【SR】弦巻こころ[みんなが花マル1等賞！]', 'みんなが花マル1等賞！', 'Leaf', 18005, 'バンドリ！ ガールズバンドパーティ！', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0056', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100158, '【R】初音ミク[バーチャル・シンガー]', 'バーチャル・シンガー', 'Aqua', 5000, 'バーチャル・シンガー', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0001', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100159, '【SR】初音ミク[電子の歌姫]', '電子の歌姫', 'Aqua', 5000, 'バーチャル・シンガー', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100021, 100062, '[O.N.G.E.K.I.]1.05-E-0002', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100160, '【SR】初音ミク[V4X]', 'V4X', 'Aqua', 5000, 'バーチャル・シンガー', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0023', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100161, '【R】巡音ルカ[バーチャル・シンガー]', 'バーチャル・シンガー', 'Fire', 5001, 'バーチャル・シンガー', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0004', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100162, '【SR】巡音ルカ[クール＆ハスキー]', 'クール＆ハスキー', 'Fire', 5001, 'バーチャル・シンガー', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100019, 100060, '[O.N.G.E.K.I.]1.05-E-0022', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100163, '【SR】巡音ルカ[V4X]', 'V4X', 'Fire', 5001, 'バーチャル・シンガー', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0051', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100164, '【R】鏡音リン[バーチャル・シンガー]', 'バーチャル・シンガー', 'Leaf', 5002, 'バーチャル・シンガー', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0003', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100165, '【SR】鏡音リン[パワフル＆チャーミング]', 'パワフル＆チャーミング', 'Leaf', 5002, 'バーチャル・シンガー', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100020, 100061, '[O.N.G.E.K.I.]1.05-E-0021', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100166, '【SR】鏡音リン[V4X]', 'V4X', 'Leaf', 5002, 'バーチャル・シンガー', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0038', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100167, '【SSR】めぐみん[爆裂魔法]', '爆裂魔法', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100033, 100074, '[O.N.G.E.K.I.]1.00-E-0043', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100168, '【R】戸山香澄[カラフルポッピン！]', 'カラフルポッピン！', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.00-E-0002', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100169, '【R】美竹蘭[グロウアップロック]', 'グロウアップロック', 'Fire', 18002, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100140, 100141, '[O.N.G.E.K.I.]1.00-E-0005', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100170, '【R】丸山彩[煌めくステージへ]', '煌めくステージへ', 'Leaf', 18003, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.00-E-0008', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100171, '【R】湊友希那[重なり合う青薔薇]', '重なり合う青薔薇', 'Aqua', 18004, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.00-E-0011', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100172, '【R】弦巻こころ[笑顔のマジック]', '笑顔のマジック', 'Leaf', 18005, 'バンドリ！ ガールズバンドパーティ！', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.00-E-0014', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100173, '【R】星咲 あかり[表情：笑顔]', '表情：笑顔', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0002', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100174, '【R】藤沢 柚子[表情：むすぅ]', '表情：むすぅ', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0012', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100175, '【R】三角 葵[表情：恥じらい]', '表情：恥じらい', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-0022', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100176, '【R】高瀬 梨緒[表情：むかっ]', '表情：むかっ', 'Aqua', 1003, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100003, 100044, '[O.N.G.E.K.I.]1.00-0032', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100177, '【R】結城 莉玖[表情：ドヤ]', '表情：ドヤ', 'Fire', 1004, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100001, 100042, '[O.N.G.E.K.I.]1.00-0041', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100178, '【R】藍原 椿[表情：びっくり]', '表情：びっくり', 'Leaf', 1005, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.00-0050', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100179, '【R】早乙女 彩華[表情：ウィンク]', '表情：ウィンク', 'Aqua', 1006, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0064', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100180, '【R】桜井 春菜[表情：笑顔]', '表情：笑顔', 'Fire', 1007, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0059', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100181, '【R】九條 楓[表情：剣幕]', '表情：剣幕', 'Leaf', 1008, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-0069', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100182, '【R】柏木 咲姫[表情：恥じらい]', '表情：恥じらい', 'Aqua', 1009, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100009, 100050, '[O.N.G.E.K.I.]1.00-0089', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100183, '【R】井之原 小星[表情：キラキラ]', '表情：キラキラ', 'Leaf', 1010, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100010, 100051, '[O.N.G.E.K.I.]1.00-0082', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100184, '【R】逢坂 茜[表情：にやり]', '表情：にやり', 'Fire', 1011, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100009, 100050, '[O.N.G.E.K.I.]1.00-0098', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100185, '【R】珠洲島 有栖[表情：照れ]', '表情：照れ', 'Aqua', 1012, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100011, 100052, '[O.N.G.E.K.I.]1.00-0110', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100186, '【SSR】戸山香澄[クインティプル☆すまいる]', 'クインティプル☆すまいる', 'Fire', 18001, 'バンドリ！ ガールズバンドパーティ！', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100138, 100139, '[O.N.G.E.K.I.]1.00-E-0021', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100187, '【SSR】めぐみん[エクスプロージョン]', 'エクスプロージョン', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 105022, 105023, '[O.N.G.E.K.I.]1.05-E-0055', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100188, '【SSR】アクア[花鳥風月]', '花鳥風月', 'Aqua', 11002, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100144, 100145, '[O.N.G.E.K.I.]1.05-E-0056', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100189, '【SSR】ダクネス[クルセイダーシールド]', 'クルセイダーシールド', 'Leaf', 11003, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100144, 100145, '[O.N.G.E.K.I.]1.05-E-0057', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100190, '【R】日向 美海[青の世界]', '青の世界', 'Aqua', 20001, 'アンジュ・ヴィエルジュ', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100003, 100044, '[O.N.G.E.K.I.]1.00-E-0057', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100191, '【SR】日向 美海[謹賀新年]', '謹賀新年', 'Aqua', 20001, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100086, 100087, '[O.N.G.E.K.I.]1.00-E-0058', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100192, '【SR】日向 美海[無限の可能性]', '無限の可能性', 'Aqua', 20001, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0063', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100193, '【SSR】日向 美海[メリークリスマス！]', 'メリークリスマス！', 'Aqua', 20001, 'アンジュ・ヴィエルジュ', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100033, 100074, '[O.N.G.E.K.I.]1.00-E-0066', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100194, '【SSR】日向 美海[常夏の可能性]', '常夏の可能性', 'Aqua', 20001, 'アンジュ・ヴィエルジュ', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100088, 100089, '[O.N.G.E.K.I.]1.00-E-0067', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100195, '【R】彩城 天音[青の世界]', '青の世界', 'Aqua', 20002, 'アンジュ・ヴィエルジュ', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.00-E-0059', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100196, '【SR】彩城 天音[謹賀新年]', '謹賀新年', 'Aqua', 20002, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100018, 100059, '[O.N.G.E.K.I.]1.00-E-0060', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100197, '【SR】彩城 天音[みんなの希望]', 'みんなの希望', 'Aqua', 20002, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100028, 100069, '[O.N.G.E.K.I.]1.00-E-0064', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100198, '【R】東条 遥[青の世界]', '青の世界', 'Aqua', 20003, 'アンジュ・ヴィエルジュ', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.00-E-0061', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100199, '【SR】東条 遥[謹賀新年]', '謹賀新年', 'Aqua', 20003, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100028, 100069, '[O.N.G.E.K.I.]1.00-E-0062', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100200, '【SR】東条 遥[希望の光]', '希望の光', 'Aqua', 20003, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.00-E-0065', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100201, '【SR】星咲 あかり[ONGEKI Vocal Collection 01]', 'ONGEKI Vocal Collection 01', 'Fire', 1000, 'プロモーション', '高校2年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 100092, 100093, '[O.N.G.E.K.I.]1.00-P-0001', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100202, '【SR】藤沢 柚子[ONGEKI Vocal Collection 01]', 'ONGEKI Vocal Collection 01', 'Leaf', 1001, 'プロモーション', '高校2年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 100094, 100095, '[O.N.G.E.K.I.]1.00-P-0002', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100203, '【SR】三角 葵[ONGEKI Vocal Collection 01]', 'ONGEKI Vocal Collection 01', 'Aqua', 1002, 'プロモーション', '高校2年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 100096, 100097, '[O.N.G.E.K.I.]1.00-P-0003', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100204, '【SR】高瀬 梨緒[ONGEKI Vocal Collection 02]', 'ONGEKI Vocal Collection 02', 'Aqua', 1003, 'プロモーション', '高校2年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 100102, 100103, '[O.N.G.E.K.I.]1.00-P-0004', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100205, '【SR】結城 莉玖[ONGEKI Vocal Collection 02]', 'ONGEKI Vocal Collection 02', 'Fire', 1004, 'プロモーション', '高校1年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 100100, 100101, '[O.N.G.E.K.I.]1.00-P-0005', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100206, '【SR】藍原 椿[ONGEKI Vocal Collection 02]', 'ONGEKI Vocal Collection 02', 'Leaf', 1005, 'プロモーション', '高校1年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 100098, 100099, '[O.N.G.E.K.I.]1.00-P-0006', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100208, '【N】柏木 美亜', '', 'Fire', 1013, '奏坂学園', '中学2年生', 'N', '50,197,212,227,242,257,287,317,347,347', 100008, 100049, '[O.N.G.E.K.I.]1.05-****', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100209, '【R】柏木 美亜[シューター・ドレス]', 'シューター・ドレス', 'Fire', 1013, '奏坂学園', '中学2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100008, 100049, '[O.N.G.E.K.I.]1.05-****', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100210, '【R】星咲 あかり[私服2]', '私服2', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105100, 105101, '[O.N.G.E.K.I.]1.05-0002', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100211, '【R】藤沢 柚子[私服2]', '私服2', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105102, 105103, '[O.N.G.E.K.I.]1.05-0006', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100212, '【R】三角 葵[私服2]', '私服2', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105104, 105105, '[O.N.G.E.K.I.]1.05-0009', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100213, '【R】高瀬 梨緒[私服]', '私服', 'Aqua', 1003, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105110, 105111, '[O.N.G.E.K.I.]1.05-0020', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100214, '【R】結城 莉玖[私服]', '私服', 'Fire', 1004, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105106, 105107, '[O.N.G.E.K.I.]1.05-0022', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100215, '【R】藍原 椿[私服]', '私服', 'Leaf', 1005, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105108, 105109, '[O.N.G.E.K.I.]1.05-0024', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100216, '【R】早乙女 彩華[私服]', '私服', 'Aqua', 1006, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105060, 105061, '[O.N.G.E.K.I.]1.05-0033', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100217, '【R】桜井 春菜[私服]', '私服', 'Fire', 1007, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105060, 105061, '[O.N.G.E.K.I.]1.05-0031', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100218, '【R】九條 楓[私服]', '私服', 'Leaf', 1008, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105114, 105115, '[O.N.G.E.K.I.]1.05-0057', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100219, '【R】柏木 咲姫[私服]', '私服', 'Aqua', 1009, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100003, 100044, '[O.N.G.E.K.I.]1.05-0047', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100220, '【R】井之原 小星[私服]', '私服', 'Leaf', 1010, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.05-0049', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100221, '【R】逢坂 茜[私服]', '私服', 'Fire', 1011, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105114, 105115, '[O.N.G.E.K.I.]1.05-0059', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100222, '【R】珠洲島 有栖[私服]', '私服', 'Aqua', 1012, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105114, 105115, '[O.N.G.E.K.I.]1.05-0061', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100223, '【R】星咲 あかり[私服 表情：げげっ]', '私服 表情：げげっ', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.05-0001', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100224, '【R】藤沢 柚子[私服 表情：涙目]', '私服 表情：涙目', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.05-0005', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100225, '【R】三角 葵[私服 表情：笑顔]', '私服 表情：笑顔', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.05-0028', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100226, '【R】高瀬 梨緒[私服 表情：ぐぬぬ]', '私服 表情：ぐぬぬ', 'Aqua', 1003, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105060, 105061, '[O.N.G.E.K.I.]1.05-0038', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100227, '【R】結城 莉玖[私服 表情：むー]', '私服 表情：むー', 'Fire', 1004, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105060, 105061, '[O.N.G.E.K.I.]1.05-0029', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100228, '【R】藍原 椿[私服 表情：目線を逸らす]', '私服 表情：目線を逸らす', 'Leaf', 1005, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105060, 105061, '[O.N.G.E.K.I.]1.05-0040', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100229, '【R】早乙女 彩華[私服 表情：照れ]', '私服 表情：照れ', 'Aqua', 1006, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105118, 105119, '[O.N.G.E.K.I.]1.05-0045', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100230, '【R】桜井 春菜[私服 表情：恥じらい]', '私服 表情：恥じらい', 'Fire', 1007, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105120, 105121, '[O.N.G.E.K.I.]1.05-0046', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100231, '【R】九條 楓[私服 表情：恥じらい]', '私服 表情：恥じらい', 'Leaf', 1008, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105068, 105069, '[O.N.G.E.K.I.]1.05-0066', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100232, '【R】柏木 咲姫[私服 表情：得意気]', '私服 表情：得意気', 'Aqua', 1009, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105124, 105125, '[O.N.G.E.K.I.]1.05-0055', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100233, '【R】井之原 小星[私服 表情：悲しい]', '私服 表情：悲しい', 'Leaf', 1010, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105122, 105123, '[O.N.G.E.K.I.]1.05-0054', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100234, '【R】逢坂 茜[私服 表情：わっはっは！]', '私服 表情：わっはっは！', 'Fire', 1011, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105068, 105069, '[O.N.G.E.K.I.]1.05-0077', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100235, '【R】珠洲島 有栖[私服 表情：微笑み]', '私服 表情：微笑み', 'Aqua', 1012, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 105068, 105069, '[O.N.G.E.K.I.]1.05-0068', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100236, '【SR】星咲 あかり[わくわくバイト・ミッション]', 'わくわくバイト・ミッション', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105078, 105079, '[O.N.G.E.K.I.]1.05-0004', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100237, '【SR】藤沢 柚子[わくわくバイト・ミッション]', 'わくわくバイト・ミッション', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105080, 105081, '[O.N.G.E.K.I.]1.05-0008', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100238, '【SR】三角 葵[わくわくバイト・ミッション]', 'わくわくバイト・ミッション', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105082, 105083, '[O.N.G.E.K.I.]1.05-0011', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100239, '【SR】高瀬 梨緒[ゲーセン☆チャンス！？]', 'ゲーセン☆チャンス！？', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105084, 105085, '[O.N.G.E.K.I.]1.05-0021', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100240, '【SR】結城 莉玖[ゲーセン☆チャンス！？]', 'ゲーセン☆チャンス！？', 'Fire', 1004, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105084, 105085, '[O.N.G.E.K.I.]1.05-0023', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100241, '【SR】藍原 椿[ゲーセン☆チャンス！？]', 'ゲーセン☆チャンス！？', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105084, 105085, '[O.N.G.E.K.I.]1.05-0025', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100242, '【SR】早乙女 彩華[妄想×動揺×大暴走]', '妄想×動揺×大暴走', 'Aqua', 1006, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105088, 105089, '[O.N.G.E.K.I.]1.05-0034', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100243, '【SR】桜井 春菜[妄想×動揺×大暴走]', '妄想×動揺×大暴走', 'Fire', 1007, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105086, 105087, '[O.N.G.E.K.I.]1.05-0032', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100244, '【SR】九條 楓[ドタバタ子守り大作戦]', 'ドタバタ子守り大作戦', 'Leaf', 1008, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105096, 105097, '[O.N.G.E.K.I.]1.05-0058', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100245, '【SR】柏木 咲姫[おかえりなさいませ！ ご主人さま♪]', 'おかえりなさいませ！ ご主人さま♪', 'Aqua', 1009, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105092, 105093, '[O.N.G.E.K.I.]1.05-0048', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100246, '【SR】井之原 小星[おかえりなさいませ！ ご主人さま♪]', 'おかえりなさいませ！ ご主人さま♪', 'Leaf', 1010, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105090, 105091, '[O.N.G.E.K.I.]1.05-0050', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100247, '【SR】逢坂 茜[ドタバタ子守り大作戦]', 'ドタバタ子守り大作戦', 'Fire', 1011, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105094, 105095, '[O.N.G.E.K.I.]1.05-0060', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100248, '【SR】珠洲島 有栖[ドタバタ子守り大作戦]', 'ドタバタ子守り大作戦', 'Aqua', 1012, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105098, 105099, '[O.N.G.E.K.I.]1.05-0062', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100249, '【SR】星咲 あかり[Jump!! Jump!! Jump!!]', 'Jump!! Jump!! Jump!!', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105040, 105041, '[O.N.G.E.K.I.]1.05-0003', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100250, '【SR】藤沢 柚子[Jump!! Jump!! Jump!!]', 'Jump!! Jump!! Jump!!', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105040, 105041, '[O.N.G.E.K.I.]1.05-0007', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100251, '【SR】三角 葵[Jump!! Jump!! Jump!!]', 'Jump!! Jump!! Jump!!', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105040, 105041, '[O.N.G.E.K.I.]1.05-0010', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100252, '【SR】高瀬 梨緒[Jump!! Jump!! Jump!!]', 'Jump!! Jump!! Jump!!', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105040, 105041, '[O.N.G.E.K.I.]1.05-0012', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100260, '【SR】逢坂 茜[Jump!! Jump!! Jump!!]', 'Jump!! Jump!! Jump!!', 'Fire', 1011, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105040, 105041, '[O.N.G.E.K.I.]1.05-0017', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100262, '【SR】星咲 あかり[どっちがどっち？]', 'どっちがどっち？', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105042, 105043, '[O.N.G.E.K.I.]1.05-0026', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100263, '【SR】藤沢 柚子[華より団子]', '華より団子', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100031, 100072, '[O.N.G.E.K.I.]1.05-0027', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100264, '【SR】三角 葵[モテモテ！？ ふれあい体験]', 'モテモテ！？ ふれあい体験', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105044, 105045, '[O.N.G.E.K.I.]1.05-0043', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100265, '【SR】高瀬 梨緒[巷で噂の看板娘]', '巷で噂の看板娘', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105050, 105051, '[O.N.G.E.K.I.]1.05-0039', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100266, '【SR】結城 莉玖[食べちゃうぞ♪]', '食べちゃうぞ♪', 'Fire', 1004, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105046, 105047, '[O.N.G.E.K.I.]1.05-0044', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100267, '【SR】藍原 椿[恥じらいバレンタイン]', '恥じらいバレンタイン', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105048, 105049, '[O.N.G.E.K.I.]1.05-0013', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100268, '【SR】早乙女 彩華[レッツ、チャレンジ！？]', 'レッツ、チャレンジ！？', 'Aqua', 1006, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105052, 105053, '[O.N.G.E.K.I.]1.05-0075', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100269, '【SR】桜井 春菜[びしょ濡れ雨宿り]', 'びしょ濡れ雨宿り', 'Fire', 1007, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100024, 100065, '[O.N.G.E.K.I.]1.05-0053', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100270, '【SR】九條 楓[行雲流水の如く]', '行雲流水の如く', 'Leaf', 1008, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100024, 100065, '[O.N.G.E.K.I.]1.05-0042', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100271, '【SR】柏木 咲姫[南国ホリデー]', '南国ホリデー', 'Aqua', 1009, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 100031, 100072, '[O.N.G.E.K.I.]1.05-0065', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100272, '【SR】井之原 小星[ぷかぷかコンティニュー]', 'ぷかぷかコンティニュー', 'Leaf', 1010, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105054, 105055, '[O.N.G.E.K.I.]1.05-0076', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100273, '【SR】逢坂 茜[いたずらスプラッシュ]', 'いたずらスプラッシュ', 'Fire', 1011, '奏坂学園', '高校3年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105056, 105057, '[O.N.G.E.K.I.]1.05-0067', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100274, '【SR】珠洲島 有栖[雨上がりの空に]', '雨上がりの空に', 'Aqua', 1012, '奏坂学園', '高校1年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105058, 105059, '[O.N.G.E.K.I.]1.05-0056', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100275, '【SSR】星咲 あかり[マーメイド・ランデブー]', 'マーメイド・ランデブー', 'Fire', 1000, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105028, 105029, '[O.N.G.E.K.I.]1.05-0073', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100276, '【SSR】藤沢 柚子[ハジけて☆サマー]', 'ハジけて☆サマー', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105028, 105029, '[O.N.G.E.K.I.]1.05-0063', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100277, '【SSR】三角 葵[with you]', 'with you', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105028, 105029, '[O.N.G.E.K.I.]1.05-0064', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100278, '【SSR】高瀬 梨緒[黄昏ロマンス]', '黄昏ロマンス', 'Aqua', 1003, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105030, 105031, '[O.N.G.E.K.I.]1.05-0074', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100280, '【SSR】藍原 椿[狐の嫁入り]', '狐の嫁入り', 'Leaf', 1005, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105030, 105031, '[O.N.G.E.K.I.]1.05-0052', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100282, '【SSR】桜井 春菜[スイート＆スイート]', 'スイート＆スイート', 'Fire', 1007, '奏坂学園', '高校2年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105032, 105033, '[O.N.G.E.K.I.]1.05-0014', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100284, '【SSR】柏木 咲姫[エキゾチック・トワイライト]', 'エキゾチック・トワイライト', 'Aqua', 1009, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105036, 105037, '[O.N.G.E.K.I.]1.05-0041', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100285, '【SSR】井之原 小星[おひな様は脱力系]', 'おひな様は脱力系', 'Leaf', 1010, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105034, 105035, '[O.N.G.E.K.I.]1.05-0030', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100286, '【SSR】逢坂 茜[それいけ！ 猛獣マスター]', 'それいけ！ 猛獣マスター', 'Fire', 1011, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 105038, 105039, '[O.N.G.E.K.I.]1.05-0051', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100288, '【SSR】九條 楓[デイドリーム・フェアリーズ]', 'デイドリーム・フェアリーズ', 'Leaf', 1008, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100039, 100080, '[O.N.G.E.K.I.]1.05-0015', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100289, '【SSR】逢坂 茜[デイドリーム・フェアリーズ]', 'デイドリーム・フェアリーズ', 'Fire', 1011, '奏坂学園', '高校3年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100038, 100079, '[O.N.G.E.K.I.]1.05-0016', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100290, '【SSR】珠洲島 有栖[デイドリーム・フェアリーズ]', 'デイドリーム・フェアリーズ', 'Aqua', 1012, '奏坂学園', '高校1年生', 'SSR', '60,257,280,295,307,317,0,0,0,317', 100040, 100081, '[O.N.G.E.K.I.]1.05-0019', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100291, '【SR】早乙女 彩華[ONGEKI Vocal Collection 03]', 'ONGEKI Vocal Collection 03', 'Aqua', 1006, 'プロモーション', '高校3年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105120, 105121, '[O.N.G.E.K.I.]1.05-P-0002', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100292, '【SR】桜井 春菜[ONGEKI Vocal Collection 03]', 'ONGEKI Vocal Collection 03', 'Fire', 1007, 'プロモーション', '高校2年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105118, 105119, '[O.N.G.E.K.I.]1.05-P-0003', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100293, '【SR】九條 楓[ONGEKI Vocal Collection 05]', 'ONGEKI Vocal Collection 05', 'Leaf', 1008, 'プロモーション', '高校3年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105128, 105129, '[O.N.G.E.K.I.]1.05-P-0006', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100294, '【SR】柏木 咲姫[ONGEKI Vocal Collection 04]', 'ONGEKI Vocal Collection 04', 'Aqua', 1009, 'プロモーション', '高校3年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105124, 105125, '[O.N.G.E.K.I.]1.05-P-0004', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100295, '【SR】井之原 小星[ONGEKI Vocal Collection 04]', 'ONGEKI Vocal Collection 04', 'Leaf', 1010, 'プロモーション', '高校1年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105122, 105123, '[O.N.G.E.K.I.]1.05-P-0005', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100296, '【SR】逢坂 茜[ONGEKI Vocal Collection 05]', 'ONGEKI Vocal Collection 05', 'Fire', 1011, 'プロモーション', '高校3年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105126, 105127, '[O.N.G.E.K.I.]1.05-P-0007', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100297, '【SR】珠洲島 有栖[ONGEKI Vocal Collection 05]', 'ONGEKI Vocal Collection 05', 'Aqua', 1012, 'プロモーション', '高校1年生', 'SR', '50,222,237,252,267,282,0,0,0,282', 105130, 105131, '[O.N.G.E.K.I.]1.05-P-0008', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100298, '【SSR】星咲 あかり[ONGEKI Sound Collection 01]', 'ONGEKI Sound Collection 01', 'Fire', 1000, 'プロモーション', '高校2年生', 'SSR', '55,252,275,290,302,312,0,0,0,312', 105144, 105145, '[O.N.G.E.K.I.]1.05-P-0001', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100299, '【R】橙[東方妖々夢]', '東方妖々夢', 'Fire', 2008, '東方妖々夢', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100012, 100053, '[O.N.G.E.K.I.]1.05-E-0039', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100300, '【R】魂魄 妖夢[東方妖々夢]', '東方妖々夢', 'Leaf', 2009, '東方妖々夢', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.05-E-0041', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100301, '【R】西行寺 幽々子[東方妖々夢]', '東方妖々夢', 'Leaf', 2010, '東方妖々夢', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0044', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100302, '【R】八雲 藍[東方妖々夢]', '東方妖々夢', 'Aqua', 2011, '東方妖々夢', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 105014, 105015, '[O.N.G.E.K.I.]1.05-E-0047', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100303, '【R】八雲 紫[東方妖々夢]', '東方妖々夢', 'Aqua', 2012, '東方妖々夢', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0049', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100304, '【SR】橙[すきま妖怪の式の式]', 'すきま妖怪の式の式', 'Fire', 2008, '東方妖々夢', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0040', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100305, '【SR】魂魄 妖夢[半人半霊の庭師]', '半人半霊の庭師', 'Leaf', 2009, '東方妖々夢', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.05-E-0042', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100306, '【SR】西行寺 幽々子[幽冥楼閣の亡霊少女]', '幽冥楼閣の亡霊少女', 'Leaf', 2010, '東方妖々夢', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0045', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100307, '【SR】八雲 藍[すきま妖怪の式]', 'すきま妖怪の式', 'Aqua', 2011, '東方妖々夢', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105012, 105013, '[O.N.G.E.K.I.]1.05-E-0048', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100308, '【SR】八雲 紫[幻想の境界]', '幻想の境界', 'Aqua', 2012, '東方妖々夢', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0050', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100309, '【SSR】西行寺 幽々子[桜符「完全なる墨染の桜 -開花-」]', '桜符「完全なる墨染の桜 -開花-」', 'Leaf', 2010, '東方妖々夢', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100033, 100074, '[O.N.G.E.K.I.]1.05-E-0046', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100310, '【SSR】魂魄 妖夢[獄界剣「二百由旬の一閃」]', '獄界剣「二百由旬の一閃」', 'Leaf', 2009, '東方妖々夢', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100036, 100077, '[O.N.G.E.K.I.]1.05-E-0043', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100311, '【R】御坂 美琴[常盤台中学]', '常盤台中学', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0005', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100312, '【R】白井 黒子[常盤台中学]', '常盤台中学', 'Leaf', 21002, 'とある科学の超電磁砲S', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100002, 100043, '[O.N.G.E.K.I.]1.05-E-0008', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100313, '【R】初春 飾利[柵川中学]', '柵川中学', 'Leaf', 21003, 'とある科学の超電磁砲S', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.05-E-0015', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100314, '【R】佐天 涙子[柵川中学]', '柵川中学', 'Leaf', 21004, 'とある科学の超電磁砲S', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0018', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100315, '【SR】御坂 美琴[電撃使い]', '電撃使い', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0006', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100316, '【SR】白井 黒子[空間移動]', '空間移動', 'Leaf', 21002, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105004, 105005, '[O.N.G.E.K.I.]1.05-E-0009', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100317, '【SR】初春 飾利[定温保存]', '定温保存', 'Leaf', 21003, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0016', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100318, '【SR】佐天 涙子[空力使い]', '空力使い', 'Leaf', 21004, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0019', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100319, '【SR】御坂 美琴[常盤台のエース]', '常盤台のエース', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.05-E-0007', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100320, '【SR】白井 黒子[風紀委員ですの！]', '風紀委員ですの！', 'Leaf', 21002, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100008, 100049, '[O.N.G.E.K.I.]1.05-E-0010', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100321, '【SR】初春 飾利[いきなり何するんですか佐天さんっ！]', 'いきなり何するんですか佐天さんっ！', 'Leaf', 21003, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105002, 105003, '[O.N.G.E.K.I.]1.05-E-0017', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100322, '【SR】佐天 涙子[うーいーはーるーん！！]', 'うーいーはーるーん！！', 'Leaf', 21004, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100008, 100049, '[O.N.G.E.K.I.]1.05-E-0020', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100323, '【SSR】御坂 美琴[超電磁砲]', '超電磁砲', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100033, 100074, '[O.N.G.E.K.I.]1.05-E-0013', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100324, '【SSR】御坂 美琴[学園都市の電撃姫]', '学園都市の電撃姫', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 105000, 105001, '[O.N.G.E.K.I.]1.05-E-0014', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100325, '【R】ペコリーヌ[美食殿]', '美食殿', 'Aqua', 22001, 'プリンセスコネクト！Re:Dive', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0024', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100326, '【R】コッコロ[美食殿]', '美食殿', 'Aqua', 22002, 'プリンセスコネクト！Re:Dive', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100004, 100045, '[O.N.G.E.K.I.]1.05-E-0027', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100327, '【R】キャル[美食殿]', '美食殿', 'Aqua', 22003, 'プリンセスコネクト！Re:Dive', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0030', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100328, '【SR】ペコリーヌ[ユースティアナ・フォン・アストライア]', 'ユースティアナ・フォン・アストライア', 'Aqua', 22001, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0025', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100329, '【SR】コッコロ[棗こころ]', '棗こころ', 'Aqua', 22002, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0028', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100330, '【SR】キャル[百地希留耶]', '百地希留耶', 'Aqua', 22003, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105004, 105005, '[O.N.G.E.K.I.]1.05-E-0031', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100331, '【SR】ペコリーヌ[プリンセスストライク]', 'プリンセスストライク', 'Aqua', 22001, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105008, 105009, '[O.N.G.E.K.I.]1.05-E-0026', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100332, '【SR】コッコロ[オーロラ]', 'オーロラ', 'Aqua', 22002, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100028, 100069, '[O.N.G.E.K.I.]1.05-E-0029', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100333, '【SR】キャル[グリムバースト]', 'グリムバースト', 'Aqua', 22003, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100029, 100070, '[O.N.G.E.K.I.]1.05-E-0032', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100334, '【SSR】ペコリーヌ[プリンセスフォース]', 'プリンセスフォース', 'Aqua', 22001, 'プリンセスコネクト！Re:Dive', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100036, 100077, '[O.N.G.E.K.I.]1.05-E-0033', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100335, '【SSR】コッコロ[精霊の啓示]', '精霊の啓示', 'Aqua', 22002, 'プリンセスコネクト！Re:Dive', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 105006, 105007, '[O.N.G.E.K.I.]1.05-E-0034', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100336, '【SR】星咲 あかり[ギーコギーコでトントントン]', 'ギーコギーコでトントントン', 'Fire', 1000, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105142, 105143, '[O.N.G.E.K.I.]1.05-0069', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100337, '【SR】藤沢 柚子[ギーコギーコでトントントン]', 'ギーコギーコでトントントン', 'Leaf', 1001, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105142, 105143, '[O.N.G.E.K.I.]1.05-0070', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100338, '【SR】三角 葵[ギーコギーコでトントントン]', 'ギーコギーコでトントントン', 'Aqua', 1002, '奏坂学園', '高校2年生', 'SR', '55,227,242,257,272,287,0,0,0,287', 105142, 105143, '[O.N.G.E.K.I.]1.05-0071', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100339, '【R】星咲 あかり[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Fire', 1000, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100110, 100111, '[O.N.G.E.K.I.]1.00-0006', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100340, '【R】藤沢 柚子[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Leaf', 1001, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100112, 100113, '[O.N.G.E.K.I.]1.00-0016', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100341, '【R】三角 葵[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Aqua', 1002, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100114, 100115, '[O.N.G.E.K.I.]1.00-0026', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100342, '【R】高瀬 梨緒[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Aqua', 1003, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100120, 100121, '[O.N.G.E.K.I.]1.00-0035', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100343, '【R】結城 莉玖[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Fire', 1004, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100116, 100117, '[O.N.G.E.K.I.]1.00-0044', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100344, '【R】藍原 椿[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Leaf', 1005, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100118, 100119, '[O.N.G.E.K.I.]1.00-0053', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100345, '【R】早乙女 彩華[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Aqua', 1006, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100126, 100127, '[O.N.G.E.K.I.]1.00-0066', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100346, '【R】桜井 春菜[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Fire', 1007, '奏坂学園', '高校2年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100122, 100123, '[O.N.G.E.K.I.]1.00-0061', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100347, '【R】九條 楓[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Leaf', 1008, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100124, 100125, '[O.N.G.E.K.I.]1.00-0071', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100348, '【R】柏木 咲姫[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Aqua', 1009, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100132, 100133, '[O.N.G.E.K.I.]1.00-0092', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100349, '【R】井之原 小星[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Leaf', 1010, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100130, 100131, '[O.N.G.E.K.I.]1.00-0085', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100350, '【R】逢坂 茜[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Fire', 1011, '奏坂学園', '高校3年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100128, 100129, '[O.N.G.E.K.I.]1.00-0101', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100351, '【R】珠洲島 有栖[シュータードレス：全身ver.]', 'シュータードレス：全身ver.', 'Aqua', 1012, '奏坂学園', '高校1年生', 'R', '50,197,212,227,242,257,0,0,0,257', 100134, 100135, '[O.N.G.E.K.I.]1.00-0113', '1.00');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100352, '【SR】御坂 美琴[ゲコラー]', 'ゲコラー', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105138, 105139, '[O.N.G.E.K.I.]1.05-E-0011', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100353, '【SR】御坂 美琴[雷撃の槍]', '雷撃の槍', 'Leaf', 21001, 'とある科学の超電磁砲S', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105140, 105141, '[O.N.G.E.K.I.]1.05-E-0012', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100375, '【R】アリサ[森の弓使い]', '森の弓使い', 'Leaf', 22004, 'プリンセスコネクト！Re:Dive', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0035', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100376, '【SR】アリサ[乙女な転校生]', '乙女な転校生', 'Leaf', 22004, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105010, 105011, '[O.N.G.E.K.I.]1.05-E-0036', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100377, '【SR】アリサ[深緑の射手]', '深緑の射手', 'Leaf', 22004, 'プリンセスコネクト！Re:Dive', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 100027, 100068, '[O.N.G.E.K.I.]1.05-E-0037', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100391, '【R】ゆんゆん[アークウィザード]', 'アークウィザード', 'Fire', 11004, 'この素晴らしい世界に祝福を！2', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 100005, 100046, '[O.N.G.E.K.I.]1.05-E-0058', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100392, '【SR】ゆんゆん[我が名はゆんゆん]', '我が名はゆんゆん', 'Fire', 11004, 'この素晴らしい世界に祝福を！2', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105024, 105025, '[O.N.G.E.K.I.]1.05-E-0059', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100393, '【SSR】ゆんゆん[紅魔族の長となるもの]', '紅魔族の長となるもの', 'Fire', 11004, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 100138, 100139, '[O.N.G.E.K.I.]1.05-E-0061', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100394, '【SSR】めぐみん[紅魔族随一の天才]', '紅魔族随一の天才', 'Fire', 11001, 'この素晴らしい世界に祝福を！2', '-', 'SSR', '55,252,275,290,302,312,0,0,0,312', 105020, 105021, '[O.N.G.E.K.I.]1.05-E-0060', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100408, '【SR】日向 美海[絶対の可能性]', '絶対の可能性', 'Aqua', 20001, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105016, 105017, '[O.N.G.E.K.I.]1.05-E-0052', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100409, '【SR】彩城 天音[サンシャイン・ヒロイン]', 'サンシャイン・ヒロイン', 'Aqua', 20002, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105010, 105011, '[O.N.G.E.K.I.]1.05-E-0053', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100410, '【SR】東条 遥[蒼輝絢爛]', '蒼輝絢爛', 'Aqua', 20003, 'アンジュ・ヴィエルジュ', '-', 'SR', '50,222,237,252,267,282,0,0,0,282', 105018, 105019, '[O.N.G.E.K.I.]1.05-E-0054', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100453, '【SR】茜ニャン[（世界の半分はいただくにゃん！）]', '（世界の半分はいただくにゃん！）', 'Fire', 5, 'プロモーション', '-', 'SR', '1,30,35,40,45,50,0,0,0,50', 105132, 105133, '[O.N.G.E.K.I.]1.05-0035', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100454, '【SSR】星咲 あかり[1st Anniversary]', '1st Anniversary', 'Fire', 1000, 'プロモーション', '高校2年生', 'SSR', '52,237,252,267,282,297,0,0,0,297', 105146, 105146, '[O.N.G.E.K.I.]1.05-0072', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100455, '【SR】茜ニャン[（とりあえず爆発させとくにゃん？）]', '（とりあえず爆発させとくにゃん？）', 'Fire', 5, 'プロモーション', '-', 'SR', '1,30,35,40,45,50,0,0,0,50', 105134, 105135, '[O.N.G.E.K.I.]1.05-0036', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100456, '【SR】茜ニャン[（まったくネコ使いが荒いにゃん……）]', '（まったくネコ使いが荒いにゃん……）', 'Fire', 5, 'プロモーション', '-', 'SR', '1,30,35,40,45,50,0,0,0,50', 105136, 105137, '[O.N.G.E.K.I.]1.05-0037', '1.05');
INSERT INTO ongeki_game_card (id, name, nick_name, attribute, chara_id, school, gakunen, rarity, level_param, skill_id, cho_kaika_skill_id, card_number, version) VALUES (100457, '【R】あおニャン[松丸君]', '松丸君', 'Aqua', 2, '-', '-', 'R', '50,197,212,227,242,257,0,0,0,257', 0, 0, '[O.N.G.E.K.I.]1.05-****', '1.05');

INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1, 'あかニャン', '', 1);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2, 'あおニャン', '', 2);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (3, 'みどニャン', '', 3);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (4, 'ナビニャン', '', 4);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (5, '茜ニャン', '', 5);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1000, '星咲 あかり', '赤尾 ひかる', 1000);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1001, '藤沢 柚子', '久保田 梨沙', 1001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1002, '三角 葵', '春野 杏', 1002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1003, '高瀬 梨緒', '久保 ユリカ', 1003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1004, '結城 莉玖', '朝日奈 丸佳', 1004);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1005, '藍原 椿', '橋本 ちなみ', 1005);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1006, '早乙女 彩華', '中島 唯', 1006);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1007, '桜井 春菜', '近藤 玲奈', 1007);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1008, '九條 楓', '佳村 はるか', 1008);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1009, '柏木 咲姫', '石見 舞菜香', 1009);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1010, '井之原 小星', 'ももの はるな', 1010);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1011, '逢坂 茜', '大空 直美', 1011);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1012, '珠洲島 有栖', '長縄 まりあ', 1012);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (1013, '柏木 美亜', '', 1013);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2000, '博麗 霊夢', '', 2000);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2001, '霧雨 魔理沙', '', 2001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2002, '十六夜 咲夜', '', 2002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2003, 'レミリア・スカーレット', '', 2003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2004, 'フランドール・スカーレット', '', 2004);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2005, '紅 美鈴', '', 2005);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2006, 'パチュリー・ノーレッジ', '', 2006);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2007, 'チルノ', '', 2007);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2008, '橙', '', 2008);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2009, '魂魄 妖夢', '', 2009);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2010, '西行寺 幽々子', '', 2010);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2011, '八雲 藍', '', 2011);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (2012, '八雲 紫', '', 2012);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (5000, '初音ミク', '', 5000);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (5001, '巡音ルカ', '', 5001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (5002, '鏡音リン', '', 5002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (11001, 'めぐみん', '高橋 李依', 11001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (11002, 'アクア', '雨宮 天', 11002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (11003, 'ダクネス', '茅野 愛衣', 11003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (11004, 'ゆんゆん', '', 11004);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (18001, '戸山香澄', '愛美', 18001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (18002, '美竹蘭', '佐倉 綾音', 18002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (18003, '丸山彩', '前島 亜美', 18003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (18004, '湊友希那', '相羽 あいな', 18004);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (18005, '弦巻こころ', '伊藤 美来', 18005);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (20001, '日向 美海', '相坂 優歌', 20001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (20002, '彩城 天音', '田村 ゆかり', 20002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (20003, '東条 遥', '中上 育実', 20003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (21001, '御坂 美琴', '', 21001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (21002, '白井 黒子', '', 21002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (21003, '初春 飾利', '', 21003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (21004, '佐天 涙子', '', 21004);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (22001, 'ペコリーヌ', '', 22001);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (22002, 'コッコロ', '', 22002);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (22003, 'キャル', '', 22003);
INSERT INTO ongeki_game_chara (id, name, cv, model_id) VALUES (22004, 'アリサ', '', 22004);

INSERT INTO ongeki_game_event (id) VALUES (1000110101);
INSERT INTO ongeki_game_event (id) VALUES (1000110102);
INSERT INTO ongeki_game_event (id) VALUES (1000110103);
INSERT INTO ongeki_game_event (id) VALUES (1000110104);
INSERT INTO ongeki_game_event (id) VALUES (1000110105);
INSERT INTO ongeki_game_event (id) VALUES (1000110201);
INSERT INTO ongeki_game_event (id) VALUES (1000110202);
INSERT INTO ongeki_game_event (id) VALUES (1000210101);
INSERT INTO ongeki_game_event (id) VALUES (1000210102);
INSERT INTO ongeki_game_event (id) VALUES (1000210103);
INSERT INTO ongeki_game_event (id) VALUES (1000210104);
INSERT INTO ongeki_game_event (id) VALUES (1000210105);
INSERT INTO ongeki_game_event (id) VALUES (1000210301);
INSERT INTO ongeki_game_event (id) VALUES (1000210401);
INSERT INTO ongeki_game_event (id) VALUES (1000210402);
INSERT INTO ongeki_game_event (id) VALUES (1000210801);
INSERT INTO ongeki_game_event (id) VALUES (1000220101);
INSERT INTO ongeki_game_event (id) VALUES (1000220102);
INSERT INTO ongeki_game_event (id) VALUES (1000220301);
INSERT INTO ongeki_game_event (id) VALUES (1000220601);
INSERT INTO ongeki_game_event (id) VALUES (1000221001);
INSERT INTO ongeki_game_event (id) VALUES (1000230101);
INSERT INTO ongeki_game_event (id) VALUES (1000230501);
INSERT INTO ongeki_game_event (id) VALUES (1000310101);
INSERT INTO ongeki_game_event (id) VALUES (1000310102);
INSERT INTO ongeki_game_event (id) VALUES (1000310201);
INSERT INTO ongeki_game_event (id) VALUES (1000310301);
INSERT INTO ongeki_game_event (id) VALUES (1000310401);
INSERT INTO ongeki_game_event (id) VALUES (1000310701);
INSERT INTO ongeki_game_event (id) VALUES (1000320101);
INSERT INTO ongeki_game_event (id) VALUES (1000320102);
INSERT INTO ongeki_game_event (id) VALUES (1000320501);
INSERT INTO ongeki_game_event (id) VALUES (1000410101);
INSERT INTO ongeki_game_event (id) VALUES (1000410102);
INSERT INTO ongeki_game_event (id) VALUES (1000410301);
INSERT INTO ongeki_game_event (id) VALUES (1000410601);
INSERT INTO ongeki_game_event (id) VALUES (1000410801);
INSERT INTO ongeki_game_event (id) VALUES (1000411001);
INSERT INTO ongeki_game_event (id) VALUES (1000411101);
INSERT INTO ongeki_game_event (id) VALUES (1000420101);
INSERT INTO ongeki_game_event (id) VALUES (1000420301);
INSERT INTO ongeki_game_event (id) VALUES (1000420401);
INSERT INTO ongeki_game_event (id) VALUES (1000420701);
INSERT INTO ongeki_game_event (id) VALUES (1000460301);
INSERT INTO ongeki_game_event (id) VALUES (1000510101);
INSERT INTO ongeki_game_event (id) VALUES (1000510102);
INSERT INTO ongeki_game_event (id) VALUES (1000510301);
INSERT INTO ongeki_game_event (id) VALUES (1000510501);
INSERT INTO ongeki_game_event (id) VALUES (1000520101);
INSERT INTO ongeki_game_event (id) VALUES (1000520102);
INSERT INTO ongeki_game_event (id) VALUES (1000520103);
INSERT INTO ongeki_game_event (id) VALUES (1000520601);
INSERT INTO ongeki_game_event (id) VALUES (1000520801);
INSERT INTO ongeki_game_event (id) VALUES (1000520901);
INSERT INTO ongeki_game_event (id) VALUES (1000521001);
INSERT INTO ongeki_game_event (id) VALUES (1000521101);
INSERT INTO ongeki_game_event (id) VALUES (1000530101);
INSERT INTO ongeki_game_event (id) VALUES (1000530301);
INSERT INTO ongeki_game_event (id) VALUES (1000530801);
INSERT INTO ongeki_game_event (id) VALUES (1000530901);
INSERT INTO ongeki_game_event (id) VALUES (1000540301);
INSERT INTO ongeki_game_event (id) VALUES (1000540701);
INSERT INTO ongeki_game_event (id) VALUES (1000560301);
INSERT INTO ongeki_game_event (id) VALUES (1000610101);
INSERT INTO ongeki_game_event (id) VALUES (1000610102);
INSERT INTO ongeki_game_event (id) VALUES (1000610201);
INSERT INTO ongeki_game_event (id) VALUES (1000610301);
INSERT INTO ongeki_game_event (id) VALUES (1000610302);
INSERT INTO ongeki_game_event (id) VALUES (1000610303);
INSERT INTO ongeki_game_event (id) VALUES (1000610501);
INSERT INTO ongeki_game_event (id) VALUES (1000620101);
INSERT INTO ongeki_game_event (id) VALUES (1000620301);
INSERT INTO ongeki_game_event (id) VALUES (1000620401);
INSERT INTO ongeki_game_event (id) VALUES (1000710101);
INSERT INTO ongeki_game_event (id) VALUES (1000710102);
INSERT INTO ongeki_game_event (id) VALUES (1000710501);
INSERT INTO ongeki_game_event (id) VALUES (1000720101);
INSERT INTO ongeki_game_event (id) VALUES (1000720102);
INSERT INTO ongeki_game_event (id) VALUES (1000720103);
INSERT INTO ongeki_game_event (id) VALUES (1000720601);
INSERT INTO ongeki_game_event (id) VALUES (1000720801);
INSERT INTO ongeki_game_event (id) VALUES (1000720901);
INSERT INTO ongeki_game_event (id) VALUES (1000721001);
INSERT INTO ongeki_game_event (id) VALUES (1000721101);
INSERT INTO ongeki_game_event (id) VALUES (1000760301);
INSERT INTO ongeki_game_event (id) VALUES (1000770301);
INSERT INTO ongeki_game_event (id) VALUES (1050210101);
INSERT INTO ongeki_game_event (id) VALUES (1050210102);
INSERT INTO ongeki_game_event (id) VALUES (1050210103);
INSERT INTO ongeki_game_event (id) VALUES (1050210104);
INSERT INTO ongeki_game_event (id) VALUES (1050210105);
INSERT INTO ongeki_game_event (id) VALUES (1050210106);
INSERT INTO ongeki_game_event (id) VALUES (1050210107);
INSERT INTO ongeki_game_event (id) VALUES (1050210108);
INSERT INTO ongeki_game_event (id) VALUES (1050210109);
INSERT INTO ongeki_game_event (id) VALUES (1050210301);
INSERT INTO ongeki_game_event (id) VALUES (1050210302);
INSERT INTO ongeki_game_event (id) VALUES (1050210303);
INSERT INTO ongeki_game_event (id) VALUES (1050210304);
INSERT INTO ongeki_game_event (id) VALUES (1050210305);
INSERT INTO ongeki_game_event (id) VALUES (1050210306);
INSERT INTO ongeki_game_event (id) VALUES (1050210501);
INSERT INTO ongeki_game_event (id) VALUES (1050210502);
INSERT INTO ongeki_game_event (id) VALUES (1050210701);
INSERT INTO ongeki_game_event (id) VALUES (1050210801);
INSERT INTO ongeki_game_event (id) VALUES (1050211301);
INSERT INTO ongeki_game_event (id) VALUES (1050211302);
INSERT INTO ongeki_game_event (id) VALUES (1050230101);
INSERT INTO ongeki_game_event (id) VALUES (1050230102);
INSERT INTO ongeki_game_event (id) VALUES (1050230103);
INSERT INTO ongeki_game_event (id) VALUES (1050230104);
INSERT INTO ongeki_game_event (id) VALUES (1050230201);
INSERT INTO ongeki_game_event (id) VALUES (1050230301);
INSERT INTO ongeki_game_event (id) VALUES (1050230302);
INSERT INTO ongeki_game_event (id) VALUES (1050230401);
INSERT INTO ongeki_game_event (id) VALUES (1050230601);
INSERT INTO ongeki_game_event (id) VALUES (1050230801);
INSERT INTO ongeki_game_event (id) VALUES (1050230901);
INSERT INTO ongeki_game_event (id) VALUES (1050231001);
INSERT INTO ongeki_game_event (id) VALUES (1050231101);
INSERT INTO ongeki_game_event (id) VALUES (1050231301);

INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (1, 'チュートリアル', 'チユトリアル', 'チュートリアル', 'オンゲキ', 1, 1, '0,0', '0,0', '0,0', '0,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (7, 'We Gonna Journey', 'WEGONNAJOURNEY', 'Queen P.A.L.', 'チュウマイ', 100058, 30, '3,0', '7,0', '10,40', '13,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (8, 'ＧＯ！ＧＯ！ラブリズム♥', 'GOGOラフリスム', '片霧烈火オンザみんマンション', 'チュウマイ', 100005, 5, '3,0', '6,0', '9,20', '12,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (11, 'Cyberozar', 'CYBEROZAR', '削除', 'チュウマイ', 100032, 20, '4,0', '6,0', '10,0', '13,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (17, '檄!帝国華撃団', 'ケキテイコクカケキタン', '真宮寺さくら（横山智佐）＆帝国歌劇団「サクラ大戦」', 'VARIETY', 100005, 1, '2,0', '5,0', '7,90', '11,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (22, 'ぼくらの16bit戦争', 'ホクラノ16BITウオス', 'sasakure.UK', 'niconico', 100070, 35, '3,0', '6,0', '10,30', '13,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (24, 'シュガーソングとビターステップ', 'シユカソンクトヒタステツフ', 'UNISON SQUARE GARDEN', 'POPS＆ANIME', 100005, 1, '2,0', '5,0', '7,70', '11,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (25, '回レ！雪月花', 'マワレセツケツカ', '歌組雪月花 夜々(原田 ひとみ)/いろり(茅野 愛衣)/小紫(小倉 唯) 「機巧少女は傷つかない」', 'POPS＆ANIME', 100005, 3, '3,0', '6,0', '9,0', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (26, 'ファッとして桃源郷', 'フアツトシテトウケンキヨウ', '新庄 かなえ(CV:三森 すずこ) 「てーきゅう」', 'POPS＆ANIME', 100014, 1, '3,0', '6,0', '8,0', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (27, 'かくしん的☆めたまるふぉ～ぜっ!', 'カクシンテキメタマルフオセツ', '土間うまる [CV.田中あいみ]「干物妹！うまるちゃん」', 'POPS＆ANIME', 100014, 1, '3,0', '5,0', '8,30', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (28, 'Oshama Scramble!', 'OSHAMASCRAMBLE', 't+pazolite', 'チュウマイ', 100014, 5, '4,0', '7,60', '10,20', '13,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (29, 'This game', 'THISGAME', '鈴木このみ「ノーゲーム・ノーライフ」', 'POPS＆ANIME', 100023, 1, '3,0', '6,0', '8,70', '11,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (30, '君の知らない物語', 'キミノシラナイモノカタリ', 'supercell 「化物語」', 'POPS＆ANIME', 100005, 1, '2,0', '5,0', '7,80', '10,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (31, 'SAVIOR OF SONG', 'SAVIOROFSONG', 'ナノ feat.MY FIRST STORY 「蒼き鋼のアルペジオ ‐アルス・ノヴァ‐」', 'POPS＆ANIME', 100023, 1, '3,0', '7,30', '8,60', '12,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (32, '心象蜃気楼', 'シンシヨウシンキロウ', 'Orangestar', 'チュウマイ', 100023, 3, '2,0', '6,0', '8,90', '12,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (33, 'Bad Apple!! feat.nomico', 'BADAPPLEFEATNOMICO', 'Masayoshi Minoshima', '東方Project', 100094, 1, '2,0', '5,0', '8,50', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (34, '幻想のサテライト', 'ケンソウノサテライト', '豚乙女', '東方Project', 100091, 5, '3,0', '7,20', '10,30', '13,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (35, 'AMAZING MIGHTYYYY!!!!', 'AMAZINGMIGHTYYYY', 'WAiKURO', 'チュウマイ', 100032, 20, '4,0', '7,60', '10,70', '13,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (36, 'Perfect Shining!!', 'PERFECTSHINING', '曲：宮崎誠／歌：星咲 あかり(CV:赤尾 ひかる)', 'オンゲキ', 100005, 1, '3,0', '6,0', '8,10', '11,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (37, 'ようこそジャパリパークへ', 'ヨウコソシヤハリハクヘ', 'どうぶつビスケッツ×PPP「けものフレンズ」', 'POPS＆ANIME', 100014, 1, '3,0', '6,0', '8,20', '11,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (38, 'Mare Maris', 'MAREMARIS', 'M2U', 'チュウマイ', 100031, 5, '3,0', '7,10', '9,0', '12,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (39, 'Gate of Doom', 'GATEOFDOOM', 'Masahiro “Godspeed” Aoki', 'チュウマイ', 100039, 20, '4,0', '7,70', '11,0', '13,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (40, '月に叢雲華に風', 'ツキニムラクモハナニカセ', '幽閉サテライト', '東方Project', 100094, 1, '3,0', '6,0', '8,70', '12,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (41, 'Los! Los! Los!', 'LOSLOSLOS', 'ターニャ・デグレチャフ(CV.悠木碧)「幼女戦記」', 'POPS＆ANIME', 100023, 1, '2,0', '6,0', '8,70', '12,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (42, 'The Formula', 'THEFORMULA', 'Junk', 'VARIETY', 100023, 5, '4,0', '7,40', '9,70', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (45, 'Halcyon', 'HALCYON', 'xi', 'VARIETY', 100031, 10, '4,0', '7,70', '11,0', '13,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (46, 'Hyper Active', 'HYPERACTIVE', 'HiTECH NINJA', 'チュウマイ', 100031, 8, '3,0', '7,90', '11,0', '12,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (47, '六兆年と一夜物語', 'ロクチヨウネントイチヤモノカタリ', 'kemu', 'niconico', 100038, 20, '3,0', '6,0', '9,20', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (49, '天ノ弱', 'アマノシヤク', '164', 'niconico', 100038, 10, '3,0', '5,0', '9,60', '12,10', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (50, '千本桜', 'センホンサクラ', '黒うさP', 'niconico', 100045, 10, '2,0', '6,0', '8,70', '11,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (52, '天火明命', 'アメノホアカリ', '削除', 'チュウマイ', 100045, 20, '3,0', '6,0', '9,80', '12,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (53, 'キミノヨゾラ哨戒班', 'キミノヨソラシヨウカイハン', 'Orangestar', 'niconico', 100039, 20, '3,0', '6,0', '9,40', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (54, '鬼KYOKAN', 'オニKYOKAN', 'じーざすP（ワンダフル☆オポチュニティ！）', 'niconico', 100046, 20, '4,0', '6,0', '10,50', '13,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (55, 'オモイヨシノ', 'オモイヨシノ', 'loos feat. 柊莉杏', 'VARIETY', 100045, 15, '3,0', '5,0', '9,10', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (56, '初音ミクの激唱', 'ハツネミクノケキシヨウ', 'Storyteller', 'niconico', 100158, 1, '4,0', '7,70', '10,90', '13,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (57, 'ヒビカセ', 'ヒヒカセ', 'GigaReol', 'niconico', 100158, 1, '2,0', '6,0', '9,40', '12,10', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (58, 'ときめきエクスペリエンス！', 'トキメキエクスヘリエンス', 'Poppin’Party「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100127, 1, '2,0', '6,0', '8,60', '10,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (59, 'That Is How I Roll!', 'THATISHOWIROLL', 'Afterglow「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100132, 1, '2,0', '5,0', '9,50', '11,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (60, 'しゅわりん☆どり〜みん', 'シユワリントリミン', 'Pastel＊Palettes「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100136, 1, '2,0', '6,0', '8,80', '10,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (61, 'BLACK SHOUT', 'BLACKSHOUT', 'Roselia「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100140, 1, '2,0', '5,0', '9,10', '11,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (62, 'えがおのオーケストラっ！', 'エカオノオケストラツ', 'ハロー、ハッピーワールド！「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100144, 1, '2,0', '5,0', '8,40', '10,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (63, 'STARTLINER', 'STARTLINER', '曲：kz(livetune)／歌：オンゲキシューターズ', 'オンゲキ', 100005, 2, '2,0', '5,0', '8,70', '11,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (64, 'みんな Happy!!', 'ミンナHAPPY', '曲：ヒゲドライバー／歌：藤沢 柚子(CV:久保田 梨沙)', 'オンゲキ', 100014, 1, '2,0', '5,0', '9,0', '11,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (65, 'Zest of Blue', 'ZESTOFBLUE', '曲：上松範廉(Elements Garden)／歌：三角 葵(CV:春野 杏)', 'オンゲキ', 100023, 1, '3,0', '6,0', '9,70', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (66, 'Dolphika', 'DOLPHIKA', 'HiTECH NINJA', 'オンゲキ', 100031, 10, '3,0', '6,0', '10,70', '13,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (67, 'Here We Go', 'HEREWEGO', '曲：TeddyLoid／歌：高瀬 梨緒(CV:久保 ユリカ)', 'オンゲキ', 100031, 5, '2,0', '5,0', '9,30', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (68, 'P！P！P！P！がおー!!', 'PPPPカオ', '曲：Tom-H@ck／歌：結城 莉玖(CV：朝日奈 丸佳)', 'オンゲキ', 100038, 5, '4,0', '7,90', '10,50', '12,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (69, 'タテマエと本心の大乱闘', 'タテマエトホンシンノタイラントウ', '曲：鯨井国家／歌：藍原 椿(CV：橋本 ちなみ)', 'オンゲキ', 100045, 5, '4,0', '6,0', '9,80', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (70, 'Ai Nov', 'AINOV', 'Feryquitous', 'オンゲキ', 100032, 30, '4,0', '7,70', '11,0', '13,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (71, 'ロッキンピンクモンスター', 'ロツキンヒンクモンスタ', '豚乙女', 'オンゲキ', 100039, 30, '3,0', '7,50', '10,80', '12,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (72, 'ブツメツビーターズ', 'フツメツヒタス', '有形ランペイジ', 'オンゲキ', 100046, 30, '3,0', '7,60', '9,90', '13,10', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (73, '鳥の詩', 'トリノウタ', 'Lia「AIR」', 'POPS＆ANIME', 100052, 20, '1,0', '5,0', '8,20', '11,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (76, 'SWEET SHAKE!!', 'SWEETSHAKE', '曲：佐藤純一（fhána）／歌：桜井 春菜(CV：近藤 玲奈)', 'オンゲキ', 100052, 10, '2,0', '6,0', '8,90', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (77, 'SAKURA', 'SAKURA', 'WITCH NUMBER 4「Tokyo 7th シスターズ」', 'POPS＆ANIME', 100058, 15, '2,0', '5,0', '8,20', '10,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (78, 'Paqqin', 'PAQQIN', 'owl＊tree', 'チュウマイ', 100058, 20, '3,0', '6,0', '10,40', '13,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (79, 'What color...', 'WHATCOLOR', '曲：NAOKI／歌：早乙女 彩華(CV：中島 唯)', 'オンゲキ', 100058, 10, '2,0', '6,0', '9,30', '12,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (80, '閃鋼のブリューナク', 'センコウノフリユナク', 'sasakure.UK', 'チュウマイ', 100064, 30, '4,0', '7,80', '10,40', '13,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (81, '神威', 'カムイ', 'TJ.hangneil', 'VARIETY', 100064, 30, '4,0', '8,0', '12,0', '14,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (82, 'Maqrite', 'MAQRITE', 'owl＊tree', 'オンゲキ', 100064, 40, '2,0', '6,0', '9,70', '13,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (83, 'Sword of Secret', 'SWORDOFSECRET', 'Cranky,Morrigan feat.Lily', 'オンゲキ', 100064, 40, '3,0', '7,20', '9,80', '13,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (84, 'Everlasting Today', 'EVERLASTINGTODAY', 'void (Mournfinale)', 'オンゲキ', 100064, 50, '3,0', '7,80', '12,10', '14,10', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (85, 'Grip & Break down !!', 'GRIPANDBREAKDOWN', 'SOUND HOLIC feat. Nana Takahashi', '東方Project', 100094, 5, '3,0', '6,0', '9,20', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (86, 'CiRCLING', 'CIRCLING', 'Poppin’Party「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100168, 10, '2,0', '6,0', '8,70', '11,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (87, 'Hey-day狂騒曲（カプリチオ）', 'HEYDAYカプリチオ', 'Afterglow「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100169, 10, '2,0', '6,0', '9,20', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (88, 'ゆら・ゆらRing-Dong-Dance', 'ユラユラRINGDONGDANCE', 'Pastel＊Palettes「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100170, 10, '3,0', '6,0', '9,0', '12,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (89, 'Opera of the wasteland', 'OPERAOFTHEWASTELAND', 'Roselia「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100171, 10, '2,0', '6,0', '9,20', '12,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (90, 'ゴーカ！ごーかい！？ファントムシーフ！', 'コカコカイフアントムシフ', 'ハロー、ハッピーワールド！「バンドリ！ ガールズバンドパーティ！」', 'POPS＆ANIME', 100172, 10, '3,0', '7,0', '9,70', '12,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (91, 'おこちゃま戦争', 'オコチヤマセンソウ', 'ギガ/れをる', 'niconico', 100038, 15, '3,0', '6,0', '9,60', '12,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (92, 'Brain Power', 'BRAINPOWER', 'ノマ', 'VARIETY', 100031, 8, '4,0', '6,0', '9,90', '13,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (93, 'GAME IS LIFE', 'GAMEISLIFE', '曲：ヒゲドライバー／歌：井之原 小星(CV：ももの はるな)', 'オンゲキ', 100070, 35, '3,0', '7,0', '9,80', '12,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (94, 'GranFatalité', 'GRANFATALITE', '曲：ZAQ／歌：柏木 咲姫(CV：石見 舞菜香)', 'オンゲキ', 100076, 35, '3,0', '7,30', '9,30', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (95, 'Dazzle hop', 'DAZZLEHOP', 'Sampling Masters MEGA', 'オンゲキ', 100082, 50, '5,0', '9,0', '12,30', '14,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (96, 'YURUSHITE', 'YURUSHITE', 't+pazolite', 'オンゲキ', 100082, 60, '4,0', '9,0', '12,30', '14,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (97, 'Opfer', 'OPFER', 'かねこちはる', 'オンゲキ', 100088, 60, '4,0', '9,0', '13,30', '14,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (98, 'Titania', 'TITANIA', 'xi', 'オンゲキ', 100088, 70, '5,0', '9,70', '13,30', '14,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (99, 'oboro', 'OBORO', 'ヒゲドライバー', 'チュウマイ', 100070, 45, '2,0', '6,0', '9,40', '12,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (100, 'Äventyr', 'AEVENTYR', 'Grand Thaw / Rigel Theatre', 'VARIETY', 100076, 35, '2,0', '5,0', '8,70', '12,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (101, '7thSense', '7THSENSE', '削除', 'チュウマイ', 100076, 45, '4,0', '7,80', '11,70', '14,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (107, 'sweet little sister', 'SWEETLITTLESISTER', 'Silver Forest', '東方Project', 100103, 8, '3,0', '6,0', '9,10', '12,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (108, '今ぞ♡崇め奉れ☆オマエらよ！！～姫の秘メタル渇望～', 'イマソアカメタテマツレオマエラヨヒメノヒメタルカツホウ', 'あべにゅうぷろじぇくと feat.佐倉 紗織　produced by ave;new', 'チュウマイ', 100082, 35, '3,0', '7,60', '9,80', '13,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (109, 'Kattobi KEIKYU Rider', 'KATTOBIKEIKYURIDER', 'Sampling Masters MEGA', 'チュウマイ', 100082, 40, '3,0', '7,70', '10,80', '13,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (110, 'エンドマークに希望と涙を添えて', 'エントマクニキホウトナミタヲソエテ', 'cosMo＠暴走P', 'チュウマイ', 100082, 45, '4,0', '8,70', '11,80', '14,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (111, '怒槌', 'イカツチ', '光吉猛修', 'チュウマイ', 100088, 50, '4,0', '8,70', '12,70', '14,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (112, '混沌を越えし我らが神聖なる調律主を讃えよ', 'コントンヲコエシワレラカシンセイナルチヨウリツシユヲタタエヨ', '穴山大輔', 'チュウマイ', 100088, 55, '5,0', '8,70', '12,90', '14,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (116, 'TOMORROW', 'TOMORROW', 'Machico「この素晴らしい世界に祝福を！2」', 'POPS＆ANIME', 100121, 1, '2,0', '5,0', '8,80', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (117, 'カミサマネジマキ', 'カミサマネシマキ', 'kemu', 'niconico', 100011, 5, '3,0', '7,0', '11,70', '13,80', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (118, '脳漿炸裂ガール', 'ノウシヨウサクレツカル', 'れるりり', 'niconico', 100014, 3, '3,0', '5,0', '10,0', '13,10', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (119, '幸せになれる隠しコマンドがあるらしい', 'シアワセニナレルカクシコマントカアルラシイ', 'うたたP', 'niconico', 100011, 10, '2,0', '7,10', '9,70', '13,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (120, 'チュルリラ・チュルリラ・ダッダッダ！', 'チユルリラチユルリラタツタツタ', '和田たけあき(くらげP)', 'niconico', 100011, 15, '3,0', '7,50', '10,0', '12,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (121, 'Red Battle', 'REDBATTLE', 'めぐみん（CV：高橋李依）、ゆんゆん（CV：豊崎愛生）「この素晴らしい世界に祝福を！2」', 'POPS＆ANIME', 100117, 10, '1,0', '6,0', '8,70', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (122, 'わたし音頭', 'ワタシオント', 'アクア（CV：雨宮天）「この素晴らしい世界に祝福を！2」', 'POPS＆ANIME', 100121, 10, '1,0', '5,0', '8,40', '11,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (123, '連れ去って・閉じ込めて・好きにして', 'ツレサツテトシコメテスキニシテ', 'ダクネス（CV：茅野愛衣）「この素晴らしい世界に祝福を！2」', 'POPS＆ANIME', 100124, 10, '1,0', '5,0', '8,0', '10,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (124, 'GOODRAGE', 'GOODRAGE', 'EBIMAYO', 'VARIETY', 100020, 30, '4,0', '7,90', '11,50', '13,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (125, 'Dragoon', 'DRAGOON', 'Shandy kubota', 'チュウマイ', 100020, 30, '3,0', '6,0', '9,20', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (126, 'ゴーストルール', 'コストルル', 'DECO*27', 'niconico', 100158, 8, '3,0', '6,0', '9,70', '12,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (128, 'Red “reduction division” -crossroads version-', 'REDREDUCTIONDIVISION', 'fripSide', 'POPS＆ANIME', 100052, 15, '2,0', '5,0', '9,0', '11,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (131, 'First Twinkle', 'FIRSTTWINKLE', '折戸伸治 feat.北沢綾香', 'チュウマイ', 100052, 30, '2,0', '6,0', '9,0', '11,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (136, 'Energy Booster ～ 上海紅茶館', 'ENERGYBOOSTERシヤンハイコウチヤカン', '激戦の人', '東方Project', 100106, 1, '3,0', '6,0', '9,40', '12,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (137, 'アクアテラリウム', 'アクアテラリウム', '森羅万象', '東方Project', 100109, 5, '2,0', '5,0', '8,50', '12,0', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (138, '四次元跳躍機関', 'ヨシケンチヨウヤクキカン', 'koutaq', '東方Project', 100097, 5, '2,0', '5,0', '8,30', '12,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (139, 'れみりあ☆デスティニー', 'レミリアテイステイニ', 'ふぉれすとぴれお', '東方Project', 100100, 8, '3,0', '6,0', '9,50', '12,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (140, 'Destiny Runner', 'DESTINYRUNNER', 'さわわ', '東方Project', 100100, 20, '3,0', '7,0', '10,0', '13,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (141, '最終鬼畜妹フランドール・Ｓ', 'サイシユウキチクイモウトフラントルS', 'ビートまりお', '東方Project', 100103, 25, '3,0', '7,50', '10,30', '13,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (145, 'ナイト・オブ・ナイツ', 'ナイトオフナイツ', 'ビートまりお', '東方Project', 100091, 1, '3,0', '7,40', '9,70', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (148, '木彫り鯰と右肩ゾンビ', 'キホリナマストミキカタソンヒ', 'hanzo/赤飯歌唱Ver', 'niconico', 100046, 20, '3,0', '6,0', '9,90', '12,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (149, '砂の惑星 feat. HATSUNE MIKU', 'スナノワクセイFEATHATSUNEMIKU', 'ハチ', 'niconico', 100158, 5, '2,0', '5,0', '8,70', '12,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (150, 'ブリキノダンス', 'フリキノタンス', '日向電工', 'niconico', 100158, 10, '2,0', '5,0', '9,20', '11,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (151, 'グリーンライツ・セレナーデ', 'クリンライツセレナテ', 'Omoi feat. 初音ミク', 'niconico', 100158, 6, '3,0', '6,0', '9,60', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (152, 'いーあるふぁんくらぶ', 'イアルフアンクラフ', 'みきとP', 'niconico', 100011, 8, '2,0', '5,0', '8,60', '11,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (153, 'Redo', 'REDO', '鈴木このみ「Re:ゼロから始める異世界生活」', 'POPS＆ANIME', 100236, 1, '3,0', '6,0', '9,40', '12,50', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (154, 'Jump!! Jump!! Jump!!', 'JUMPJUMPJUMP', '曲：宮崎誠／歌：オンゲキシューターズ', 'オンゲキ', 100236, 2, '3,0', '6,0', '9,40', '12,60', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (155, 'TAKE ON THE WORLD', 'TAKEONTHEWORLD', 'REDALiCE', 'オンゲキ', 100236, 5, '3,0', '7,40', '10,20', '13,20', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (156, 'Grand symphony', 'GRANDSYMPHONY', '佐咲紗花', 'POPS＆ANIME', 100237, 1, '3,0', '6,0', '9,20', '12,40', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (157, 'TeA', 'TEA', 'ガリガリさむし', 'オンゲキ', 100237, 5, '3,0', '7,0', '10,0', '12,90', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (158, '流星', 'リユウセイ', '藍井エイル', 'POPS＆ANIME', 100238, 1, '2,0', '5,0', '8,70', '12,30', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (159, 'fulgente', 'FULGENTE', 'Junk', 'オンゲキ', 100238, 5, '2,0', '6,0', '8,90', '12,70', '0,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (8001, 'No Remorse', 'NOREMORSE', '並木 学「ケツイ ～絆地獄たち～」', 'VARIETY', 1, 50, '0,0', '0,0', '0,0', '0,0', '14,0');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (8002, 'ナイト・オブ・ナイツ', 'ナイトオフナイツ', 'ビートまりお', '東方Project', 3, 50, '0,0', '0,0', '0,0', '0,0', '13,80');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (8021, '初音ミクの激唱', 'ハツネミクノケキシヨウ', 'Storyteller', 'niconico', 2, 50, '0,0', '0,0', '0,0', '0,0', '13,90');
INSERT INTO ongeki_game_music (id, name, sort_name, artist_name, genre, boss_card_id, boss_level, level0, level1, level2, level3, level4) VALUES (8022, 'ようこそジャパリパークへ', 'ヨウコソシヤハリハクヘ', 'どうぶつビスケッツ×PPP「けものフレンズ」', 'POPS＆ANIME', 100237, 30, '0,0', '0,0', '0,0', '0,0', '13,0');

INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (0, '-', 'None', 'スキルなし');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100000, 'ボスアタック +5', 'Attack', 'バトル後半で、自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100001, 'リーフガード +20', 'Guard', '属性【LEAF】からのダメージ20％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100002, 'アクアガード +20', 'Guard', '属性【AQUA】からのダメージ20％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100003, 'ファイアガード +20', 'Guard', '属性【FIRE】からのダメージ20％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100004, 'ブースト +5', 'Boost', '【ATTACK】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100005, 'ボスアタック +10', 'Attack', 'バトル後半で、自身の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100006, '茜フュージョン +4', 'Attack', '【逢坂 茜】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100007, 'ボス全属性ガード +35', 'Guard', 'バトル後半で、ダメージ35％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100008, 'アタック +10', 'Attack', '自身の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100009, 'アタック +20（危）', 'DangerAttack', '自身の攻撃力20％アップ
被弾時のダメージが2倍になる');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100010, 'アクアガード +35（危）', 'DangerGuard', '属性【AQUA】からのダメージ35％軽減
自身の攻撃力3％アップ
ミス1回につき3％ダメージ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100011, 'ブースト +12（危）', 'DangerBoost', '【ATTACK】の攻撃力12％アップ
被弾時のダメージが2倍になる');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100012, 'SIDE-Lアシスト +3', 'Support', 'ノーツ【SIDE-L】を自動で攻撃する
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100013, '茜ブースト +10', 'Boost', '【逢坂 茜】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100014, 'ボスアタック +12', 'Attack', 'バトル後半で、自身の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100015, 'SIDE-Rアシスト +5', 'Support', 'ノーツ【SIDE-R】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100016, 'リーフガード +30', 'Guard', '属性【LEAF】からのダメージ30％軽減
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100017, 'アクアガード +30', 'Guard', '属性【AQUA】からのダメージ30％軽減
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100018, 'ファイアガード +30', 'Guard', '属性【FIRE】からのダメージ30％軽減
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100019, 'ファイアブースト +10', 'Boost', '属性【FIRE】かつ【ATTACK】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100020, 'リーフブースト +10', 'Boost', '属性【LEAF】かつ【ATTACK】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100021, 'アクアブースト +10', 'Boost', '属性【AQUA】かつ【ATTACK】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100022, 'ボス茜フュージョン +6', 'Attack', 'バトル後半で、【逢坂 茜】のカード1枚につき、
自身の攻撃力6％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100023, 'まんたんブースト +12', 'Boost', 'ライフ100％時、【ATTACK】の攻撃12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100024, 'ノーダメブースト +14', 'Boost', 'ダメージカウント0の時、【ATTACK】の攻撃14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100025, 'アタック +11', 'Attack', '自身の攻撃力11％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100026, 'ボスファイアブースト +15', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100027, 'ボスリーフブースト +15', 'Boost', 'バトル後半で、
属性【LEAF】かつ【ATTACK】の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100028, 'ボスアクアブースト +15', 'Boost', 'バトル後半で、
属性【AQUA】かつ【ATTACK】の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100029, 'ボスアタック +15', 'Attack', 'バトル後半で、自身の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100030, 'ボス茜ブースト +15', 'Boost', 'バトル後半で、【逢坂 茜】の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100031, 'ノーダメアタック +14', 'Attack', 'ダメージカウント0の時、自身の攻撃14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100032, 'SIDE-LRアシスト +5', 'Support', 'ノーツ【SIDE】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100033, 'ボスアタック +17', 'Attack', 'バトル後半で、自身の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100034, 'ボスアタック +20', 'Attack', 'バトル後半で、自身の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100035, '全属性ガード +30', 'Guard', 'ダメージ軽減30％');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100036, 'アタック +15', 'Attack', '自身の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100037, 'ボスアクアブースト +20', 'Boost', 'バトル後半で、
属性【AQUA】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100038, 'ファイアブースト +20', 'Boost', '属性【FIRE】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100039, 'リーフブースト +20', 'Boost', '属性【LEAF】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100040, 'アクアブースト +20', 'Boost', '属性【AQUA】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100041, 'ボスアタック +25', 'Attack', 'バトル後半で、自身の攻撃力25％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100042, 'リーフガード +25', 'Guard', '属性【LEAF】からのダメージ25％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100043, 'アクアガード +25', 'Guard', '属性【AQUA】からのダメージ25％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100044, 'ファイアガード +25', 'Guard', '属性【FIRE】からのダメージ25％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100045, 'ブースト +7', 'Boost', '【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100046, 'ボスアタック +12', 'Attack', 'バトル後半で、自身の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100047, '茜フュージョン +5', 'Attack', '【逢坂 茜】のカード1枚につき、自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100048, 'ボス全属性ガード +40', 'Guard', 'バトル後半で、ダメージ40％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100049, 'アタック +12', 'Attack', '自身の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100050, 'アタック +22（危）', 'DangerAttack', '自身の攻撃力22％アップ
被弾時のダメージが2倍になる');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100051, 'アクアガード +40（危）', 'DangerGuard', '属性【AQUA】からのダメージ40％軽減
自身の攻撃力3％アップ
ミス1回につき3％ダメージ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100052, 'ブースト +14（危）', 'DangerBoost', '【ATTACK】の攻撃力14％アップ
被弾時のダメージが2倍になる');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100053, 'SIDE-Lアシスト +5', 'Support', 'ノーツ【SIDE-L】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100054, '茜ブースト +12', 'Boost', '【逢坂 茜】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100055, 'ボスアタック +14', 'Attack', 'バトル後半で、自身の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100056, 'SIDE-Rアシスト +7', 'Support', 'ノーツ【SIDE-R】を自動で攻撃する
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100057, 'リーフガード +35', 'Guard', '属性【LEAF】からのダメージ35％軽減
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100058, 'アクアガード +35', 'Guard', '属性【AQUA】からのダメージ35％軽減
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100059, 'ファイアガード +35', 'Guard', '属性【FIRE】からのダメージ35％軽減
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100060, 'ファイアブースト +12', 'Boost', '属性【FIRE】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100061, 'リーフブースト +12', 'Boost', '属性【LEAF】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100062, 'アクアブースト +12', 'Boost', '属性【AQUA】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100063, 'ボス茜フュージョン +7', 'Attack', 'バトル後半で、【逢坂 茜】のカード1枚につき、
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100064, 'まんたんブースト +14', 'Boost', 'ライフ100％時、【ATTACK】の攻撃14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100065, 'ノーダメブースト +16', 'Boost', 'ダメージカウント0の時、【ATTACK】の攻撃16％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100066, 'アタック +13', 'Attack', '自身の攻撃力13％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100067, 'ボスファイアブースト +17', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100068, 'ボスリーフブースト +17', 'Boost', 'バトル後半で、
属性【LEAF】かつ【ATTACK】の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100069, 'ボスアクアブースト +17', 'Boost', 'バトル後半で、
属性【AQUA】かつ【ATTACK】の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100070, 'ボスアタック +17', 'Attack', 'バトル後半で、自身の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100071, 'ボス茜ブースト +17', 'Boost', 'バトル後半で、【逢坂 茜】の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100072, 'ノーダメアタック +16', 'Attack', 'ダメージカウント0の時、自身の攻撃16％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100073, 'SIDE-LRアシスト +7', 'Support', 'ノーツ【SIDE】を自動で攻撃する
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100074, 'ボスアタック +19', 'Attack', 'バトル後半で、自身の攻撃力19％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100075, 'ボスアタック +22', 'Attack', 'バトル後半で、自身の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100076, '全属性ガード +35', 'Guard', 'ダメージ軽減35％');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100077, 'アタック +17', 'Attack', '自身の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100078, 'ボスアクアブースト +22', 'Boost', 'バトル後半で、
属性【AQUA】かつ【ATTACK】の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100079, 'ファイアブースト +22', 'Boost', '属性【FIRE】かつ【ATTACK】の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100080, 'リーフブースト +22', 'Boost', '属性【LEAF】かつ【ATTACK】の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100081, 'アクアブースト +22', 'Boost', '属性【AQUA】かつ【ATTACK】の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100082, 'めぐみんブースト +10', 'Boost', '【めぐみん】かつ【ATTACK】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100083, 'めぐみんブースト +12', 'Boost', '【めぐみん】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100084, 'めぐみんブースト +15', 'Boost', '【めぐみん】かつ【ATTACK】の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100085, 'めぐみんブースト +17', 'Boost', '【めぐみん】かつ【ATTACK】の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100086, '美海ブースト +10', 'Boost', '【日向 美海】かつ【ATTACK】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100087, '美海ブースト +12', 'Boost', '【日向 美海】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100088, '美海ブースト +15', 'Boost', '【日向 美海】かつ【ATTACK】の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100089, '美海ブースト +17', 'Boost', '【日向 美海】かつ【ATTACK】の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100090, 'ボスファイアブースト +20', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100091, 'ボスファイアブースト +22', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100092, '柚子＆葵ブースト +5', 'Boost', '【藤沢 柚子】と【三角 葵】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100093, '柚子＆葵ブースト +7', 'Boost', '【藤沢 柚子】と【三角 葵】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100094, 'あかり＆葵ブースト +5', 'Boost', '【星咲 あかり】と【三角 葵】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100095, 'あかり＆葵ブースト +7', 'Boost', '【星咲 あかり】と【三角 葵】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100096, 'あかり＆柚子ブースト +5', 'Boost', '【星咲 あかり】と【藤沢 柚子】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100097, 'あかり＆柚子ブースト +7', 'Boost', '【星咲 あかり】と【藤沢 柚子】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100098, '梨緒＆莉玖ブースト +5', 'Boost', '【高瀬 梨緒】と【結城 莉玖】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100099, '梨緒＆莉玖ブースト +7', 'Boost', '【高瀬 梨緒】と【結城 莉玖】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100100, '梨緒＆椿ブースト +5', 'Boost', '【高瀬 梨緒】と【藍原 椿】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100101, '梨緒＆椿ブースト +7', 'Boost', '【高瀬 梨緒】と【藍原 椿】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100102, '莉玖＆椿ブースト +5', 'Boost', '【結城 莉玖】と【藍原 椿】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100103, '莉玖＆椿ブースト +7', 'Boost', '【結城 莉玖】と【藍原 椿】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100104, 'ボス莉玖フュージョン +7', 'Attack', 'バトル後半で、【結城 莉玖】のカード1枚につき、
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100105, 'ボス莉玖フュージョン +8', 'Attack', 'バトル後半で、【結城 莉玖】のカード1枚につき、
自身の攻撃力8％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100106, 'ボス椿フュージョン +7', 'Attack', 'バトル後半で、【藍原 椿】のカード1枚につき、
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100107, 'ボス椿フュージョン +8', 'Attack', 'バトル後半で、【藍原 椿】のカード1枚につき、
自身の攻撃力8％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100108, 'ボス梨緒フュージョン +7', 'Attack', 'バトル後半で、【高瀬 梨緒】のカード1枚につき、
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100109, 'ボス梨緒フュージョン +8', 'Attack', 'バトル後半で、【高瀬 梨緒】のカード1枚につき、
自身の攻撃力8％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100110, 'あかりブースト +7', 'Boost', '【星咲 あかり】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100111, 'あかりブースト +9', 'Boost', '【星咲 あかり】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100112, '柚子ブースト +7', 'Boost', '【藤沢 柚子】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100113, '柚子ブースト +9', 'Boost', '【藤沢 柚子】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100114, '葵ブースト +7', 'Boost', '【三角 葵】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100115, '葵ブースト +9', 'Boost', '【三角 葵】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100116, '莉玖ブースト +7', 'Boost', '【結城 莉玖】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100117, '莉玖ブースト +9', 'Boost', '【結城 莉玖】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100118, '椿ブースト +7', 'Boost', '【藍原 椿】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100119, '椿ブースト +9', 'Boost', '【藍原 椿】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100120, '梨緒ブースト +7', 'Boost', '【高瀬 梨緒】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100121, '梨緒ブースト +9', 'Boost', '【高瀬 梨緒】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100122, '春菜ブースト +7', 'Boost', '【桜井 春菜】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100123, '春菜ブースト +9', 'Boost', '【桜井 春菜】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100124, '楓ブースト +7', 'Boost', '【九條 楓】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100125, '楓ブースト +9', 'Boost', '【九條 楓】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100126, '彩華ブースト +7', 'Boost', '【早乙女 彩華】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100127, '彩華ブースト +9', 'Boost', '【早乙女 彩華】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100128, '茜ブースト +7', 'Boost', '【逢坂 茜】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100129, '茜ブースト +9', 'Boost', '【逢坂 茜】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100130, '小星ブースト +7', 'Boost', '【井之原 小星】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100131, '小星ブースト +9', 'Boost', '【井之原 小星】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100132, '咲姫ブースト +7', 'Boost', '【柏木 咲姫】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100133, '咲姫ブースト +9', 'Boost', '【柏木 咲姫】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100134, '有栖ブースト +7', 'Boost', '【珠洲島 有栖】かつ【ATTACK】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100135, '有栖ブースト +9', 'Boost', '【珠洲島 有栖】かつ【ATTACK】の攻撃力9％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100136, 'まんたん茜フュージョン +5', 'Attack', 'ライフ100％時、【逢坂 茜】のカード1枚につき、
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100137, 'まんたん茜フュージョン +6', 'Attack', 'ライフ100％時、【逢坂 茜】のカード1枚につき、
自身の攻撃力6％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100138, 'ボスファイアブースト +18', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力18％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100139, 'ボスファイアブースト +20', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100140, 'ボスファイアブースト +10', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100141, 'ボスファイアブースト +12', 'Boost', 'バトル後半で、
属性【FIRE】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100142, 'SIDE-Lアシスト +5', 'Support', 'ノーツ【SIDE-L】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100143, 'SIDE-Lアシスト +7', 'Support', 'ノーツ【SIDE-L】を自動で攻撃する
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100144, 'アタック +13', 'Attack', '自身の攻撃力13％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (100145, 'アタック +15', 'Attack', '自身の攻撃力15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105000, 'ボスリーフブースト +18', 'Boost', 'バトル後半で、
属性【LEAF】かつ【ATTACK】の攻撃力18％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105001, 'ボスリーフブースト +20', 'Boost', 'バトル後半で、
属性【LEAF】かつ【ATTACK】の攻撃力20％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105002, 'ボス佐天&初春ブースト +10', 'Boost', 'バトル後半で、
【佐天 涙子】と【初春 飾利】の攻撃力10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105003, 'ボス佐天&初春ブースト +12', 'Boost', 'バトル後半で、
【佐天 涙子】と【初春 飾利】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105004, 'SIDE-LRアシスト +6', 'Support', 'ノーツ【SIDE】を自動で攻撃する
自身の攻撃力6％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105005, 'SIDE-LRアシスト +8', 'Support', 'ノーツ【SIDE】を自動で攻撃する
自身の攻撃力8％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105006, 'ボス美食殿ブースト +11', 'Boost', 'バトル後半で、
【ペコリーヌ】と【キャル】と【コッコロ】の攻撃力11％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105007, 'ボス美食殿ブースト +13', 'Boost', 'バトル後半で、
【ペコリーヌ】と【キャル】と【コッコロ】の攻撃力13％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105008, '全属性ガード +25', 'Guard', 'ダメージ25%軽減
自身の攻撃力5%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105009, '全属性ガード +30', 'Guard', 'ダメージ30%軽減
自身の攻撃力5%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105010, 'ノーダメボスアタック +17', 'Attack', 'バトル後半で、
ダメージカウントが0の時、自身の攻撃力17％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105011, 'ノーダメボスアタック +19', 'Attack', 'バトル後半で、
ダメージカウントが0の時、自身の攻撃力19％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105012, '八雲ブースト +11', 'Boost', '【橙】と【八雲 紫】の攻撃力11％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105013, '八雲ブースト +13', 'Boost', '【橙】と【八雲 紫】の攻撃力13％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105014, 'SIDE-Rアシスト +3', 'Support', 'ノーツ【SIDE-R】を自動で攻撃する
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105015, 'SIDE-Rアシスト +5', 'Support', 'ノーツ【SIDE-R】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105016, 'ボス美海フュージョン +5', 'Attack', 'バトル後半で、
【日向 美海】のカード1枚につき、自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105017, 'ボス美海フュージョン +6', 'Attack', 'バトル後半で、
【日向 美海】のカード1枚につき、自身の攻撃力6％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105018, '遥フュージョン+4', 'Attack', '【東条 遥】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105019, '遥フュージョン+5', 'Attack', '【東条 遥】のカード1枚につき、自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105020, 'アタック +14', 'Attack', '自身の攻撃力14%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105021, 'アタック +16', 'Attack', '自身の攻撃力16%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105022, 'SIDE-LR＆FLICKアシスト +14', 'Support', 'ノーツ【SIDE】【FLICK】を自動で攻撃する
自身の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105023, 'SIDE-LR＆FLICKアシスト +16', 'Support', 'ノーツ【SIDE】【FLICK】を自動で攻撃する
自身の攻撃力16％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105024, 'ボスリーフガード +35', 'Guard', 'バトル後半で、属性【LEAF】からのダメージ35％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105025, 'ボスリーフガード +40', 'Guard', 'バトル後半で、属性【LEAF】からのダメージ40％軽減
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105026, 'ボスアタック +9', 'Attack', 'バトル後半で、自身の攻撃力9%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105027, 'アタック +9', 'Attack', '自身の攻撃力9%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105028, 'まんたんアタック +16', 'Attack', 'ライフ100％時、自身の攻撃力16％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105029, 'まんたんアタック +18', 'Attack', 'ライフ100％時、自身の攻撃力18％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105030, 'まんたんボスアタック +22', 'Attack', 'バトル後半で、
ライフ100%時、自身の攻撃力22％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105031, 'まんたんボスアタック +24', 'Attack', 'バトル後半で、
ライフ100%時、自身の攻撃力24％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105032, '春菜フュージョン +5（ボスアタック +2）', 'Attack', '【桜井 春菜】のカード1枚につき、自身の攻撃力5％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105033, '春菜フュージョン +6（ボスアタック +2）', 'Attack', '【桜井 春菜】のカード1枚につき、自身の攻撃力6％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105034, '小星フュージョン +5（ボスアタック +2）', 'Attack', '【井之原 小星】のカード1枚につき、自身の攻撃力5％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105035, '小星フュージョン +6（ボスアタック +2）', 'Attack', '【井之原 小星】のカード1枚につき、自身の攻撃力6％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105036, '咲姫フュージョン +5（ボスアタック +2）', 'Attack', '【柏木 咲姫】のカード1枚につき、自身の攻撃力5％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105037, '咲姫フュージョン +6（ボスアタック +2）', 'Attack', '【柏木 咲姫】のカード1枚につき、自身の攻撃力6％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105038, '茜フュージョン +5（ボスアタック +2）', 'Attack', '【逢坂 茜】のカード1枚につき、自身の攻撃力5％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105039, '茜フュージョン +6（ボスアタック +2）', 'Attack', '【逢坂 茜】のカード1枚につき、自身の攻撃力6％アップ
バトル後半で自身の攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105040, 'ボスアタック +14 (ノーダメボスアタック +3)', 'Attack', 'バトル後半で、自身の攻撃力14％アップ
ダメージカウント0の時、追加で自身の攻撃力3%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105041, 'ボスアタック +16 (ノーダメボスアタック +3)', 'Attack', 'バトル後半で、自身の攻撃力16％アップ
ダメージカウント0の時、追加で自身の攻撃力3%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105042, 'まんたんアタック +12', 'Attack', 'ライフ100％時、自身の攻撃力12%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105043, 'まんたんアタック +14', 'Attack', 'ライフ100％時、自身の攻撃力14%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105044, 'アタック +13（危）', 'DangerAttack', '自身の攻撃力13%アップ
ミス1回につき3%ダメージ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105045, 'アタック +15（危）', 'DangerAttack', '自身の攻撃力15%アップ
ミス1回につき3%ダメージ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105046, 'ボス莉玖ブースト +12', 'Boost', 'バトル後半で、【結城 莉玖】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105047, 'ボス莉玖ブースト +14', 'Boost', 'バトル後半で、【結城 莉玖】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105048, 'ボス椿ブースト +12', 'Boost', 'バトル後半で、【藍原 椿】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105049, 'ボス椿ブースト +14', 'Boost', 'バトル後半で、【藍原 椿】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105050, 'ボス梨緒ブースト +12', 'Boost', 'バトル後半で、【高瀬 梨緒】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105051, 'ボス梨緒ブースト +14', 'Boost', 'バトル後半で、【高瀬 梨緒】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105052, 'まんたんアクアブースト +13', 'Boost', 'ライフ100％の時、
属性【AQUA】かつ【ATTACK】の攻撃13％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105053, 'まんたんアクアブースト +15', 'Boost', 'ライフ100％の時、
属性【AQUA】かつ【ATTACK】の攻撃15％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105054, 'アクアガード +30（ヒール +10）', 'Guard', '属性【AQUA】からのダメージ30％軽減
BELLの回復量10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105055, 'アクアガード +35（ヒール +10）', 'Guard', '属性【AQUA】からのダメージ35％軽減
BELLの回復量10％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105056, 'アタック +14（危）', 'DangerAttack', '自身の攻撃力14％アップ
被弾時のダメージが2倍になる');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105057, 'アタック +16（危）', 'DangerAttack', '自身の攻撃力16％アップ
被弾時のダメージが2倍になる');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105058, 'ボス有栖フュージョン +6', 'Attack', 'バトル後半で、【玖洲島 有栖】のカード1枚につき、
自身の攻撃力6％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105059, 'ボス有栖フュージョン +7', 'Attack', 'バトル後半で、【玖洲島 有栖】のカード1枚につき、
自身の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105060, 'SIDE-LRアシスト +3', 'Support', 'ノーツ【SIDE】を自動で攻撃する
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105061, 'SIDE-LRアシスト +5', 'Support', 'ノーツ【SIDE】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105062, 'ボス小星フュージョン +4', 'Attack', 'バトル後半で、【井之原 小星】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105063, 'ボス小星フュージョン +5', 'Attack', 'バトル後半で、【井之原 小星】のカード1枚につき、
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105064, 'ボス咲姫フュージョン +4', 'Attack', 'バトル後半で、【柏木 咲姫】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105065, 'ボス咲姫フュージョン +5', 'Attack', 'バトル後半で、【柏木 咲姫】のカード1枚につき、
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105066, 'ボス有栖フュージョン +4', 'Attack', 'バトル後半で、【珠洲島 有栖】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105067, 'ボス有栖フュージョン +5', 'Attack', 'バトル後半で、
【珠洲島 有栖】のカード1枚につき、自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105068, 'ノーダメボスアタック +16', 'Attack', 'バトル後半で、
ダメージカウント0の時、自身の攻撃力16%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105069, 'ノーダメボスアタック +18', 'Attack', 'バトル後半で、ダメージカウント0の時、
自身の攻撃力18%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105070, '春菜フュージョン +3', 'Attack', '【桜井 春菜】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105071, '春菜フュージョン +4', 'Attack', '【桜井 春菜】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105072, '彩華フュージョン +3', 'Attack', '【早乙女 彩華】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105073, '彩華フュージョン +4', 'Attack', '【早乙女 彩華】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105074, '小星フュージョン +3', 'Attack', '【井之原 小星】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105075, '小星フュージョン +4', 'Attack', '【井之原 小星】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105076, '咲姫フュージョン +3', 'Attack', '【柏木 咲姫】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105077, '咲姫フュージョン +4', 'Attack', '【柏木 咲姫】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105078, 'あかりブースト +12', 'Boost', '【星咲 あかり】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105079, 'あかりブースト +14', 'Boost', '【星咲 あかり】かつ【ATTACK】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105080, '柚子ブースト +12', 'Boost', '【藤沢 柚子】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105081, '柚子ブースト +14', 'Boost', '【藤沢 柚子】かつ【ATTACK】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105082, '葵ブースト +12', 'Boost', '【三角 葵】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105083, '葵ブースト +14', 'Boost', '【三角 葵】かつ【ATTACK】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105084, 'まんたんアタック +11', 'Attack', 'ライフ100％時、自身の攻撃力11%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105085, 'まんたんアタック +13', 'Attack', 'ライフ100％時、自身の攻撃力13%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105086, '春菜フュージョン +3（アタック +2）', 'Attack', '自身の攻撃力2%アップ
さらに、
【桜井 春菜】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105087, '春菜フュージョン +4（アタック +2）', 'Attack', '自身の攻撃力2%アップ
さらに、
【桜井 春菜】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105088, '彩華フュージョン +3（アタック +2）', 'Attack', '自身の攻撃力2%アップ
さらに、
【早乙女 彩華】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105089, '彩華フュージョン +4（アタック +2）', 'Attack', '自身の攻撃力2%アップ
さらに、
【早乙女 彩華】のカード1枚につき、自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105090, '小星ブースト +12', 'Boost', '【井之原 小星】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105091, '小星ブースト +14', 'Boost', '【井之原 小星】かつ【ATTACK】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105092, '咲姫ブースト +12', 'Boost', '【柏木 咲姫】かつ【ATTACK】の攻撃力12％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105093, '咲姫ブースト +14', 'Boost', '【柏木 咲姫】かつ【ATTACK】の攻撃力14％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105094, 'ファイアブースト +10（アタック +3）', 'Boost', '属性【FIRE】かつ【ATTACK】の攻撃力10％アップ
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105095, 'ファイアブースト +12（アタック +3）', 'Boost', '属性【FIRE】かつ【ATTACK】の攻撃力12％アップ
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105096, 'リーフブースト +10（アタック +3）', 'Boost', '属性【LEAF】かつ【ATTACK】の攻撃力10％アップ
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105097, 'リーフブースト +12（アタック +3）', 'Boost', '属性【LEAF】かつ【ATTACK】の攻撃力12％アップ
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105098, 'アクアブースト +10（アタック +3）', 'Boost', '属性【AQUA】かつ【ATTACK】の攻撃力10％アップ
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105099, 'アクアブースト +12（アタック +3）', 'Boost', '属性【AQUA】かつ【ATTACK】の攻撃力12％アップ
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105100, 'あかりフュージョン +2', 'Attack', '【星咲 あかり】のカード1枚につき、自身の攻撃力2％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105101, 'あかりフュージョン +3', 'Attack', '【星咲 あかり】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105102, '柚子フュージョン +2', 'Attack', '【藤沢 柚子】のカード1枚につき、自身の攻撃力2％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105103, '柚子フュージョン +3', 'Attack', '【藤沢 柚子】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105104, '葵フュージョン +2', 'Attack', '【三角 葵】のカード1枚につき、自身の攻撃力2％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105105, '葵フュージョン +3', 'Attack', '【三角 葵】のカード1枚につき、自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105106, '莉玖ブースト +4', 'Boost', '【結城 莉玖】の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105107, '莉玖ブースト +5', 'Boost', '【結城 莉玖】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105108, '椿ブースト +4', 'Boost', '【藍原 椿】の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105109, '椿ブースト +5', 'Boost', '【藍原 椿】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105110, '梨緒ブースト +4', 'Boost', '【高瀬 梨緒】の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105111, '梨緒ブースト +5', 'Boost', '【高瀬 梨緒】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105112, 'FLICKアシスト +3', 'Support', 'ノーツ【FLICK】を自動で攻撃する
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105113, 'FLICKアシスト +5', 'Support', 'ノーツ【FLICK】を自動で攻撃する
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105114, 'ローミスアタック +10', 'Attack', 'MISS数10以下の時、自身の攻撃力10%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105115, 'ローミスアタック +12', 'Attack', 'MISS数10以下の時、自身の攻撃力12%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105116, 'ボスブースト +15', 'Boost', 'バトル後半で、【ATTACK】全員の攻撃力15%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105117, 'ボスブースト +17', 'Boost', 'バトル後半で、【ATTACK】全員の攻撃力17%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105118, '春菜＆彩華フュージョン +3', 'Attack', '【桜井 春菜】か【早乙女 彩華】のカード1枚につき、
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105119, '春菜＆彩華フュージョン +4', 'Attack', '【桜井 春菜】か【早乙女 彩華】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105120, '彩華＆春菜フュージョン +3', 'Attack', '【早乙女 彩華】か【桜井 春菜】のカード1枚につき、
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105121, '彩華＆春菜フュージョン +4', 'Attack', '【早乙女 彩華】か【桜井 春菜】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105122, '小星＆咲姫フュージョン +3', 'Attack', '【井之原 小星】か【柏木 咲姫】のカード1枚につき、
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105123, '小星＆咲姫フュージョン +4', 'Attack', '【井之原 小星】か【柏木 咲姫】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105124, '咲姫＆小星フュージョン +3', 'Attack', '【柏木 咲姫】か【井之原 小星】のカード1枚につき、
自身の攻撃力3％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105125, '咲姫＆小星フュージョン +4', 'Attack', '【柏木 咲姫】か【井之原 小星】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105126, '楓＆有栖ブースト +5', 'Boost', '【九條 楓】と【珠洲島 有栖】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105127, '楓＆有栖ブースト +7', 'Boost', '【九條 楓】と【珠洲島 有栖】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105128, '茜＆有栖ブースト +5', 'Boost', '【逢坂 茜】と【珠洲島 有栖】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105129, '茜＆有栖ブースト +7', 'Boost', '【逢坂 茜】と【珠洲島 有栖】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105130, '楓＆茜ブースト +5', 'Boost', '【九條 楓】と【逢坂 茜】の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105131, '楓＆茜ブースト +7', 'Boost', '【九條 楓】と【逢坂 茜】の攻撃力7％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105132, 'クックックッ……', 'Attack', '自身の攻撃力400%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105133, 'クックックッ……', 'Attack', '自身の攻撃力401%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105134, '腕が鳴るにゃん', 'Attack', '自身の攻撃力400%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105135, '腕が鳴るにゃん', 'Attack', '自身の攻撃力401%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105136, '手を貸してやるにゃん', 'Attack', '自身の攻撃力400%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105137, '手を貸してやるにゃん', 'Attack', '自身の攻撃力401%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105138, 'ボス美琴ブースト +16', 'Boost', 'バトル後半で、
【御坂 美琴】かつ【ATTACK】の攻撃力16％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105139, 'ボス美琴ブースト +18', 'Boost', 'バトル後半で、
【御坂 美琴】かつ【ATTACK】の攻撃力18％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105140, '美琴フュージョン +4', 'Attack', '【御坂 美琴】のカード1枚につき、
自身の攻撃力4％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105141, '美琴フュージョン +5', 'Attack', '【御坂 美琴】のカード1枚につき、
自身の攻撃力5％アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105142, 'アタック +10（ローミスアタック +2）', 'Attack', '自身の攻撃力10％アップ
さらに、MISS数20以下の時、追加で攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105143, 'アタック +12（ローミスアタック +2）', 'Attack', '自身の攻撃力12％アップ
さらに、MISS数20以下の時、追加で攻撃力2%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105144, 'ボスブースト +16', 'Boost', 'バトル後半で、【ATTACK】全員の攻撃力16%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105145, 'ボスブースト +18', 'Boost', 'バトル後半で、【ATTACK】全員の攻撃力18%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105146, 'ボスアタック +16', 'Attack', 'バトル後半で、自身の攻撃力16%アップ');
INSERT INTO ongeki_game_skill (id, name, category, info) VALUES (105147, 'ボスアタック +19', 'Attack', 'バトル後半で、自身の攻撃力19%アップ');

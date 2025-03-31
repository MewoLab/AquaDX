# Delete duplicate rows

-- UserMusicDetailList
DELETE FROM ongeki_user_music_detail
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_music_detail
    GROUP BY user_id, music_id, level
);

-- UserCharacterList
DELETE FROM ongeki_user_character
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_character
    GROUP BY user_id, character_id
);

-- UserCardList
DELETE FROM ongeki_user_card
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_card
    GROUP BY user_id, card_id
);

-- UserDeckList
DELETE FROM ongeki_user_deck
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_deck
    GROUP BY user_id, deck_id
);

-- UserTrainingRoomList
DELETE FROM ongeki_user_training_room
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_training_room
    GROUP BY user_id, room_id
);

-- UserChapterList
DELETE FROM ongeki_user_chapter
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_chapter
    GROUP BY user_id, chapter_id
);

-- UserMemoryChapterList
DELETE FROM ongeki_user_memory_chapter
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_memory_chapter
    GROUP BY user_id, chapter_id
);

-- UserItemList
DELETE FROM ongeki_user_item
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_item
    GROUP BY user_id, item_kind, item_id
);

-- UserMusicItemList
DELETE FROM ongeki_user_music_item
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_music_item
    GROUP BY user_id, music_id
);

-- UserLoginBonusList
DELETE FROM ongeki_user_login_bonus
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_login_bonus
    GROUP BY user_id, bonus_id
);

-- UserEventPointList
DELETE FROM ongeki_user_event_point
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_event_point
    GROUP BY user_id, event_id
);

-- UserMissionPointList
DELETE FROM ongeki_user_mission_point
WHERE id NOT IN (
    SELECT MAX(id)
    FROM ongeki_user_mission_point
    GROUP BY user_id, event_id
);

-- UserMusicDetailList
ALTER TABLE ongeki_user_music_detail
    ADD UNIQUE KEY uniq_user_music_level (user_id, music_id, level);

-- UserCharacterList
ALTER TABLE ongeki_user_character
    ADD UNIQUE KEY uniq_user_character (user_id, character_id);

-- UserCardList
ALTER TABLE ongeki_user_card
    ADD UNIQUE KEY uniq_user_card (user_id, card_id);

-- UserDeckList
ALTER TABLE ongeki_user_deck
    ADD UNIQUE KEY uniq_user_deck (user_id, deck_id);

-- UserTrainingRoomList
ALTER TABLE ongeki_user_training_room
    ADD UNIQUE KEY uniq_user_room (user_id, room_id);

-- UserChapterList
ALTER TABLE ongeki_user_chapter
    ADD UNIQUE KEY uniq_user_chapter (user_id, chapter_id);

-- UserMemoryChapterList
ALTER TABLE ongeki_user_memory_chapter
    ADD UNIQUE KEY uniq_user_mem_chapter (user_id, chapter_id);

-- UserItemList
ALTER TABLE ongeki_user_item
    ADD UNIQUE KEY uniq_user_item (user_id, item_kind, item_id);

-- UserMusicItemList
ALTER TABLE ongeki_user_music_item
    ADD UNIQUE KEY uniq_user_music_item (user_id, music_id);

-- UserLoginBonusList
ALTER TABLE ongeki_user_login_bonus
    ADD UNIQUE KEY uniq_user_login_bonus (user_id, bonus_id);

-- UserEventPointList
ALTER TABLE ongeki_user_event_point
    ADD UNIQUE KEY uniq_user_event_point (user_id, event_id);

-- UserMissionPointList
ALTER TABLE ongeki_user_mission_point
    ADD UNIQUE KEY uniq_user_mission_point (user_id, event_id);

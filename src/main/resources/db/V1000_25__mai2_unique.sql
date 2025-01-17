# Delete duplicate rows
DELETE FROM maimai2_user_extend
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_extend
    GROUP BY user_id
);

DELETE FROM maimai2_user_option
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_option
    GROUP BY user_id
);

DELETE FROM maimai2_user_character
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_character
    GROUP BY user_id, character_id
);

DELETE FROM maimai2_user_map
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_map
    GROUP BY user_id, map_id
);

DELETE FROM maimai2_user_login_bonus
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_login_bonus
    GROUP BY user_id, bonus_id
);

DELETE FROM maimai2_user_udemae
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_udemae
    GROUP BY user_id
);

DELETE FROM maimai2_user_general_data
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_general_data
    GROUP BY user_id, property_key
);

DELETE FROM maimai2_user_item
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_item
    GROUP BY user_id, item_id, item_kind
);

DELETE FROM maimai2_user_music_detail
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_music_detail
    GROUP BY user_id, music_id, level
);

DELETE FROM maimai2_user_course
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_course
    GROUP BY user_id, course_id
);

DELETE FROM maimai2_user_friend_season_ranking
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_friend_season_ranking
    GROUP BY user_id, season_id
);

DELETE FROM maimai2_user_favorite
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_favorite
    GROUP BY user_id, item_kind
);

DELETE FROM maimai2_user_activity
WHERE id NOT IN (
    SELECT MAX(id)
    FROM maimai2_user_activity
    GROUP BY user_id, activity_id, kind
);

# Add unique constraint
ALTER TABLE maimai2_user_extend
    ADD CONSTRAINT unique_user_extend UNIQUE (user_id);

ALTER TABLE maimai2_user_option
    ADD CONSTRAINT unique_user_option UNIQUE (user_id);

ALTER TABLE maimai2_user_character
    ADD CONSTRAINT unique_user_character UNIQUE (user_id, character_id);

ALTER TABLE maimai2_user_map
    ADD CONSTRAINT unique_user_map UNIQUE (user_id, map_id);

ALTER TABLE maimai2_user_login_bonus
    ADD CONSTRAINT unique_user_login_bonus UNIQUE (user_id, bonus_id);

ALTER TABLE maimai2_user_udemae
    ADD CONSTRAINT unique_user_udemae UNIQUE (user_id);

ALTER TABLE maimai2_user_general_data
    ADD CONSTRAINT unique_user_general_data UNIQUE (user_id, property_key);

ALTER TABLE maimai2_user_item
    ADD CONSTRAINT unique_user_item UNIQUE (user_id, item_id, item_kind);

ALTER TABLE maimai2_user_music_detail
    ADD CONSTRAINT unique_user_music_detail UNIQUE (user_id, music_id, level);

ALTER TABLE maimai2_user_course
    ADD CONSTRAINT unique_user_course UNIQUE (user_id, course_id);

ALTER TABLE maimai2_user_friend_season_ranking
    ADD CONSTRAINT unique_user_friend_season_ranking UNIQUE (user_id, season_id);

ALTER TABLE maimai2_user_favorite
    ADD CONSTRAINT unique_user_favorite UNIQUE (user_id, item_kind);

ALTER TABLE maimai2_user_activity
    ADD CONSTRAINT unique_user_activity UNIQUE (user_id, activity_id, kind);

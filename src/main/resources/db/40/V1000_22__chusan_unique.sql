# Delete duplicate rows
DELETE FROM chusan_user_map_area
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_map_area
    GROUP BY user_id, map_area_id
);

DELETE FROM chusan_user_character
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_character
    GROUP BY user_id, character_id
);

DELETE FROM chusan_user_item
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_item
    GROUP BY user_id, item_id, item_kind
);

DELETE FROM chusan_user_music_detail
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_music_detail
    GROUP BY user_id, music_id, level
);

DELETE FROM chusan_user_activity
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_activity
    GROUP BY user_id, activity_id, kind
);

DELETE FROM chusan_user_charge
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_charge
    GROUP BY user_id, charge_id
);

DELETE FROM chusan_user_course
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_course
    GROUP BY user_id, course_id
);

DELETE FROM chusan_user_duel
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_duel
    GROUP BY user_id, duel_id
);

DELETE FROM chusan_user_general_data
WHERE id NOT IN (
    SELECT MAX(id)
    FROM chusan_user_general_data
    GROUP BY user_id, property_key
);

# Add unique constraint
ALTER TABLE chusan_user_map_area
    ADD CONSTRAINT unique_user_map_area UNIQUE (user_id, map_area_id);

ALTER TABLE chusan_user_character
    ADD CONSTRAINT unique_user_character UNIQUE (user_id, character_id);

ALTER TABLE chusan_user_item
    ADD CONSTRAINT unique_user_item UNIQUE (user_id, item_id, item_kind);

ALTER TABLE chusan_user_music_detail
    ADD CONSTRAINT unique_user_music_detail UNIQUE (user_id, music_id, level);

ALTER TABLE chusan_user_activity
    ADD CONSTRAINT unique_user_activity UNIQUE (user_id, activity_id, kind);

ALTER TABLE chusan_user_charge
    ADD CONSTRAINT unique_user_charge UNIQUE (user_id, charge_id);

ALTER TABLE chusan_user_course
    ADD CONSTRAINT unique_user_course UNIQUE (user_id, course_id);

ALTER TABLE chusan_user_duel
    ADD CONSTRAINT unique_user_duel UNIQUE (user_id, duel_id);

ALTER TABLE chusan_user_general_data
    ADD CONSTRAINT unique_user_general_data UNIQUE (user_id, property_key);

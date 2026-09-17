-- new field wall option field
ALTER TABLE ongeki_user_option
    ADD COLUMN field_wall INT NOT NULL DEFAULT 0;

-- varchar(255) not enough for event map storage
ALTER TABLE ongeki_user_event_map
    MODIFY COLUMN map_data TEXT NULL;

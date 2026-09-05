-- allow separate Technical Challenge records for each chart difficulty
-- drop the old constraint and dedupe with the new constraint just in case again
ALTER TABLE ongeki_user_event_music
    DROP INDEX UKo5PU3BgZeTKB8SNykq9U7xjfshp;

DELETE FROM ongeki_user_event_music
WHERE id NOT IN (
    SELECT id
    FROM (
        SELECT MAX(id) AS id
        FROM ongeki_user_event_music
        GROUP BY user_id, event_id, type, music_id, level
    ) AS rows_to_keep
);

ALTER TABLE ongeki_user_event_music
    ADD UNIQUE KEY uniq_ongeki_user_event_music (user_id, event_id, type, music_id, level);

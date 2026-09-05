-- dedupe before adding new constraint just in case
DELETE FROM ongeki_user_event_map
WHERE id NOT IN (
    SELECT id
    FROM (
        SELECT MAX(id) AS id
        FROM ongeki_user_event_map
        GROUP BY user_id, event_id, map_id
    ) AS rows_to_keep
);

ALTER TABLE ongeki_user_event_map
    ADD UNIQUE KEY uniq_ongeki_user_event_map (user_id, event_id, map_id);

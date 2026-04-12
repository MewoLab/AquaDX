-- Normalize stored keychip IDs to 15 digits by appending our suffix.
-- Existing IDs are historically 11 characters (A + 10 digits).
UPDATE user_keychip
SET keychip_id = CONCAT(keychip_id, '1337')
WHERE CHAR_LENGTH(keychip_id) = 11;

-- Add new unlock columns
ALTER TABLE aqua_game_options ADD COLUMN mai2_unlock_music BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN mai2_unlock_chara BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN mai2_unlock_chara_max_level BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN mai2_unlock_partners BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN mai2_unlock_collectables BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN mai2_unlock_tickets BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN wacca_unlock_music BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN wacca_unlock_plates BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN wacca_unlock_collectables BIT NOT NULL DEFAULT 0;
ALTER TABLE aqua_game_options ADD COLUMN wacca_unlock_tickets BIT NOT NULL DEFAULT 0;

-- Migrate data
UPDATE aqua_game_options SET
    mai2_unlock_music = unlock_music,
    mai2_unlock_chara = unlock_chara,
    mai2_unlock_chara_max_level = unlock_chara,
    mai2_unlock_partners = unlock_chara,
    mai2_unlock_collectables = unlock_collectables,
    mai2_unlock_tickets = unlock_tickets,
    wacca_unlock_music = unlock_music,
    wacca_unlock_plates = unlock_chara | unlock_collectables,
    wacca_unlock_collectables = unlock_collectables,
    wacca_unlock_tickets = unlock_tickets;

-- Drop old columns
ALTER TABLE aqua_game_options DROP COLUMN unlock_music;
ALTER TABLE aqua_game_options DROP COLUMN unlock_chara;
ALTER TABLE aqua_game_options DROP COLUMN unlock_collectables;
ALTER TABLE aqua_game_options DROP COLUMN unlock_tickets;

-- Create the user_keychip join table
CREATE TABLE user_keychip
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    au_id      BIGINT                NOT NULL,
    keychip_id VARCHAR(32)           NOT NULL,
    CONSTRAINT pk_user_keychip PRIMARY KEY (id)
);

ALTER TABLE user_keychip
    ADD CONSTRAINT uc_user_keychip_keychip_id UNIQUE (keychip_id);

ALTER TABLE user_keychip
    ADD CONSTRAINT fk_user_keychip_on_au FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id) ON DELETE CASCADE;

-- Migrate existing keychip values from aqua_net_user into the new table
INSERT INTO user_keychip (au_id, keychip_id)
SELECT au_id, keychip
FROM aqua_net_user
WHERE keychip IS NOT NULL;

-- Drop the old keychip column
ALTER TABLE aqua_net_user
    DROP CONSTRAINT uc_aqua_net_user_keychip;

ALTER TABLE aqua_net_user
    DROP COLUMN keychip;

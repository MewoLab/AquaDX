ALTER TABLE maimai2_user_detail
    ADD COLUMN point INT DEFAULT 0,
    ADD COLUMN total_point INT DEFAULT 0;

CREATE TABLE maimai2_user_intimate
(
    id                      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id                 BIGINT                NULL,
    partner_id              INT                   NOT NULL,
    intimate_level          INT                   NOT NULL,
    intimate_count_rewarded INT                   NOT NULL,
    CONSTRAINT fku_maimai2_user_intimate FOREIGN KEY (user_id) REFERENCES maimai2_user_detail (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_maimai2_user_intimate UNIQUE (user_id, partner_id)
);


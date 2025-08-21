CREATE TABLE chusan_user_regions
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                NULL,
    region_id INT NOT NULL,
    play_count INT NOT NULL DEFAULT 1,
    created VARCHAR(355),
    PRIMARY KEY (id),
    CONSTRAINT fk_chusanregions_on_chusan_user_Data FOREIGN KEY (user_id) REFERENCES chusan_user_data (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unq_chusanregions_on_region_user UNIQUE (user_id, region_id)
);

CREATE TABLE ongeki_user_regions
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                NULL,
    region_id INT NOT NULL,
    play_count INT NOT NULL DEFAULT 1,
    created VARCHAR(355),
    PRIMARY KEY (id),
    CONSTRAINT fk_ongekiregions_on_aqua_net_user FOREIGN KEY (user_id) REFERENCES aqua_net_user (au_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unq_ongekiregions_on_region_user UNIQUE (user_id, region_id)
);

CREATE TABLE maimai2_user_regions
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                NULL,
    region_id INT NOT NULL,
    play_count INT NOT NULL DEFAULT 1,
    created VARCHAR(355),
    PRIMARY KEY (id),
    CONSTRAINT fk_maimai2regions_on_user_Details FOREIGN KEY (user_id) REFERENCES maimai2_user_detail (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unq_maimai2regions_on_region_user UNIQUE (user_id, region_id)
);

ALTER TABLE aqua_net_user
ADD COLUMN region VARCHAR(2) NOT NULL DEFAULT '1';
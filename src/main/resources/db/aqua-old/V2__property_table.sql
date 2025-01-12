--
-- テーブルの構造 `property`
--

CREATE TABLE `property`
(
    `id`             bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `property_key`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `property_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Indexes for table `property`
--
ALTER TABLE `property`
    ADD UNIQUE KEY `UK_ekbti34ksu6o2sfms9yumvp4o` (`property_key`);
COMMIT;

INSERT INTO `property` (id, property_key, property_value)
VALUES (1, 'diva_news', 'Server Running                  No other news');
INSERT INTO `property` (id, property_key, property_value)
VALUES (2, 'diva_warning', 'Network Service Running');
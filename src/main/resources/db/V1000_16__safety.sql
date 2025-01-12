CREATE TABLE aqua_net_safety
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    content VARCHAR(255)          NOT NULL,
    safe    BIT(1)                NOT NULL,
    CONSTRAINT pk_aqua_net_safety PRIMARY KEY (id),
    CONSTRAINT uq_aqua_net_safety_content UNIQUE (content)
);
CREATE TABLE ongeki_user_trade_item (
    id            BIGINT auto_increment PRIMARY KEY,
    chapter_id    INT,
    trade_item_id INT,
    trade_count   INT,
    user_id       BIGINT,
    CONSTRAINT UKUApE4XTzAn4TknhsyHbMoxk2vxr
        UNIQUE (chapter_id, trade_item_id, user_id),
    constraint FKjK4YZyUoHo397H35roV4y5xCGPL
        foreign key (user_id) references ongeki_user_data (id));

CREATE TABLE ongeki_user_tech_event (
    id                       BIGINT auto_increment PRIMARY KEY,
    event_id                 INT,
    total_tech_score         INT,
    total_platinum_score     INT,
    tech_record_date         VARCHAR (255),
    is_ranking_rewarded      BIT,
    is_total_tech_new_record BIT,
    user_id                  BIGINT,
    CONSTRAINT UKMy7BYF82aAqh7gcbULDtNZQ3L9u
        UNIQUE (event_id, user_id),
    constraint FKeA3LffHizJP95rE8WLL4ANZZLfM
        foreign key (user_id) references ongeki_user_data (id));

CREATE TABLE ongeki_user_kop (
    id                       BIGINT auto_increment PRIMARY KEY,
    auth_key                 VARCHAR (255),
    kop_id                   INT,
    area_id                  INT,
    total_tech_score         INT,
    total_platinum_score     INT,
    tech_record_date         VARCHAR (255),
    is_total_tech_new_record BIT,
    user_id                  BIGINT,
    CONSTRAINT UKhB9obeVojvBcG7iShsgrEb29W9R
        UNIQUE (kop_id, area_id, user_id),
    constraint FKNeHq3QCaKu7ipBCDEtZQ3QFgvBv
        foreign key (user_id) references ongeki_user_data (id));

CREATE TABLE ongeki_user_event_music (
    id                 BIGINT auto_increment PRIMARY KEY,
    event_id           INT,
    type               INT,
    music_id           INT,
    level              INT,
    tech_score_max     INT,
    platinum_score_max INT,
    tech_record_date   VARCHAR (255),
    is_tech_new_record BIT,
    user_id            BIGINT,
    CONSTRAINT UKo5PU3BgZeTKB8SNykq9U7xjfshp
        UNIQUE (event_id, type, music_id, user_id),
    constraint FKYyerzsu49pHUHJNvN67w4SGQbB2
        foreign key (user_id) references ongeki_user_data (id));
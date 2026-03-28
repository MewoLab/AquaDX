-- maimai2 card maker support

CREATE TABLE maimai2_game_selling_card (
    id                BIGINT auto_increment PRIMARY KEY,
    card_id           INTEGER,
    start_date        DATETIME,
    end_date          DATETIME,
    notice_start_date DATETIME,
    notice_end_date   DATETIME
);

CREATE TABLE maimai2_user_card (
    id           BIGINT auto_increment PRIMARY KEY,
    card_id      INTEGER,
    card_type_id INTEGER,
    chara_id     INTEGER,
    map_id       INTEGER,
    start_date   VARCHAR(255),
    end_date     VARCHAR(255),
    user_id      BIGINT,
    constraint FKEraxz5HoWgfKLz8w63
        foreign key (user_id) references maimai2_user_detail (id)
);

CREATE TABLE maimai2_user_print_detail (
    id                BIGINT auto_increment PRIMARY KEY,
    order_id          BIGINT,
    print_number      INTEGER,
    print_date        VARCHAR(255),
    serial_id         VARCHAR(255),
    place_id          INTEGER,
    client_id         VARCHAR(255),
    printer_serial_id VARCHAR(255),
    user_card_id      BIGINT,
    card_rom_version  INTEGER,
    is_holograph      BOOLEAN,
    print_option1     BOOLEAN,
    print_option2     BOOLEAN,
    print_option3     BOOLEAN,
    print_option4     BOOLEAN,
    print_option5     BOOLEAN,
    print_option6     BOOLEAN,
    print_option7     BOOLEAN,
    print_option8     BOOLEAN,
    print_option9     BOOLEAN,
    print_option10    BOOLEAN,
    created           VARCHAR(255),
    user_id           BIGINT,
    constraint FKDjNkXby95DMyQ9RKem
        foreign key (user_card_id) references maimai2_user_card (id),
    constraint FKEnhDgski3cuxz8Z5jd
        foreign key (user_id) references maimai2_user_detail (id)
);

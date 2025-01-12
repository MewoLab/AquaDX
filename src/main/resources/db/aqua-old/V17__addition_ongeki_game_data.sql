create table ongeki_user_mission_point
(
    id       bigint auto_increment
        primary key,
    event_id int    not null,
    point    bigint not null,
    user_id  bigint null,
    constraint FK867xgd2c5g6ic4k1tbd32hhqb
        foreign key (user_id) references ongeki_user_data (id)
);

create table ongeki_game_point
(
    id         bigint       not null
        primary key,
    type       int          not null,
    cost       int          not null,
    start_date varchar(255) not null,
    end_date   varchar(255) not null
);

create table ongeki_game_present
(
    id           bigint       not null
        primary key,
    present_name varchar(255) not null,
    reward_id    int          not null,
    stock        int          not null,
    message      varchar(255) not null,
    start_date   varchar(255) not null,
    end_date     varchar(255) not null
);

create table ongeki_game_reward
(
    id        bigint not null
        primary key,
    item_kind int    not null,
    item_id   int    not null
);

create table ongeki_user_general_data
(
    id             bigint auto_increment
        primary key,
    property_key   varchar(255) not null,
    property_value text         not null,
    user_id        bigint       null,
    constraint UK2upnsa6nothlfrqewa4tma62d
        unique (user_id, property_key),
    constraint FKj1v48ag7iyelf1va5hbcv63uj
        foreign key (user_id) references ongeki_user_data (id)
);

INSERT INTO ongeki_game_point (id, type, cost, start_date, end_date)
VALUES (1, 0, 100, '2000-01-01 05:00:00.0', '2099-01-01 05:00:00.0');
INSERT INTO ongeki_game_point (id, type, cost, start_date, end_date)
VALUES (2, 1, 200, '2000-01-01 05:00:00.0', '2099-01-01 05:00:00.0');
INSERT INTO ongeki_game_point (id, type, cost, start_date, end_date)
VALUES (3, 2, 300, '2000-01-01 05:00:00.0', '2099-01-01 05:00:00.0');
INSERT INTO ongeki_game_point (id, type, cost, start_date, end_date)
VALUES (4, 3, 333, '2000-01-01 05:00:00.0', '2099-01-01 05:00:00.0');
INSERT INTO ongeki_game_point (id, type, cost, start_date, end_date)
VALUES (5, 4, 666, '2000-01-01 05:00:00.0', '2099-01-01 05:00:00.0');
INSERT INTO ongeki_game_point (id, type, cost, start_date, end_date)
VALUES (6, 5, 999, '2000-01-01 05:00:00.0', '2099-01-01 05:00:00.0');

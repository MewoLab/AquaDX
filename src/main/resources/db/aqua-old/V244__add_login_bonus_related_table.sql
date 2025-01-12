create table chusan_game_login_bonus
(
    id                        bigint       not null
        primary key,
    version                   int          not null,
    preset_id                 int          not null,
    login_bonus_id            int          not null,
    login_bonus_name          varchar(255) not null,
    present_id                int          not null,
    present_name              varchar(255) not null,
    item_num                  int          not null,
    need_login_day_count      int          not null,
    login_bonus_category_type int          not null
)   ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    collate = utf8mb4_unicode_ci;

create table chusan_game_login_bonus_preset
(
    id          bigint        not null
        primary key,
    version     int           not null,
    preset_name varchar(255)  not null,
    is_enabled  int default 1 not null
)   ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    collate = utf8mb4_unicode_ci;

create table chusan_user_login_bonus
(
    id               bigint auto_increment primary key,
    user             bigint                                 not null,
    version          int                                    not null,
    preset_id        int                                    not null,
    bonus_count      int      default 0                     not null,
    last_update_date datetime default '2018-01-01 00:00:00' not null,
    is_watched       int      default 0                     not null,
    is_finished      int      default 0                     not null
)   ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    collate = utf8mb4_unicode_ci;
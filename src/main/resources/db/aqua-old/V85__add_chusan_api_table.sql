CREATE TABLE chusan_music (
    music_id        INTEGER PRIMARY KEY,
    artist_name     VARCHAR (255),
    genre           INTEGER,
    name            VARCHAR (255),
    release_version VARCHAR (255),
    sort_name       VARCHAR (255)

);

CREATE TABLE chusan_music_level (
    id            INTEGER auto_increment PRIMARY KEY,
    diff          INTEGER NOT NULL,
    enable        BOOLEAN NOT NULL,
    level         INTEGER NOT NULL,
    level_decimal INTEGER NOT NULL,
    music_id      INTEGER
);

CREATE TABLE chusan_game_character (
    id               INTEGER PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    release_tag      VARCHAR(255) NOT NULL,
    works_name       VARCHAR(255),
    illustrator_name VARCHAR(255),
    add_images       VARCHAR(255) NOT NULL

);

CREATE TABLE chusan_trophy (
    id   INTEGER PRIMARY KEY,
    name VARCHAR (255)
);

CREATE TABLE chusan_nameplate (
    id   INTEGER PRIMARY KEY,
    name VARCHAR (255)
);

CREATE TABLE chusan_voice (
    id   INTEGER PRIMARY KEY,
    name VARCHAR (255)
);

CREATE TABLE chusan_mapicon (
    id   INTEGER PRIMARY KEY,
    name VARCHAR (255)
);

CREATE TABLE chusan_frame (
    id   INTEGER PRIMARY KEY,
    name VARCHAR (255)
);

CREATE TABLE chusan_avatar (
    id   INTEGER PRIMARY KEY,
    name VARCHAR (255),
    category   INTEGER
);
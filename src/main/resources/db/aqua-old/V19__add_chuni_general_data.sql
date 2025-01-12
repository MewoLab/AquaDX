create table chuni_user_general_data
(
    id             bigint auto_increment
        primary key,
    property_key   varchar(255) not null,
    property_value text         not null,
    user_id        bigint       null,
    constraint UK2up23a6n1ghlf5gewa4tm5f2d
        unique (user_id, property_key),
    constraint FK2dkb8agh5ye1f15g5hbcv63uj
        foreign key (user_id) references chuni_user_data (id)
);
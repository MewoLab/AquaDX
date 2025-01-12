create table maimai2_user_general_data
(
    id             bigint auto_increment
        primary key,
    property_key   varchar(255) not null,
    property_value text         not null,
    user_id        bigint       null,
    constraint UKiPb2EMwbXzqTUfMun6Y4AeAKx
        unique (user_id, property_key),
    constraint FK6BJbvEH8Z22SLbwWKxsb7uarS
        foreign key (user_id) references maimai2_user_detail (id)
);
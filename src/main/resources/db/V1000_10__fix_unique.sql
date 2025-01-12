# maimai2
delete from maimai2_user_detail where aime_card_id is null;
alter table maimai2_user_detail modify aime_card_id bigint not null;
DELETE m
FROM maimai2_user_detail m
         JOIN (
    SELECT MAX(id) as max_id, aime_card_id
    FROM maimai2_user_detail
    GROUP BY aime_card_id
    HAVING COUNT(*) > 1
) d ON m.aime_card_id = d.aime_card_id
WHERE m.id < d.max_id;
alter table maimai2_user_detail
    add constraint maimai2_user_detail_pk unique (aime_card_id);

# maimai
delete from maimai_user_data where aime_card_id is null;
alter table maimai_user_data modify aime_card_id bigint not null;
DELETE m
FROM maimai_user_data m
         JOIN (
    SELECT MAX(id) as max_id, aime_card_id
    FROM maimai_user_data
    GROUP BY aime_card_id
    HAVING COUNT(*) > 1
) d ON m.aime_card_id = d.aime_card_id
WHERE m.id < d.max_id;
alter table maimai_user_data
    add constraint maimai_user_data_pk unique (aime_card_id);

# chusan
delete from chusan_user_data where card_id is null;
alter table chusan_user_data modify card_id bigint not null;
DELETE m
FROM chusan_user_data m
         JOIN (
    SELECT MAX(id) as max_id, card_id
    FROM chusan_user_data
    GROUP BY card_id
    HAVING COUNT(*) > 1
) d ON m.card_id = d.card_id
WHERE m.id < d.max_id;
alter table chusan_user_data
    add constraint chusan_user_data_pk unique (card_id);

# chunithm
delete from chuni_user_data where card_id is null;
alter table chuni_user_data modify card_id bigint not null;
DELETE m
FROM chuni_user_data m
         JOIN (
    SELECT MAX(id) as max_id, card_id
    FROM chuni_user_data
    GROUP BY card_id
    HAVING COUNT(*) > 1
) d ON m.card_id = d.card_id
WHERE m.id < d.max_id;
alter table chuni_user_data
    add constraint chuni_user_data_pk unique (card_id);

# ongeki
delete from ongeki_user_data where aime_card_id is null;
alter table ongeki_user_data modify aime_card_id bigint not null;
DELETE m
FROM ongeki_user_data m
         JOIN (
    SELECT MAX(id) as max_id, aime_card_id
    FROM ongeki_user_data
    GROUP BY aime_card_id
    HAVING COUNT(*) > 1
) d ON m.aime_card_id = d.aime_card_id
WHERE m.id < d.max_id;
alter table ongeki_user_data
    add constraint ongeki_user_data_pk unique (aime_card_id);

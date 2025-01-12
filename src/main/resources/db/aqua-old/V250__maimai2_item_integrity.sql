# Fixes crashes due to duplicate keys in maimai2_user_item
alter table maimai2_user_item
    add constraint maimai2_user_item_pk
        unique (item_kind, item_id, user_id);

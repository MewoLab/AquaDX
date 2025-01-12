alter table chusan_user_misc drop foreign key FK_CHUSANUSERMISC_ON_USER;
alter table chusan_user_misc add constraint fku_chusan_user_misc
    foreign key (user_id) references chusan_user_data (id)
        on delete cascade on update cascade;

alter table chusan_net_battle_log drop foreign key FK_CHUSAN_NET_BATTLE_LOG_ON_USER;
alter table chusan_net_battle_log add constraint fku_chusan_net_battle_log
    foreign key (user_id) references chusan_user_data (id)
        on delete cascade on update cascade;


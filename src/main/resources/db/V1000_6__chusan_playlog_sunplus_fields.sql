ALTER TABLE chusan_user_playlog
    ADD machine_type INT NOT NULL DEFAULT 0;

ALTER TABLE chusan_user_playlog
    ADD region_id INT NOT NULL DEFAULT 0;

ALTER TABLE chusan_user_playlog
    ADD ticket_id INT NOT NULL DEFAULT 0;

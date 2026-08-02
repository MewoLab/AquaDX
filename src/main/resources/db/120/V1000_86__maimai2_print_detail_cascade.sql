-- V1000.7 accidentally replaced the user foreign key with a second user-card
-- foreign key. Restore the direct ownership link so every print detail is
-- removed with its Mai2 user, including legacy rows without a user card.
DELETE detail FROM maimai2_user_print_detail detail
    LEFT JOIN maimai2_user_detail user_data ON detail.user_id = user_data.id
    WHERE user_data.id IS NULL;

ALTER TABLE maimai2_user_print_detail
    ADD CONSTRAINT fku_maimai2_user_print_detail_user
        FOREIGN KEY (user_id) REFERENCES maimai2_user_detail (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

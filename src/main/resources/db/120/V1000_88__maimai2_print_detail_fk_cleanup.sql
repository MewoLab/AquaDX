-- V1000.7 left two foreign keys on user_card_id. The older restrictive key
-- prevents the newer cascading relationship from deleting print details.
ALTER TABLE maimai2_user_print_detail
    DROP FOREIGN KEY IF EXISTS FKDjNkXby95DMyQ9RKem;

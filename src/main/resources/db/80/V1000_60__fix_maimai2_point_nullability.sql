# Fix NULL values in maimai2_user_detail point and total_point fields
# These fields were added without NOT NULL constraint in V1000_36

# First, set NULL values to 0
UPDATE maimai2_user_detail SET point = 0 WHERE point IS NULL;
UPDATE maimai2_user_detail SET total_point = 0 WHERE total_point IS NULL;

# Then add NOT NULL constraint
ALTER TABLE maimai2_user_detail MODIFY point INT NOT NULL DEFAULT 0;
ALTER TABLE maimai2_user_detail MODIFY total_point INT NOT NULL DEFAULT 0;


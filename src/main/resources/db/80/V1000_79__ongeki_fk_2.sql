ALTER TABLE ongeki_user_regions
DROP FOREIGN KEY IF EXISTS fk_ongekiregions_on_ongeki_user_data;

ALTER TABLE ongeki_user_regions
DROP FOREIGN KEY IF EXISTS fk_ongekiregions_on_aqua_net_user;

ALTER TABLE ongeki_user_regions
    ADD CONSTRAINT fk_ongekiregions_on_ongeki_user_data
        FOREIGN KEY (user_id) REFERENCES ongeki_user_data (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

DROP VIEW IF EXISTS ongeki_user_data_view;
    CREATE VIEW ongeki_user_data_view AS
    SELECT * FROM ongeki_user_data;
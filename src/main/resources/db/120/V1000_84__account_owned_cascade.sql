-- Account-owned rows disappear with the account. Physical cards are only unlinked.
ALTER TABLE aqua_net_session
    DROP FOREIGN KEY IF EXISTS FK_SESSION;
ALTER TABLE aqua_net_session
    ADD CONSTRAINT FK_SESSION
        FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE aqua_net_email_confirmation
    DROP FOREIGN KEY IF EXISTS FK_EMAIL_CONFIRMATION_ON_AQUA_USER;
ALTER TABLE aqua_net_email_confirmation
    ADD CONSTRAINT FK_EMAIL_CONFIRMATION_ON_AQUA_USER
        FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE aqua_net_email_reset_password
    DROP FOREIGN KEY IF EXISTS FK_EMAIL_RESET_PASSWORD_ON_AQUA_USER;
ALTER TABLE aqua_net_email_reset_password
    ADD CONSTRAINT FK_EMAIL_RESET_PASSWORD_ON_AQUA_USER
        FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE allnet_keychip_sessions
    DROP FOREIGN KEY IF EXISTS FK_ALLNET_KEYCHIP_SESSIONS_ON_AU;
ALTER TABLE allnet_keychip_sessions
    ADD CONSTRAINT FK_ALLNET_KEYCHIP_SESSIONS_ON_AU
        FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE user_keychip
    DROP FOREIGN KEY IF EXISTS fk_user_keychip_on_au;
ALTER TABLE user_keychip
    ADD CONSTRAINT fk_user_keychip_on_au
        FOREIGN KEY (au_id) REFERENCES aqua_net_user (au_id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE sega_card
    DROP FOREIGN KEY IF EXISTS FK_SEGA_CARD_ON_NET_USER;
ALTER TABLE sega_card
    ADD CONSTRAINT FK_SEGA_CARD_ON_NET_USER
        FOREIGN KEY (net_user_id) REFERENCES aqua_net_user (au_id)
            ON DELETE SET NULL ON UPDATE CASCADE;

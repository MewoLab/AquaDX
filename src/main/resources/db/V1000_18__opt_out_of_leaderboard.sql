-- If the column does not exist, add it
ALTER TABLE `aqua_net_user`
    ADD COLUMN IF NOT EXISTS `opt_out_of_leaderboard` bit NOT NULL DEFAULT b'0';


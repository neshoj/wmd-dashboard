set foreign_key_checks = 0;

--- Update Virtual Stations Transactions
ALTER TABLE `virtual_stations_transactions`
            ADD COLUMN  `front_plate_binary_image`   mediumblob  null;

set foreign_key_checks = 1;
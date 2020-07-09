set foreign_key_checks = 0;

-- Update Virtual Stations Transactions, add front and detail image views
ALTER TABLE `virtual_stations_transactions`
            ADD COLUMN  `detail_image_back`    mediumblob  null,
            ADD COLUMN  `back_plate_binary_image`      mediumblob null;

set foreign_key_checks = 1;
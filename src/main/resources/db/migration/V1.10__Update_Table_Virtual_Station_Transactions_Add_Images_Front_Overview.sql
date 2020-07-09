set foreign_key_checks = 0;

-- Update Virtual Stations Transactions, add front and detail image views
ALTER TABLE `virtual_stations_transactions`
            ADD COLUMN  `lp_image_front`    mediumblob  null,
            ADD COLUMN  `detail_image`      mediumblob null;

set foreign_key_checks = 1;
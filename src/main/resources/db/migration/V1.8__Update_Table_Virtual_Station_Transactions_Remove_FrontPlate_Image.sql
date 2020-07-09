set foreign_key_checks = 0;

-- Update Virtual Stations Transactions, remove all image views
ALTER TABLE `virtual_stations_transactions`
DROP COLUMN front_plate_binary_image;

set foreign_key_checks = 1;
set foreign_key_checks = 0;

--- Update Virtual Stations Transactions, remove all image views
ALTER TABLE `virtual_stations_transactions`
DROP COLUMN overview_image,
DROP COLUMN detail_image_back,
DROP COLUMN detail_image,
DROP COLUMN back_plate_binary_image;

set foreign_key_checks = 1;
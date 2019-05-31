set foreign_key_checks = 0;

--- Update Virtual Stations Transactions, remove all image views
ALTER TABLE `virtual_stations_transactions`
CHANGE `limit`  `weight_limit` decimal (11, 2) default 0;

set foreign_key_checks = 1;
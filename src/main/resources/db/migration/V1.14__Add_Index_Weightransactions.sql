set foreign_key_checks = 0;

# Speed up transactions with non unique index
ALTER TABLE `weighing_transactions` ADD INDEX(`status`);
ALTER TABLE `weighing_transactions` ADD INDEX(`axle_configuration`);
ALTER TABLE `weighing_transactions` ADD INDEX(`transaction_date`);
ALTER TABLE `weighing_transactions` ADD INDEX(`flag`);

ALTER TABLE `users` ADD INDEX(`email`);

ALTER TABLE `tagging_transactions` ADD INDEX(`tag_status`);


set foreign_key_checks = 1;
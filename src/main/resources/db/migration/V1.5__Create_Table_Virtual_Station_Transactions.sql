set foreign_key_checks = 0;

--- Virtual Stations Transactions Table
drop table if exists `virtual_stations_transactions`;
create table `virtual_stations_transactions` (
  `id`                              int(11) not null auto_increment,
  `virtual_station_transaction_id`  int(11) not null,
  `datetime`                        timestamp null default current_timestamp,
  `front_plate`                     varchar(45) default 'N\A',
  `back_plate`                      varchar(45) default 'N\A',
  `virtual_station_id`              varchar(45) default 'N\A',
  `virtual_station`                 varchar(150) default 'N\A',
  `axles_count`                     int(2) default 0,
  `total_weight`                    decimal (11, 2) default 0,
  `limit`                           decimal (11, 2) default 0,
  `percentage_overload`             decimal (11, 2) default 0,
  `axle_configuration`              varchar(35) default 'N\A',
  `axle_description`                varchar(150) default 'N\A',
  `velocity`                        varchar(50) default 'N\A',
  `length`                          varchar(10) default 'N\A',
  `flag`                            varchar(30) default '0',
  `wim_detector_id`                 varchar(10) default 'N\A',
  `first_axle_load`                 decimal (11, 2) default 0,
  `second_axle_load`                decimal (11, 2) default 0,
  `third_axle_load`                 decimal (11, 2) default 0,
  `fourth_axle_load`                decimal (11, 2) default 0,
  `fifth_axle_load`                 decimal (11, 2) default 0,
  `sixth_axle_load`                 decimal (11, 2) default 0,
  `seventh_axle_load`               decimal (11, 2) default 0,
  `eighth_axle_load`                decimal (11, 2) default 0,
  `ninth_axle_load`                 decimal (11, 2) default 0,
  `back_plate_binary_image`         mediumblob  null,
  `detail_image`                    mediumblob null,
  `detail_image_back`               mediumblob null,
  `overview_image`                  mediumblob null,
  `created_on`                      timestamp null default current_timestamp,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------
-- Weighbridge Transaction Table
-- --------------------------------
CREATE INDEX virtual_station_transaction_id
ON virtual_stations_transactions (virtual_station_transaction_id);


set foreign_key_checks = 1;
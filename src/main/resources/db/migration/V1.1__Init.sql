set foreign_key_checks = 0;

--- HSWIM Data Table
drop table if exists `hswim_transactions`;
create table `hswim_transactions` (
  `id`                int(11)         not     null auto_increment,
  `ticket_no`         varchar(100)    default null,
  `station_name`      varchar(150)    default null,
  `vehicle_speed`     varchar(50)     default null,
  `vehicle_length`    varchar(100)    default null,
  `axle_count`        int(2)        default null,
  `axle_one`          decimal (11, 2) default 0,
  `axle_two`          decimal (11, 2) default 0,
  `axle_three`        decimal (11, 2) default 0,
  `axle_four`         decimal (11, 2) default 0,
  `axle_five`         decimal (11, 2) default 0,
  `axle_six`          decimal (11, 2) default 0,
  `axle_seven`        decimal (11, 2) default 0,
  `transaction_date`  timestamp null  default current_timestamp,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


set foreign_key_checks = 1;
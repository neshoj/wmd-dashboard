set foreign_key_checks = 0;

-- --------------------------------
-- Axle Configurations
-- --------------------------------
drop table if exists `axle_configuration`;
create table `axle_classifications` (
  `id`          int(11)             not null auto_increment,
  `axle_code`   varchar(10)        default null,
  `axle_one`    decimal (11, 2)     default 0,
  `axle_two`    decimal (11, 2)     default 0,
  `axle_three`  decimal (11, 2)     default 0,
  `axle_four`   decimal (11, 2)     default 0,
  `axle_five`   decimal (11, 2)     default 0,
  `axle_six`    decimal (11, 2)     default 0,
  `axle_seven`  decimal (11, 2)     default 0,
  `axle_count`  int(2)              default 2,

  `edit_data`          longtext,
  `reason_code_no`     int(11)               default null,
  `reason_description` varchar(200)          default null,
  `created_on`  timestamp null      default current_timestamp,
  `updated_on`  timestamp null      default current_timestamp,
  `created_by`  int(11)             default null,
  `updated_by`  int(11)             default null,
  `flag`        varchar(20)         default '1',
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ---------------------------------
--  Update transaction table
-- ---------------------------------
ALTER TABLE `weighing_transactions`
      ADD COLUMN  `first_axle_weight_exceeded_percent`    decimal (11, 2) default 0,
      ADD COLUMN  `first_axle_weight_exceeded_weight`     decimal (11, 2) default 0,

      ADD COLUMN  `second_axle_weight_exceeded_percent`   decimal (11, 2) default 0,
      ADD COLUMN  `second_axle_weight_exceeded_weight`    decimal (11, 2) default 0,

      ADD COLUMN  `third_axle_weight_exceeded_percent`    decimal (11, 2) default 0,
      ADD COLUMN  `third_axle_weight_exceeded_weight`     decimal (11, 2) default 0,

      ADD COLUMN  `fourth_axle_weight_exceeded_percent`   decimal (11, 2) default 0,
      ADD COLUMN  `fourth_axle_weight_exceeded_weight`    decimal (11, 2) default 0,

      ADD COLUMN  `fifth_axle_weight_exceeded_percent`    decimal (11, 2) default 0,
      ADD COLUMN  `fifth_axle_weight_exceeded_weight`     decimal (11, 2) default 0,

      ADD COLUMN  `sixth_axle_weight_exceeded_percent`    decimal (11, 2) default 0,
      ADD COLUMN  `sixth_axle_weight_exceeded_weight`     decimal (11, 2) default 0,

      ADD COLUMN  `seventh_axle_weight_exceeded_percent`  decimal (11, 2) default 0,
      ADD COLUMN  `seventh_axle_weight_exceeded_weight`   decimal (11, 2) default 0,

      ADD COLUMN  `gvw_exceeded_percent`                  decimal (11, 2) default 0,
      ADD COLUMN  `gvw_exceeded_weight`                   decimal (11, 2) default 0,
      ADD COLUMN  `flag`                                  int(2) default 0,
      ADD COLUMN  `axle_weight_status`                    int(2) default 0,
      ADD COLUMN  `status`                                int(2) default 0;


-- Within=0 WithinTolerance=1  Above=2 ;
set foreign_key_checks = 1;
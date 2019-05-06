set foreign_key_checks = 0;

-- --------------------------------
-- Tag Clearance Report Table
-- --------------------------------
drop table if exists `tag_clearances_report`;
create table `tag_clearances_report` (
  `id`          int(11)             not null auto_increment,
  `tag_reference`           varchar(150) not null,
  `reference_no`           varchar(150) not null,

  `tagging_transactions_no`          int(11) not null,

  `narration` varchar(200)          default null,
  `created_on`  timestamp null      default current_timestamp,
  `created_by`  int(11)             default null,

  PRIMARY KEY (`id`),
   KEY `tagging_transactions_no` (`tagging_transactions_no`),
  CONSTRAINT `tag_clearances_report_fk_1` FOREIGN KEY (`tagging_transactions_no`) REFERENCES `tagging_transactions` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

set foreign_key_checks = 1;
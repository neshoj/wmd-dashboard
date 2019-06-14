set foreign_key_checks = 0;

-- Add Roles
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES
(11,   'Mobile Weighing', 'ROLE_MOBILE_WEIGHING', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin, aea-admin, mobile-operators');
-- Add Roles`

-- Add Permission
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES
(68, 'Can add mobile weigh transactions', 'can-add-mobile-weigh-transactions', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 11),
(69, 'Can add mobile weigh arrest transactions', 'can-add-mobile-weigh-arrest-transactions', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 11);
-- Add Permission

--- Add user type mobile-operators
insert into `user_types` (`id`, `name`, `code`)
values
  (7, 'Mobile Operators', 'mobile-operators');
--- Add user type mobile-operators

--  Police Officer details table
drop table if exists police_officer_details;
create table police_officer_details (
  `id`                  int(11) not null auto_increment,
  `first_name`          varchar(100)    not null,
  `last_name`           varchar(100)    not null,
  `gender`              int(1)          not null,
  `police_no`           varchar(100)    not null,

  `flag`                varchar(20)     default '0',
  `edit_data`           longtext,
  `reason_code_no`      int(11)         default null,
  `reason_description`  varchar(200)    default null,
  `created_on`          timestamp null  default current_timestamp,
  `updated_on`          timestamp null  default current_timestamp,
  `created_by`          int(11)         default null,
  `updated_by`          int(11)         default null,
  PRIMARY KEY (id),
    KEY `created_by` (`created_by`),
    KEY `updated_by` (`updated_by`),
    KEY `reason_code_no` (`reason_code_no`),
    CONSTRAINT `police_officer_details_fk_1` FOREIGN KEY (`created_by`)  REFERENCES `users` (`id`),
    CONSTRAINT `police_officer_details_fk_2` FOREIGN KEY (`updated_by`)  REFERENCES `users` (`id`),
    CONSTRAINT `police_officer_details_fk_3` FOREIGN KEY (`reason_code_no`) REFERENCES `reason_codes` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
--   Police Officer detail table

--  Mobile weigh Arrest data
drop table if exists transactions_mobile_weigh_arrest;
create table transactions_mobile_weigh_arrest (
  `id`                  int(11) not null auto_increment,
  `transaction_date`    timestamp null  default current_timestamp,
  `vehicle_no`          varchar(40)    not null,
  `transporter`         varchar(150)    not null,
  `excess_gvw`          decimal(10,2)   default 0,
  `excess_axle`         decimal(10,2)   default 0,
  `cargo`               varchar(100)    not null,
  `origin`              varchar(100)    not null,
  `destination`         varchar(100)    not null,
  `action`              varchar(250)    not null,

  `created_on`          timestamp null  default current_timestamp,
  `created_by`          int(11)         default null,
  PRIMARY KEY (id),
    KEY `created_by` (`created_by`),
    CONSTRAINT `transactions_mobile_weigh_arrest_fk_1` FOREIGN KEY (`created_by`)  REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
--  Mobile weigh Arrest data


--  Mobile weigh Prosecution data
drop table if exists transactions_mobile_weigh_prosecution;
create table transactions_mobile_weigh_prosecution (
  `id`              int(11) not null auto_increment,
  `transporter`     varchar(150)    not null,
  `axle_class`      varchar(40)    not null,
  `excess_gvw`      decimal(10,2)   default 0,
  `excess_axle`     decimal(10,2)   default 0,
  `vehicle_no`      varchar(40)    not null,
  `prohibition_no`  varchar(100)    not null,
  `expected_amount` decimal(10,2)   default 0,
  `actual_amount`   decimal(10,2)   default 0,
  `receipt_no`      varchar(100)    not null,
  `payment_date`    timestamp null  default current_timestamp,
  `cargo`           varchar(100)    not null,
  `origin`          varchar(100)    not null,
  `destination`     varchar(100)    not null,
  `officer`         int(11) not null ,

  `created_on`      timestamp null  default current_timestamp,
  `created_by`      int(11)         default null,
  PRIMARY KEY (id),
    KEY `created_by` (`created_by`),
    KEY `officer` (`officer`),
    CONSTRAINT `transactions_mobile_weigh_prosecution_fk_1` FOREIGN KEY (`created_by`)  REFERENCES `users` (`id`),
    CONSTRAINT `transactions_mobile_weigh_prosecution_fk_2` FOREIGN KEY (`officer`)     REFERENCES `police_officer_details` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
--  Mobile weigh Prosecution data


set foreign_key_checks = 1;
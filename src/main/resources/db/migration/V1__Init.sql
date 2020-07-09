set foreign_key_checks = 0;

-- Application Roles Table
drop table if exists `app_roles`;
create table `app_roles` (
  `id`           int(11)   not null auto_increment,
  `name`         varchar(100)       default null,
  `app_code`     varchar(100)       default null,
  `created_at`   timestamp null     default current_timestamp,
  `updated_at`   timestamp null     default current_timestamp,
  `app_function` varchar(200)       default null,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;

INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (1,   'Weighbridge Stations', 'ROLE_WEIGHBRIDGE_STATION', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin,kenha-admin, aea-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (2,   'Clients', 'ROLE_CLIENTS', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (3,   'User Groups', 'ROLE_USER_GROUP', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin,kenha-admin, aea-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (4,   'Users', 'ROLE_USERS', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin,kenha-admin, aea-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (5,   'Vehicle Tags', 'ROLE_VEHICLE_TAGS', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin, aea-admin, aea-weighbridge-manager');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (6,   'Vehicle Configuration', 'ROLE_VEHICLE_CONFIGURATION', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (7,   'Demerit Points', 'ROLE_DEMERIT_POINTS', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (8,   'Penalties', 'ROLE_PENALTIES', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (9,   'Reports', 'ROLE_REPORTS', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin, aea-admin, aea-weighbridge-manager,aea-operations-manager');
INSERT INTO app_roles (id, name, app_code, created_at, updated_at, app_function) VALUES (10,   'Audit Logs', 'ROLE_AUDIT_LOGS', '2019-02-22 10:15:02', '2019-02-22 10:15:02', 'super-admin, kenha-admin, aea-admin');

-- App Settings Table
drop table if exists `app_settings`;
create table `app_settings` (
  `id`          int(11) not null auto_increment,
  `name`        varchar(150)     default null,
  `code`        varchar(150)     default null,
  `flag`        varchar(100)     default null,
  `value`       varchar(100)     default null,
  `description` varchar(100)     default null,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

insert into `app_settings` (`id`, `name`, `code`, `flag`, `value`, `description`)
values
(1, 'Login Trials', 'LOGIN_TRIALS', 'settings', '5', 'INT of allowed login attempts'),
(2, 'Lock out period', 'LOCK_OUT_PERIOD', 'settings', '30','Period(minutes) in which a locked account will be inaccessible'),
(3, 'Reset code expiry period', 'RESET_EXPIRY', 'settings', '2','Period in hours in which a reset password token is valid'),
(4, 'Axle Weight Tolerance', 'AXLE_WEIGHT_TOLERANCE', 'settings', '5','A percentage tolerance is allowed on the permissible axle weights and the permissible axle group');

-- Audit Trail Table
drop table if exists `audit_trail`;
create table `audit_trail` (
  `id`         int(11)   not null auto_increment,
  `activity`   longtext,
  `status`     varchar(45)        default null,
  `created_on` timestamp null     default current_timestamp,
  `old_values` longtext,
  `new_values` longtext,
  `log_type`   varchar(100)       default null,
  `user_no`    int(11)            default null,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Reason Codes Table
drop table if exists `reason_codes`;
create table `reason_codes` (
  `id`          int(11)   not null auto_increment,
  `name`        varchar(100)       default null,
  `description` varchar(200)       default null,
  `created_on`  timestamp null     default current_timestamp,
  `updated_on`  timestamp null     default current_timestamp,
  `created_by`  int(11)            default null,
  `updated_by`  int(11)            default null,
  `flag`        varchar(20)        default '1',
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Maker Checker Table
drop table if exists `maker_checker`;
create table `maker_checker` (
  `id`      int(11) not null default '0',
  `module`  varchar(100)     default null,
  `code`    varchar(100)     default null,
  `enabled` tinyint(1)       default '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `maker_checker` (`id`, `module`, `code`, `enabled`)
VALUES
      ('1', 'WeighbridgeStations', 'Weighbridge Stations', true),
      ('2', 'VehicleTags', 'Vehicle Tags', true),
      ('3', 'VehicleConfiguration', 'Vehicle Configuration', true),
      ('4', 'DemeritPoints', 'Demerit Points', true),
      ('5', 'Penalties', 'Penalties', true),
      ('6', 'UserGroups', 'User Groups', true),
      ('7', 'Users', 'Users', true);


-- User Login Attempts Table
drop table if exists `user_attempts`;
create table `user_attempts` (
  `id`            int(11) not null auto_increment,
  `email`         varchar(150)     default null,
  `attempts`      int(11)          default null,
  `last_modified` timestamp null     default current_timestamp,
  `ip`            varchar(100)     default null,
  `user_agent`    text             default null,
  `country`       varchar(100)     default null,
  `region`        varchar(100)     default null,
  `city`          varchar(100)     default null,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*
* Permissions Table
*/
drop table if exists `permissions`;
create table `permissions` (
  `id`         int(11)   not null auto_increment,
  `name`       varchar(100)       default null,
  `app_code`   varchar(100)       default null,
  `created_on` timestamp null     default current_timestamp,
  `updated_on` timestamp null     default current_timestamp,
  `role_no`    int(11)            default null,
  PRIMARY KEY (`ID`),
  KEY `role_no` (`role_no`),
  CONSTRAINT `permissions_fk_1` FOREIGN KEY (`role_no`) REFERENCES `app_roles` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (1,  'View Weighbridge Stations', 'default', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (2,  'View Active Weighbridge Stations', 'view-active', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (3,  'View New Weighbridge Stations', 'view-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (4,  'View Edited Weighbridge Stations', 'view-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (5,  'View Deactivated Weighbridge Stations', 'view-deactivated', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (6,  'View Inactive Weighbridge Stations', 'view-inactive', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (7,  'Create Weighbridge Stations', 'new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (8,  'Edit Weighbridge Stations', 'edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (9,  'Approve New  Weighbridge Stations', 'approve-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (10, 'Decline New Weighbridge Stations', 'decline-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (11, 'Approve Edited Weighbridge Stations', 'approve-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (12, 'Decline Edited Weighbridge Stations', 'decline-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (13, 'Approve Deactivated Weighbridge Stations', 'approve-deactivation', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (14, 'Decline Deactivated Weighbridge Stations', 'decline-deactivation', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (15, 'Delete Weighbridge Stations', 'delete', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (16, 'Activate Weighbridge Stations', 'activate', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 1);

INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (17, 'View UserGroups', 'default', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (18, 'View Active UserGroups', 'view-active', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (19, 'View New UserGroups', 'view-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (20, 'View Edited UserGroups', 'view-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (21, 'View Deactivated UserGroups', 'view-deactivated', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (22, 'View Inactive UserGroups', 'view-inactive', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (23, 'Create UserGroups', 'new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (24, 'Edit UserGroups', 'edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (25, 'Approve New  UserGroups', 'approve-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (26, 'Decline New UserGroups', 'decline-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (27, 'Approve Edited UserGroups', 'approve-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (28, 'Decline Edited UserGroups', 'decline-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (29, 'Approve Deactivated UserGroups', 'approve-deactivation', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (30, 'Decline Deactivated UserGroups', 'decline-deactivation', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (31, 'Delete UserGroups', 'delete', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (32, 'Activate UserGroups', 'activate', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 2);

INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (33, 'View Users', 'default', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (34, 'View Active Users', 'view-active', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (35, 'View New Users', 'view-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (36, 'View Edited Users', 'view-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (37, 'View Deactivated Users', 'view-deactivated', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (38, 'View Inactive Users', 'view-inactive', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (39, 'Create Users', 'new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (40, 'Edit Users', 'edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (41, 'Approve New  Users', 'approve-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (42, 'Decline New Users', 'decline-new', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (43, 'Approve Edited Users', 'approve-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (44, 'Decline Edited Users', 'decline-edit', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (45, 'Approve Deactivated Users', 'approve-deactivation', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (46, 'Decline Deactivated Users', 'decline-deactivation', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (47, 'Delete Users', 'delete', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (48, 'Activate Users', 'activate', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 3);

INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (49, 'View Clients', 'default', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (50, 'View Active Clients', 'view-active', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (51, 'View New Clients', 'view-new', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (52, 'View Edited Clients', 'view-edit', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (53, 'View Deactivated Clients', 'view-deactivated', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (54, 'View Inactive Clients', 'view-inactive', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (55, 'Create Clients', 'new', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (56, 'Edit Clients', 'edit', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (57, 'Approve New  Clients', 'approve-new', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (58, 'Decline New Clients', 'decline-new', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (59, 'Approve Edited Clients', 'approve-edit', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (60, 'Decline Edited Clients', 'decline-edit', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (61, 'Approve Deactivated Clients', 'approve-deactivation', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (62, 'Decline Deactivated Clients', 'decline-deactivation', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (63, 'Delete Clients', 'delete', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (64, 'Activate Clients', 'activate', '2019-02-01 02:20:20', '2019-02-01 02:20:20', 2);

INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (65, 'Can view transactions', 'activate', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 6);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (66, 'Can view reports', 'activate', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 7);
INSERT INTO permissions (id, name, app_code, created_on, updated_on, role_no) VALUES (67, 'Can view audit logs', 'activate', '2019-03-01 02:30:30', '2019-03-01 02:30:30', 8);

-- Group Permissions Table
drop table if exists `group_permissions`;
create table `group_permissions` (
  `permission_id` int(11) not null,
  `user_group_id` int(11) not null,
  PRIMARY KEY (`permission_id`, `user_group_id`),
  KEY `user_group_id` (`user_group_id`),
  CONSTRAINT `group_permissions_fk_1` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`ID`),
  CONSTRAINT `group_permissions_fk_2` FOREIGN KEY (`user_group_id`) REFERENCES `user_groups` (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- User Groups Table
drop table if exists `user_groups`;
create table `user_groups` (
  `id`                 int(11)   not null auto_increment,
  `name`               varchar(100)       default null,
  `description`        varchar(250)       default null,
  `base_type`          varchar(100)       default null,
  `system_defined`     bit(1)             default 0,

  `flag`               varchar(20)        default '0',
  `edit_data`          longtext,
  `reason_code_no`     int(11)            default null,
  `reason_description` varchar(200)       default null,
  `created_on`         timestamp null     default current_timestamp,
  `updated_on`         timestamp null     default current_timestamp,
  `deleted_on`         timestamp null     default null,
  `created_by`         int(11)            default null,
  `updated_by`         int(11)            default null,

  PRIMARY KEY (`id`),
  KEY `reason_code_no` (`reason_code_no`),
  CONSTRAINT `user_groups_fk_1` FOREIGN KEY (`reason_code_no`) REFERENCES `reason_codes` (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into `user_groups` (`name`, `description`, `flag`, `base_type`, `system_defined`) values ('Super Admin Group', 'Super Administrators Group', 1, 'super-admin', 1);

-- User Types Table
DROP TABLE IF EXISTS `user_types`;
CREATE TABLE `user_types` (
  `id`   int(11) not null auto_increment,
  `name` varchar(150)      default null,
  `code` varchar(100)     default null,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

insert into `user_types` (`id`, `name`, `code`)
values
  (1, 'Super Admin', 'super-admin'),
  (2, 'KENHA Admin', 'kenha-admin'),
  (3, 'KENHA Axle Load Control Officer', 'kenha-axle-load-control-officer'),
  (4, 'AEA Admin', 'aea-admin'),
  (5, 'AEA Weighbridge Manager', 'aea-weighbridge-manager'),
  (6, 'AEA Operations Manager', 'aea-operations-manager');


-- Users Table
drop table if exists `users`;
create table `users` (
  `id`                         int(11)   not null auto_increment,
  `first_name`                 varchar(100)       default null,
  `surname`                    varchar(100)       default null,
  `email`                      varchar(250)       default null,
  `phone`                      varchar(20)        default null,
  `password`                   varchar(100)       default null,
  `photo_url`                  longtext,
  `photo_key`                  varchar(200)       default null,
  `expiry`                     timestamp null     default null,
  `enabled`                    tinyint(4)         default '1',
  `non_locked`                  tinyint(4)         default '0',
  `created_on`                 timestamp null     default current_timestamp,
  `updated_on`                 timestamp null     default current_timestamp,
  `deleted_on`                 timestamp null     default null,
  `created_by`                 int(11)            default null,
  `updated_by`                 int(11)            default null,
  `mobile_token`               varchar(200)       default null,
  `email_token`                varchar(200)       default null,
  `fcm_token`                  varchar(600)       default null,
  `reset_key`                  varchar(200)       default null,
  `mobile_verified`            tinyint(4)         default '0',
  `email_verified`             tinyint(4)         default '0',
  `flag`                       varchar(20)        default '0',
  `user_type_no`               int(11)            default null,
  `user_group_no`              int(11)            default null,
  `edit_data`                  longtext,
  `reason_code_no`             int(11)            default null,
  `reason_description`         varchar(200)       default null,
  `last_time_password_updated` timestamp null     default null,
  `password_never_expires`     tinyint(4)         default '0',
  `reset_req_date`             timestamp null     default null,
  `activation_key`             varchar(200)       default null,
  PRIMARY KEY (`id`),
  KEY `user_type_no` (`user_type_no`),
  KEY `user_group_no` (`user_group_no`),
  KEY `reason_code_no` (`reason_code_no`),
  CONSTRAINT `users_fk_1` FOREIGN KEY (`user_type_no`) REFERENCES `user_types` (`id`),
  CONSTRAINT `users_fk_2` FOREIGN KEY (`user_group_no`) REFERENCES `user_groups` (`id`),
  CONSTRAINT `users_fk_3` FOREIGN KEY (`reason_code_no`) REFERENCES `reason_codes` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into `users` (`id`, `first_name`, `surname`, `email`, `phone`, `password`, `enabled`, `non_locked`, `mobile_verified`, `email_verified`, `flag`, `user_type_no`, `user_group_no`, `password_never_expires`)
values
(1, 'one', 'point-seven', 'info@one-point-seven.com', '+254706151591', '$2a$10$OB4s5782LhCGjpzfmxa97e85NwR.yBTSjOfbBd5Bl98I2frrwiwaK', 1, 0, 1, 1, '1', 1, 1, 1),
(2, 'AEA', 'Limited', 'avery@averyafrica.com', '+254724259815', '$2a$10$OB4s5782LhCGjpzfmxa97e85NwR.yBTSjOfbBd5Bl98I2frrwiwaK', 1, 0, 1, 1, '1', 1, 1, 1),
(3, 'Dg', 'Kenha', 'dg@kenha.co.ke', '+2542989000', '$2a$10$OB4s5782LhCGjpzfmxa97e85NwR.yBTSjOfbBd5Bl98I2frrwiwaK', 1, 0, 1, 1, '1', 1, 1, 1);

-- =====================================================================================================================
-- =========================        Clients         ====================================================================
-- =====================================================================================================================

-- clients Table
drop table if exists clients;
create table clients (
  `id`                 int(11)      not null auto_increment,
  `name`               varchar(100) not null,
  `location`           varchar(100) not null,

  `contact_first_name` varchar(100)          default null,
  `contact_surname`   varchar(100)          default null,
  `contact_phone`      varchar(20)           default null,
  `contact_email`      text                  default null,

  `flag`               varchar(20)           default '0', -- MakerChecker Flag
  `edit_data`          longtext,
  `reason_code_no`     int(11)               default null,
  `reason_description` varchar(200)          default null,
  `created_by`         int(11)               default null,
  `updated_by`         int(11)               default null,
  `created_on`         timestamp    null     default current_timestamp,
  `updated_on`         timestamp    null     default current_timestamp,
  `deleted_on`         timestamp    null     default null,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `clients` (`name`, `location`, `contact_first_name`, `contact_surname`, `contact_phone`, `contact_email`, `flag`, `created_by`, `updated_by`, `created_on`, `updated_on`) VALUES ('AEA', 'Nairobi', 'Avery', 'Support', '+254724 259 815', 'avery@averyafrica.com', '1', '1', '1', '2019-02-22 10:15:02', '2019-02-22 10:15:02');
INSERT INTO `clients` (`name`, `location`, `contact_first_name`, `contact_surname`, `contact_phone`, `contact_email`, `flag`, `created_by`, `updated_by`, `created_on`, `updated_on`) VALUES ('KENHA', 'Nairobi', 'KENHA', 'Support', '+254-020-2729200', 'info@kenha.co.ke', '1', '1', '1', '2019-02-22 10:15:02', '2019-02-22 10:15:02');


-- Client Users Table
drop table if exists client_users;
create table client_users (
  `id`          int(11) not null,
  `client_no` int(11) not null,

  PRIMARY KEY (id),
  KEY `client_no` (`client_no`),
  CONSTRAINT `clients_users_fk_1` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  CONSTRAINT `clients_users_fk_2` FOREIGN KEY (`client_no`) REFERENCES `clients` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Clients User Groups Table
drop table if exists clients_groups;
create table clients_groups (
  `id`          int(11) not null,
  `client_no` int(11) not null,

  PRIMARY KEY (id),
  KEY `client_no` (`client_no`),
  CONSTRAINT `clients_groups_fk_1` FOREIGN KEY (`id`) REFERENCES `user_groups` (`id`),
  CONSTRAINT `clients_groups_fk_2` FOREIGN KEY (`client_no`) REFERENCES `client_no` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- =====================================================================================================================
-- =========================        Weighbridge Stations         =======================================================
-- =====================================================================================================================
drop table if exists `weighbridge_types`;
create table `weighbridge_types` (
  `id`                    int(11)   not null auto_increment,
  `name`                  varchar(100)       default null,
  `description`           varchar(100)       default null,

  `flag`                 varchar(20)        default '0',
  `edit_data`            longtext,
  `reason_code_no`       int(11)            default null,
  `reason_description`   varchar(200)       default null,
  `created_on`           timestamp null     default current_timestamp,
  `updated_on`           timestamp null     default current_timestamp,
  `deleted_on`           timestamp null     default null,
  `created_by`           int(11)            default null,
  `updated_by`           int(11)            default null,
  PRIMARY KEY (`id`),
  KEY `reason_code_no` (`reason_code_no`),
  CONSTRAINT `weighbridge_types_fk_1` FOREIGN KEY (`reason_code_no`) REFERENCES `reason_codes` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `weighbridge_types` (`id`, `name`, `description`)
VALUES
      ('1', 'Single Axle Weigher', 'Measure each axle independently'),
      ('2', 'Group Axle Weigher', 'Measure axle groups independently'),
      ('3', 'Multi Deck Axle Weigher', 'Measure vehicle gross weight once');

drop table if exists `weighbridge_stations`;
create table `weighbridge_stations` (
  `id`                  int(11)       not null auto_increment,
  `name`                varchar(100)  not null,
  `station_code`        varchar(200)  not null,
  `location`            varchar(200)  not null,
  `mobile_no`           varchar(20)   not null,

  `weighbridge_types_no`  int(11)       not null,

  `created_on`          timestamp null     default current_timestamp,
  `updated_on`          timestamp null     default current_timestamp,
  `deleted_on`          timestamp null     default null,
  `created_by`          int(11)            default null,
  `updated_by`          int(11)            default null,
  `flag`                varchar(20)        default '0',
  `reason_code_no`      int(11)            default null,
  `reason_description`  varchar(200)       default null,
  `edit_data`           longtext,

  PRIMARY KEY (`id`),
  KEY `reason_code_no` (`reason_code_no`),
  KEY `weighbridge_types_no` (`weighbridge_types_no`),
  CONSTRAINT `weighbridge_stations_fk_1` FOREIGN KEY (`reason_code_no`) REFERENCES `reason_codes` (`id`),
  CONSTRAINT `weighbridge_stations_fk_2` FOREIGN KEY (`weighbridge_types_no`) REFERENCES `weighbridge_types` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- weighbridge stations Users Table
drop table if exists weighbridge_stations_users;
create table weighbridge_stations_users (
  `id`                      int(11) not null,
  `weighbridge_stations_no` int(11) not null,

  PRIMARY KEY (id),
  KEY `weighbridge_stations_no` (`weighbridge_stations_no`),
  CONSTRAINT `weighbridge_stations_users_fk_1` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  CONSTRAINT `weighbridge_stations_users_fk_2` FOREIGN KEY (`weighbridge_stations_no`) REFERENCES `weighbridge_stations` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- weighbridge_stations User Groups Table
drop table if exists weighbridge_stations_groups;
create table weighbridge_stations_groups (
  `id`                      int(11) not null,
  `weighbridge_stations_no` int(11) not null,

  PRIMARY KEY (id),
  KEY `weighbridge_stations_no` (`weighbridge_stations_no`),
  CONSTRAINT `weighbridge_stations_groups_fk_1` FOREIGN KEY (`id`) REFERENCES `user_groups` (`id`),
  CONSTRAINT `weighbridge_stations_groups_fk_2` FOREIGN KEY (`weighbridge_stations_no`) REFERENCES `weighbridge_stations` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- =====================================================================================================================
-- =========================        Vehicle Tags         ===============================================================
-- =====================================================================================================================

--  Transactions table
drop table if exists vehicle_tags;
create table vehicle_tags (
  `id`                      int(11) not null AUTO_INCREMENT,
  `tag_id`                  varchar(100) not null,
  `tag_vehicle`             varchar(50) not null,
  `tag_reason`              longtext,
  `tag_date_time`           timestamp null  default current_timestamp,
  `tag_transaction_id`      varchar (100) not null,
  `tagging_system_used`     varchar (150) not null,
  `station_of_tagging`      varchar(150) default null,
  `tag_correct_vehicle_no`  varchar(50) default null,
  `tag_type`                varchar(50) default null,
  `weigh_bridge_name`       varchar(50) default null,
  `tag_removed`             int(3) not null,
  `tag_charge_amount`       decimal (11, 2) default 0,
  `flag`                    varchar(20)           default '0',

  `weighbridge_no`          int(11) not null,

  `created_on`              timestamp null     default current_timestamp,
   PRIMARY KEY (id),
   KEY `weighbridge_no` (`weighbridge_no`),
  CONSTRAINT `vehicle_tags_fk_1` FOREIGN KEY (`weighbridge_no`) REFERENCES `weighbridge_stations` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- =====================================================================================================================
-- =========================        Weighing Transactions         ======================================================
-- =====================================================================================================================

--  Weighing Transactions table
drop table if exists weighing_transactions;
create table weighing_transactions (
  `id`                          int(11) not null AUTO_INCREMENT,
  `ticket_no`                   varchar(100) not null,
  `station_code`                varchar(100) not null,
  `transaction_date`            timestamp null,
  `vehicle_no`                  varchar(100) default '-',
  `axle_configuration`          varchar(100) default '-',

  `first_axle_weight`           decimal (11, 2) default 0,
  `first_axle_legal_weight`     decimal (11, 2) default 0,
  `first_axle_type`             varchar(20) not null,
  `first_axle_grouping`         varchar(20) not null,

  `second_axle_weight`          decimal (11, 2) default 0,
  `second_axle_legal_weight`    decimal (11, 2) default 0,
  `second_axle_type`            varchar(20) not null,
  `second_axle_grouping`        varchar(20) not null,

  `third_axle_weight`           decimal (11, 2) default 0,
  `third_axle_legal_weight`     decimal (11, 2) default 0,
  `third_axle_type`             varchar(20) default '-',
  `third_axle_grouping`         varchar(20) default '-',

  `fourth_axle_weight`          decimal (11, 2) default 0,
  `fourth_axle_legal_weight`    decimal (11, 2) default 0,
  `fourth_axle_type`            varchar(20) default '-',
  `fourth_axle_grouping`        varchar(20) default '-',

  `fifth_axle_weight`           decimal (11, 2) default 0,
  `fifth_axle_legal_weight`     decimal (11, 2) default 0,
  `fifth_axle_type`             varchar(20) default '-',
  `fifth_axle_grouping`         varchar(20) default '-',

  `sixth_axle_weight`           decimal (11, 2) default 0,
  `sixth_axle_legal_weight`     decimal (11, 2) default 0,
  `sixth_axle_type`             varchar(20) default '-',
  `sixth_axle_grouping`         varchar(20) default '-',

  `seventh_axle_weight`         decimal (11, 2) default 0,
  `seventh_axle_legal_weight`   decimal (11, 2) default 0,
  `seventh_axle_type`           varchar(20) default '-',
  `seventh_axle_grouping`       varchar(20) default '-',

  `vehicleGVM`                  decimal (11, 2) default 0,
  `operator`                    varchar(100) default '-',
  `wbt_status`                  varchar(20) default '-',
  `wbt_direction`               varchar(11) default '-',
  `wbt_bu`                      varchar(11) default '-',
  `wbt_shift`                   varchar(20) default '-',

  `origin`                      varchar(150) default '-',
  `destination`                 varchar(150) default '-',
  `cargo`                       varchar(150)  default '-',
  `action_taken`                varchar(250) default '-',
  `permit_no`                   varchar(50) default '-',
  `created_on`                  timestamp null default current_timestamp,

  `weighbridge_no`              int(11) not null,

   PRIMARY KEY (id),
   KEY `weighbridge_no` (`weighbridge_no`),
  CONSTRAINT `weighing_transactions_fk_1` FOREIGN KEY (`weighbridge_no`) REFERENCES `weighbridge_stations` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--  Tagging Transactions table
drop table if exists tagging_transactions;
create table tagging_transactions (
  `id`                      int(11) not null AUTO_INCREMENT,
  `tag_reference`           varchar(150) not null,
  `vehicle_no`              varchar(50) not null,
  `transgression`           varchar(250) not null,
  `transaction_date`        timestamp null,
  `weighing_reference`      varchar(100) default '-',
  `tagging_system`          varchar(100) default '-',
  `tagging_scene`           varchar(100) default '-',
  `tag_status`              varchar(10) default '-',
  `confirmed_vehicle_no`    varchar(50) default '-',
  `tag_on_charge_amount`    decimal (11, 2) default 0,
  `tag_type`                varchar(20) default '-',
  `weighbridge`             varchar(150) default '-',
  `charged_reason`          varchar(255) default '-',
  `evidence_reference`      varchar(20) default '-',
  `evidence_id`             varchar(20) default '-',
  `created_on`              timestamp null default current_timestamp,

  `weighbridge_no`          int(11) not null,

   PRIMARY KEY (id),
   KEY `weighbridge_no` (`weighbridge_no`),
  CONSTRAINT `tagging_transactions_fk_1` FOREIGN KEY (`weighbridge_no`) REFERENCES `weighbridge_stations` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


set foreign_key_checks = 0;
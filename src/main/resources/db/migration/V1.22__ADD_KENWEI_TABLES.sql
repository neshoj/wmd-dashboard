set foreign_key_checks = 0;

#====================================================================
#   Refactor Weigh transaction table
#====================================================================
ALTER TABLE `weighing_transactions`
    DROP COLUMN `first_axle_type`,
    DROP COLUMN `first_axle_grouping`,
    DROP COLUMN `second_axle_type`,
    DROP COLUMN `second_axle_grouping`,
    DROP COLUMN `third_axle_type`,
    DROP COLUMN `third_axle_grouping`,
    DROP COLUMN `fourth_axle_type`,
    DROP COLUMN `fourth_axle_grouping`,
    DROP COLUMN `fifth_axle_type`,
    DROP COLUMN `fifth_axle_grouping`,
    DROP COLUMN `sixth_axle_type`,
    DROP COLUMN `sixth_axle_grouping`,
    DROP COLUMN `seventh_axle_type`,
    DROP COLUMN `seventh_axle_grouping`,
    DROP COLUMN `wbt_status`,
    DROP COLUMN `wbt_direction`,
    DROP COLUMN `wbt_bu`,
    DROP COLUMN `wbt_shift`;

#====================================================================
#   Add KENWEI temp transaction table
#====================================================================
CREATE TABLE `kenwei_temp_weighing_transactions`
(
    `id`                                   INT(11)        NOT NULL AUTO_INCREMENT,
    `ticket_no`                            VARCHAR(100)   NOT NULL,
    `station_code`                         VARCHAR(100)   NOT NULL,
    `transaction_date`                     TIMESTAMP      NULL DEFAULT NULL,
    `vehicle_no`                           VARCHAR(100)   NULL DEFAULT '-',
    `axle_configuration`                   VARCHAR(100)   NULL DEFAULT '-',

    `first_axle_weight`                    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `first_axle_legal_weight`              DECIMAL(11, 2) NULL DEFAULT '0.00',

    `second_axle_weight`                   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `second_axle_legal_weight`             DECIMAL(11, 2) NULL DEFAULT '0.00',

    `third_axle_weight`                    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `third_axle_legal_weight`              DECIMAL(11, 2) NULL DEFAULT '0.00',

    `fourth_axle_weight`                   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `fourth_axle_legal_weight`             DECIMAL(11, 2) NULL DEFAULT '0.00',

    `fifth_axle_weight`                    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `fifth_axle_legal_weight`              DECIMAL(11, 2) NULL DEFAULT '0.00',

    `sixth_axle_weight`                    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `sixth_axle_legal_weight`              DECIMAL(11, 2) NULL DEFAULT '0.00',

    `seventh_axle_weight`                  DECIMAL(11, 2) NULL DEFAULT '0.00',
    `seventh_axle_legal_weight`            DECIMAL(11, 2) NULL DEFAULT '0.00',

    `gross_vehicle_mass`                   DECIMAL(11, 2) NULL DEFAULT '0.00',

    `operator`                             VARCHAR(100)   NULL DEFAULT 'Kenwei Operator',
    `origin`                               VARCHAR(150)   NULL DEFAULT '-',
    `destination`                          VARCHAR(150)   NULL DEFAULT '-',
    `cargo`                                VARCHAR(150)   NULL DEFAULT '-',
    `action_taken`                         VARCHAR(250)   NULL DEFAULT '-',
    `created_on`                           TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP,
    `weighbridge_no`                       INT(11)        NOT NULL,
    `first_axle_weight_exceeded_percent`   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `first_axle_weight_exceeded_weight`    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `second_axle_weight_exceeded_percent`  DECIMAL(11, 2) NULL DEFAULT '0.00',
    `second_axle_weight_exceeded_weight`   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `third_axle_weight_exceeded_percent`   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `third_axle_weight_exceeded_weight`    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `fourth_axle_weight_exceeded_percent`  DECIMAL(11, 2) NULL DEFAULT '0.00',
    `fourth_axle_weight_exceeded_weight`   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `fifth_axle_weight_exceeded_percent`   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `fifth_axle_weight_exceeded_weight`    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `sixth_axle_weight_exceeded_percent`   DECIMAL(11, 2) NULL DEFAULT '0.00',
    `sixth_axle_weight_exceeded_weight`    DECIMAL(11, 2) NULL DEFAULT '0.00',
    `seventh_axle_weight_exceeded_percent` DECIMAL(11, 2) NULL DEFAULT '0.00',
    `seventh_axle_weight_exceeded_weight`  DECIMAL(11, 2) NULL DEFAULT '0.00',
    `gvw_exceeded_percent`                 DECIMAL(11, 2) NULL DEFAULT '0.00',
    `gvw_exceeded_weight`                  DECIMAL(11, 2) NULL DEFAULT '0.00',
    `flag`                                 INT(2)         NULL DEFAULT '0',
    `axle_weight_status`                   INT(2)         NULL DEFAULT '0',
    `axle_count`                           INT(2)         NULL DEFAULT '0',
    `status`                               INT(2)         NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    INDEX `weighbridge_no` (`weighbridge_no`),
    INDEX `ticket_no` (`ticket_no`),
    INDEX `status` (`status`),
    INDEX `axle_configuration` (`axle_configuration`),
    INDEX `transaction_date` (`transaction_date`),
    INDEX `flag` (`flag`),
    CONSTRAINT `kenwei_temp_weighing_transactions_fk_1` FOREIGN KEY (`weighbridge_no`) REFERENCES `weighbridge_stations` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8
    AUTO_INCREMENT = 1;

#====================================================================
#   Add KENWEI temp axle transaction table
#====================================================================
CREATE TABLE `kenwei_temp_axle_transactions`
(
    `id`                    INT(11)        NOT NULL AUTO_INCREMENT,
    `ticket_no`             VARCHAR(100)   NOT NULL,
    `station_code`          VARCHAR(100)   NOT NULL,
    `transaction_date`      TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP,
    `vehicle_no`            VARCHAR(100)   NULL DEFAULT '-',

    `actual_mass`           DECIMAL(11, 2) NULL DEFAULT '0.00',
    `allowed_mass`          DECIMAL(11, 2) NULL DEFAULT '0.00',
    `acceptable_tolerance`  DECIMAL(11, 2) NULL DEFAULT '0.00',
    `overload`              DECIMAL(11, 2) NULL DEFAULT '0.00',

    `axle_configuration_no` VARCHAR(10)    NULL DEFAULT '-',
    `axle_classification`   VARCHAR(15)    NULL DEFAULT '-',
    `axle_unit`             INT(2)              DEFAULT '1',
    `axle_unit_position`    INT(2)              DEFAULT '1',

    `status`                INT(2)         NULL DEFAULT '0',
    `temp_transaction_no`   INT(11)             DEFAULT NULL,

    PRIMARY KEY (`id`),
    INDEX `temp_transaction_no` (`temp_transaction_no`),
    INDEX `ticket_no` (`ticket_no`),
    INDEX `axle_configuration` (`axle_classification`),
    INDEX `transaction_date` (`transaction_date`),
    CONSTRAINT `kenwei_temp_axle_transactions_fk_1` FOREIGN KEY (`temp_transaction_no`) REFERENCES `kenwei_temp_weighing_transactions` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8
    AUTO_INCREMENT = 1;

set foreign_key_checks = 1;
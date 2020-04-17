drop table if exists `census_data`;

CREATE TABLE `traffic_census`
(
    `id`                              INT(11)      NOT NULL AUTO_INCREMENT,
    `wb_station`                      VARCHAR(150) NOT NULL,
    `transaction_date`                TIMESTAMP    NULL DEFAULT NULL,
    `census_hour`                     varchar(2)   NOT NULL,
    `trucks_greater_than_7t`          INT(10)           DEFAULT 0,
    `trucks_between_3_5t_and_less_7t` INT(10)           DEFAULT 0,
    `buses`                           INT(10)           DEFAULT 0,
    `weighbridge_no`                  INT(11)      NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `weighbridge_no` (`weighbridge_no`),
    INDEX `transaction_date` (`transaction_date`),
    CONSTRAINT `traffic_census_fk_1` FOREIGN KEY (`weighbridge_no`) REFERENCES `weighbridge_stations` (`id`)
)
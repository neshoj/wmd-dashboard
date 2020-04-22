set foreign_key_checks = 0;

#====================================================================
#   Container Tables
#====================================================================
CREATE TABLE `data_agent_status`
(
    `id`              INT(11)   NOT NULL AUTO_INCREMENT,
    `weighbridge_no`  INT(11)   NOT NULL,
    `last_updated_on` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `weighbridge_no` (`weighbridge_no`),
    CONSTRAINT `data_agent_status_fk_1` FOREIGN KEY (`weighbridge_no`) REFERENCES `weighbridge_stations` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;


set foreign_key_checks = 1;
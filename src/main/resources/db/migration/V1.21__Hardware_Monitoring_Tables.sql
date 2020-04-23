set foreign_key_checks = 0;

#====================================================================
#   Hardware monitoring Tables
#====================================================================
CREATE TABLE `hardware_monitoring`
(
    `id`              INT(11)      NOT NULL AUTO_INCREMENT,
    `site_name`       VARCHAR(200) NOT NULL,
    `site_ip_address` VARCHAR(15)  NOT NULL,
    `device_name`     VARCHAR(100) NOT NULL,
    `last_updated_on` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

set foreign_key_checks = 1;
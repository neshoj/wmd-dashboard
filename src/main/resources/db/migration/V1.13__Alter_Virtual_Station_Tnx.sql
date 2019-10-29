set foreign_key_checks = 0;


--  Mobile weigh Prosecution data
DROP INDEX virtual_station_transaction_id ON virtual_stations_transactions;

--  Mobile weigh Prosecution data
drop table if exists virtual_stations_transactions;

--
create table virtual_stations_transactions
(
    id int auto_increment primary key,
    virtual_station_transaction_id int not null,
    datetime timestamp default CURRENT_TIMESTAMP null,
    front_plate varchar(45) default 'NA' null,
    back_plate varchar(45) default 'NA' null,
    virtual_station_id varchar(45) default 'NA' null,
    virtual_station varchar(150) default 'NA' null,
    axles_count int(2) default '0' null,
    total_weight decimal(11,2) default '0.00' null,
    weight_limit decimal(11,2) default '0.00' null,
    percentage_overload decimal(11,2) default '0.00' null,
    axle_configuration varchar(35) default 'NA' null,
    axle_description varchar(150) default 'NA' null,
    velocity varchar(50) default 'NA' null,
    length varchar(10) default 'NA' null,
    flag varchar(30) default '0' null,
    wim_detector_id varchar(10) default 'NA' null,
    first_axle_load decimal(11,2) default '0.00' null,
    second_axle_load decimal(11,2) default '0.00' null,
    third_axle_load decimal(11,2) default '0.00' null,
    fourth_axle_load decimal(11,2) default '0.00' null,
    fifth_axle_load decimal(11,2) default '0.00' null,
    sixth_axle_load decimal(11,2) default '0.00' null,
    seventh_axle_load decimal(11,2) default '0.00' null,
    eighth_axle_load decimal(11,2) default '0.00' null,
    ninth_axle_load decimal(11,2) default '0.00' null,
    created_on timestamp default CURRENT_TIMESTAMP null,
    lp_image_front mediumblob null,
    detail_image mediumblob null,
    detail_image_back mediumblob null,
    back_plate_binary_image mediumblob null,
    lp_image_front_url varchar(255) null,
    detail_image_url varchar(255) null,
    detail_image_back_url varchar(255) null,
    back_plate_binary_image_url varchar(255) null
);

CREATE INDEX virtual_station_transaction_id
    ON virtual_stations_transactions (virtual_station_transaction_id);

set foreign_key_checks = 1;
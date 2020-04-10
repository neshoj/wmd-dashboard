drop table if exists `vehicles_overloaded_vhm_daily_reports`;

create table `vehicles_overloaded_vhm_daily_reports`
(
	`id` int auto_increment
		primary key,
	`date` date not null,
	`report_json` json null,
	`report_map` text null,
	constraint vehicles_overloaded_vhm_daily_reports_date_uindex
		unique (date)
);
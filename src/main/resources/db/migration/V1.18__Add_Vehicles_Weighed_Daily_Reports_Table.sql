drop table if exists `vehicles_weighed_daily_reports`;

create table `vehicles_weighed_daily_reports`
(
	`id` int auto_increment
		primary key,
	`date` date not null,
	`report_json` text null,
	constraint ehicles_weighed_daily_reports_date_uindex
		unique (date)
);
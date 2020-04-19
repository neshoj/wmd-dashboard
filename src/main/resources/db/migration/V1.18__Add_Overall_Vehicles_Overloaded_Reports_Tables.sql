drop table if exists `vehicles_overloaded_axle_overall_reports`;

create table `vehicles_overloaded_axle_overall_reports`
(
	`id` int auto_increment
		primary key,
	`report_json` text null
);


drop table if exists `vehicles_overloaded_gvm_overall_reports`;

create table `vehicles_overloaded_gvm_overall_reports`
(
	`id` int auto_increment
		primary key,
	`report_json` text null
);
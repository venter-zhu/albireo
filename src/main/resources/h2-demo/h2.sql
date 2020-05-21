/*
 * Copyright Cboard
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

drop table if exists dashboard_board;
CREATE TABLE if not exists dashboard_board
(
    board_id    bigint(20)   NOT NULL AUTO_INCREMENT,
    user_id     varchar(50)  NOT NULL,
    category_id bigint(20) DEFAULT NULL,
    board_name  varchar(100) NOT NULL,
    layout_json text,
    PRIMARY KEY (board_id)
);
drop table if exists dashboard_category;
CREATE TABLE if not exists dashboard_category (
  category_id bigint(20) NOT NULL AUTO_INCREMENT,
  category_name varchar(100) NOT NULL,
  user_id varchar(100) NOT NULL,
  PRIMARY KEY (category_id)
);
drop table if exists dashboard_datasource;
CREATE TABLE if not exists dashboard_datasource (
  datasource_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id varchar(50) NOT NULL,
  source_name varchar(100) NOT NULL,
  source_type varchar(100) NOT NULL,
  config text,
  PRIMARY KEY (datasource_id)
);
drop table if exists dashboard_widget;
CREATE TABLE if not exists dashboard_widget (
  widget_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id varchar(100) NOT NULL,
  category_name varchar(100) DEFAULT NULL,
  widget_name varchar(100) DEFAULT NULL,
  data_json text,
  PRIMARY KEY (widget_id)
);
drop table if exists dashboard_dataset;
CREATE TABLE if not exists dashboard_dataset (
  dataset_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id varchar(100) NOT NULL,
  category_name varchar(100) DEFAULT NULL,
  dataset_name varchar(100) DEFAULT NULL,
  data_json text,
  PRIMARY KEY (dataset_id)
);
drop table if exists dashboard_user;
CREATE TABLE if not exists dashboard_user (
  user_id varchar(50) NOT NULL,
  login_name varchar(100) DEFAULT NULL,
  user_name varchar(100) DEFAULT NULL,
  user_password varchar(100) DEFAULT NULL,
  user_status varchar(100) DEFAULT NULL,
  PRIMARY KEY (user_id)
);

INSERT INTO dashboard_user (user_id,login_name,user_name,user_password)
VALUES('1', 'admin', 'Administrator', 'ff9830c42660c1dd1942844f8069b74a');
drop table if exists dashboard_user_role;
CREATE TABLE if not exists dashboard_user_role (
  user_role_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id varchar(100) DEFAULT NULL,
  role_id varchar(100) DEFAULT NULL,
  PRIMARY KEY (user_role_id)
);
drop table if exists dashboard_role;
CREATE TABLE if not exists dashboard_role (
  role_id varchar(100) NOT NULL,
  role_name varchar(100) DEFAULT NULL,
  user_id varchar(50) DEFAULT NULL,
  PRIMARY KEY (role_id)
);
drop table if exists dashboard_role_res;
CREATE TABLE if not exists dashboard_role_res (
  role_res_id bigint(20) NOT NULL AUTO_INCREMENT,
  role_id varchar(100) DEFAULT NULL,
  res_type varchar(100) DEFAULT NULL,
  res_id bigint(20) DEFAULT NULL,
  permission varchar(20) DEFAULT NULL,
  PRIMARY KEY (role_res_id)
);
drop table if exists dashboard_job;
CREATE TABLE if not exists dashboard_job (
  job_id bigint(20) NOT NULL AUTO_INCREMENT,
  job_name varchar(200) DEFAULT NULL,
  cron_exp varchar(200) DEFAULT NULL,
  start_date timestamp NULL DEFAULT NULL,
  end_date timestamp NULL DEFAULT NULL,
  job_type varchar(200) DEFAULT NULL,
  job_config text,
  user_id varchar(100) DEFAULT NULL,
  last_exec_time timestamp NULL DEFAULT NULL,
  job_status bigint(20),
  exec_log text,
  PRIMARY KEY (job_id)
);
drop table if exists dashboard_board_param;
CREATE TABLE if not exists dashboard_board_param (
  board_param_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id varchar(50) NOT NULL,
  board_id bigint(20) NOT NULL,
  config text,
  PRIMARY KEY (board_param_id)
);

-- 升级0.4需要执行的
ALTER  TABLE  dashboard_dataset ADD create_time TIMESTAMP DEFAULT now();
ALTER  TABLE  dashboard_dataset ADD update_time TIMESTAMP DEFAULT now();

ALTER  TABLE  dashboard_datasource ADD create_time TIMESTAMP DEFAULT now();
ALTER  TABLE  dashboard_datasource ADD update_time TIMESTAMP DEFAULT now();

ALTER  TABLE  dashboard_widget ADD create_time TIMESTAMP DEFAULT now();
ALTER  TABLE  dashboard_widget ADD update_time TIMESTAMP DEFAULT now();

ALTER  TABLE  dashboard_board ADD create_time TIMESTAMP DEFAULT now();
ALTER  TABLE  dashboard_board ADD update_time TIMESTAMP DEFAULT now();
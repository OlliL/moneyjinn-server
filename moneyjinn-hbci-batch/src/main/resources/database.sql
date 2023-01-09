-- MySQL dump 10.13  Distrib 5.6.26, for FreeBSD10.1 (amd64)
--
-- Host: localhost    Database: moneyflow_hbci
-- ------------------------------------------------------
-- Server version       5.6.26


--
-- Table structure for table `account_movements`
--

DROP TABLE IF EXISTS account_movements;
CREATE TABLE account_movements (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  creation_time datetime NOT NULL,
  my_iban varchar(34) NOT NULL,
  my_bic varchar(11) NOT NULL,
  my_accountnumber bigint(10) unsigned NOT NULL,
  my_bankcode int(8) unsigned NOT NULL,
  booking_date date NOT NULL,
  value_date date NOT NULL,
  invoice_timestamp timestamp NULL DEFAULT NULL,
  other_iban varchar(34) DEFAULT NULL,
  other_bic varchar(11) DEFAULT NULL,
  other_accountnumber bigint(10) unsigned DEFAULT NULL,
  other_bankcode int(8) unsigned DEFAULT NULL,
  other_name varchar(54) DEFAULT NULL,
  charge_value decimal(15,2) DEFAULT NULL,
  charge_currency varchar(3) DEFAULT NULL,
  original_value decimal(15,2) DEFAULT NULL,
  original_currency varchar(3) DEFAULT NULL,
  movement_value decimal(15,2) NOT NULL,
  movement_currency varchar(3) NOT NULL,
  movement_reason text,
  movement_type_code int(3) NOT NULL,
  movement_type_text varchar(31) DEFAULT NULL,
  customer_reference varchar(16) NOT NULL,
  bank_reference varchar(16) DEFAULT NULL,
  cancellation int(1) unsigned NOT NULL,
  additional_information varchar(512) DEFAULT NULL,
  additional_key int(3) DEFAULT NULL,
  prima_nota varchar(16) DEFAULT NULL,
  balance_date date NOT NULL,
  balance_value decimal(15,2) NOT NULL,
  balance_currency varchar(3) NOT NULL,
  PRIMARY KEY (account_movement_id),
  UNIQUE KEY hbci_i_03 (my_iban,my_bic,my_accountnumber,my_bankcode,booking_date,value_date,movement_value,movement_currency,movement_type_code,customer_reference,cancellation,balance_date,balance_value,balance_currency)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `balance_monthly`
--

DROP TABLE IF EXISTS balance_monthly;
CREATE TABLE balance_monthly (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  my_iban varchar(34) NOT NULL,
  my_bic varchar(11) NOT NULL,
  my_accountnumber bigint(10) unsigned NOT NULL,
  my_bankcode int(8) unsigned NOT NULL,
  balance_year year(4) NOT NULL,
  balance_month tinyint(4) unsigned NOT NULL,
  balance_value decimal(10,2) NOT NULL,
  balance_currency varchar(3) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY hbci_i_02 (my_iban,my_bic,my_accountnumber,my_bankcode,balance_year,balance_month)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `balance_daily`
--

DROP TABLE IF EXISTS balance_daily;
CREATE TABLE balance_daily (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  my_iban varchar(34) NOT NULL,
  my_bic varchar(11) NOT NULL,
  my_accountnumber bigint(10) unsigned NOT NULL,
  my_bankcode int(8) unsigned NOT NULL,
  balance_date date NOT NULL,
  last_transaction_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  balance_available_value decimal(10,2) NOT NULL,
  line_of_credit_value decimal(10,2) NOT NULL,
  balance_currency varchar(3) NOT NULL,
  last_balance_update timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY hbci_i_04 (my_iban,my_bic,my_accountnumber,my_bankcode,balance_date)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Dump completed on 2015-09-23 21:52:34

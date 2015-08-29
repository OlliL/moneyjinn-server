-- MySQL dump 10.13  Distrib 5.6.26, for FreeBSD10.1 (amd64)
--
-- Host: db    Database: moneyflow
-- ------------------------------------------------------
-- Server version	5.6.26-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES latin1 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access`
--

DROP TABLE IF EXISTS access;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE access (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `password` varchar(40) DEFAULT NULL,
  att_user tinyint(1) unsigned NOT NULL,
  att_change_password tinyint(1) unsigned NOT NULL,
  perm_login tinyint(1) unsigned NOT NULL,
  perm_admin tinyint(1) unsigned NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_relation`
--

DROP TABLE IF EXISTS access_relation;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE access_relation (
  id int(10) unsigned NOT NULL,
  ref_id int(10) unsigned NOT NULL,
  validfrom date NOT NULL,
  validtil date NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_flattened`
--

DROP TABLE IF EXISTS access_flattened;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE access_flattened (
  id int(10) unsigned NOT NULL,
  validfrom date NOT NULL,
  validtil date NOT NULL,
  id_level_1 int(10) unsigned NOT NULL,
  id_level_2 int(10) unsigned NOT NULL,
  id_level_3 int(10) unsigned DEFAULT NULL,
  id_level_4 int(10) unsigned DEFAULT NULL,
  id_level_5 int(10) unsigned DEFAULT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS settings;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE settings (
  mac_id int(10) unsigned NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  `value` varchar(256) DEFAULT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `capitalsources`
--

DROP TABLE IF EXISTS capitalsources;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE capitalsources (
  capitalsourceid int(10) unsigned NOT NULL AUTO_INCREMENT,
  mac_id_creator int(10) unsigned NOT NULL,
  mac_id_accessor int(10) unsigned NOT NULL,
  `type` tinyint(4) NOT NULL DEFAULT '1',
  state tinyint(4) NOT NULL DEFAULT '1',
  accountnumber varchar(34) DEFAULT NULL,
  bankcode varchar(11) DEFAULT NULL,
  `comment` varchar(255) NOT NULL,
  validtil date NOT NULL DEFAULT '2999-12-31',
  validfrom date NOT NULL DEFAULT '1970-01-01',
  att_group_use tinyint(1) unsigned NOT NULL DEFAULT '0',
  import_allowed tinyint(1) unsigned NOT NULL DEFAULT '0',
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contractpartners`
--

DROP TABLE IF EXISTS contractpartners;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE contractpartners (
  contractpartnerid int(10) unsigned NOT NULL AUTO_INCREMENT,
  mac_id_creator int(10) unsigned NOT NULL,
  mac_id_accessor int(10) unsigned NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  street varchar(100) DEFAULT '',
  postcode int(12) DEFAULT '0',
  town varchar(100) DEFAULT '',
  country varchar(100) DEFAULT '',
  validfrom date NOT NULL,
  validtil date NOT NULL,
  mmf_comment varchar(100) DEFAULT NULL,
  mpa_postingaccountid int(10) unsigned DEFAULT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contractpartneraccounts`
--

DROP TABLE IF EXISTS contractpartneraccounts;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE contractpartneraccounts (
  contractpartneraccountid int(10) unsigned NOT NULL AUTO_INCREMENT,
  mcp_contractpartnerid int(10) unsigned NOT NULL,
  accountnumber varchar(34) NOT NULL,
  bankcode varchar(11) DEFAULT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflows`
--

DROP TABLE IF EXISTS moneyflows;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE moneyflows (
  moneyflowid int(10) unsigned NOT NULL AUTO_INCREMENT,
  mac_id_creator int(10) unsigned NOT NULL,
  mac_id_accessor int(10) unsigned NOT NULL,
  bookingdate date NOT NULL DEFAULT '0000-00-00',
  invoicedate date NOT NULL DEFAULT '0000-00-00',
  amount decimal(8,2) NOT NULL DEFAULT '0.00',
  mcs_capitalsourceid int(10) unsigned NOT NULL,
  mcp_contractpartnerid int(10) unsigned NOT NULL,
  `comment` varchar(100) NOT NULL DEFAULT '',
  mpa_postingaccountid int(10) unsigned NOT NULL,
  private tinyint(1) NOT NULL DEFAULT '0',
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `monthlysettlements`
--

DROP TABLE IF EXISTS monthlysettlements;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE monthlysettlements (
  monthlysettlementid int(10) unsigned NOT NULL AUTO_INCREMENT,
  mac_id_creator int(10) unsigned NOT NULL,
  mac_id_accessor int(10) unsigned NOT NULL,
  mcs_capitalsourceid int(10) unsigned NOT NULL,
  `month` tinyint(4) unsigned NOT NULL,
  `year` year(4) NOT NULL,
  amount decimal(8,2) NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `postingaccounts`
--

DROP TABLE IF EXISTS postingaccounts;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE postingaccounts (
  postingaccountid int(10) unsigned NOT NULL AUTO_INCREMENT,
  postingaccountname varchar(20) NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `predefmoneyflows`
--

DROP TABLE IF EXISTS predefmoneyflows;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE predefmoneyflows (
  predefmoneyflowid int(10) unsigned NOT NULL AUTO_INCREMENT,
  mac_id int(10) unsigned NOT NULL,
  amount decimal(8,2) NOT NULL DEFAULT '0.00',
  mcs_capitalsourceid int(10) unsigned NOT NULL,
  mcp_contractpartnerid int(10) unsigned NOT NULL,
  `comment` varchar(100) NOT NULL DEFAULT '',
  createdate date NOT NULL,
  once_a_month tinyint(1) unsigned NOT NULL DEFAULT '0',
  last_used date DEFAULT NULL,
  mpa_postingaccountid int(10) unsigned NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impbalance`
--

DROP TABLE IF EXISTS impbalance;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE impbalance (
  mcs_capitalsourceid int(10) unsigned NOT NULL,
  balance decimal(8,2) NOT NULL,
  changedate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmoneyflows`
--

DROP TABLE IF EXISTS impmoneyflows;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE impmoneyflows (
  impmoneyflowid int(10) unsigned NOT NULL AUTO_INCREMENT,
  externalid varchar(10) NOT NULL,
  mcs_capitalsourceid int(10) unsigned NOT NULL,
  bookingdate date NOT NULL,
  invoicedate date NOT NULL,
  `name` varchar(100) NOT NULL,
  accountnumber varchar(34) NOT NULL,
  bankcode varchar(11) NOT NULL,
  `comment` varchar(512) DEFAULT NULL,
  amount decimal(8,2) NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmonthlysettlements`
--

DROP TABLE IF EXISTS impmonthlysettlements;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE impmonthlysettlements (
  impmonthlysettlementid int(10) unsigned NOT NULL AUTO_INCREMENT,
  externalid varchar(10) NOT NULL,
  mcs_capitalsourceid int(10) unsigned NOT NULL,
  `month` tinyint(4) unsigned NOT NULL,
  `year` year(4) NOT NULL,
  amount decimal(8,2) NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_data`
--

DROP TABLE IF EXISTS imp_data;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE imp_data (
  dataid int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` varchar(10) NOT NULL,
  amount varchar(20) NOT NULL,
  `source` varchar(100) NOT NULL,
  partner varchar(100) NOT NULL,
  `comment` varchar(100) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1',
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_mapping_source`
--

DROP TABLE IF EXISTS imp_mapping_source;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE imp_mapping_source (
  source_from varchar(100) NOT NULL,
  source_to varchar(100) NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_mapping_partner`
--

DROP TABLE IF EXISTS imp_mapping_partner;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE imp_mapping_partner (
  partner_from varchar(100) NOT NULL,
  partner_to varchar(100) NOT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmp_data_formats`
--

DROP TABLE IF EXISTS cmp_data_formats;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE cmp_data_formats (
  formatid int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  startline varchar(255) NOT NULL,
  delimiter varchar(1) NOT NULL,
  pos_date tinyint(2) NOT NULL,
  pos_partner tinyint(2) DEFAULT NULL,
  pos_amount tinyint(2) NOT NULL,
  pos_comment tinyint(2) DEFAULT NULL,
  fmt_date varchar(10) NOT NULL,
  fmt_amount_decimal varchar(1) NOT NULL,
  fmt_amount_thousand varchar(1) DEFAULT NULL,
  pos_partner_alt tinyint(2) DEFAULT NULL,
  pos_partner_alt_pos_key tinyint(2) DEFAULT NULL,
  pos_partner_alt_keyword varchar(255) DEFAULT NULL,
);
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

CREATE   PRIMARY KEY ON predefmoneyflows (predefmoneyflowid);
CREATE   INDEX mcs_mac_pk_01 ON capitalsources (mac_id_creator);
CREATE   PRIMARY KEY ON access_relation (id,validfrom);
CREATE   INDEX mcs_mac_pk_02 ON capitalsources (mac_id_accessor);
CREATE   INDEX mar_i_01 ON access_relation (ref_id);
ALTER TABLE capitalsources ADD   CONSTRAINT mcs_mac_pk_01 FOREIGN KEY (mac_id_creator) REFERENCES access (id);
ALTER TABLE access_relation ADD   CONSTRAINT mar_mac_pk_01 FOREIGN KEY (id) REFERENCES access (id);
ALTER TABLE impmoneyflows ADD   CONSTRAINT mim_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources (capitalsourceid);
ALTER TABLE capitalsources ADD   CONSTRAINT mcs_mac_pk_02 FOREIGN KEY (mac_id_accessor) REFERENCES access (id);
ALTER TABLE access_relation ADD   CONSTRAINT mar_mac_pk_02 FOREIGN KEY (ref_id) REFERENCES access (id);
CREATE   PRIMARY KEY ON impmonthlysettlements (impmonthlysettlementid);
CREATE   PRIMARY KEY ON contractpartners (contractpartnerid);
CREATE   PRIMARY KEY ON access_flattened (id,validfrom);
CREATE   UNIQUE INDEX mit_i_01 ON impmonthlysettlements (externalid);
CREATE   UNIQUE INDEX mcp_i_01 ON contractpartners (mac_id_accessor,`name`);
CREATE   INDEX maf_i_01 ON access_flattened (id_level_1);
CREATE   INDEX mis_mcs_pk ON impmonthlysettlements (mcs_capitalsourceid);
CREATE   INDEX mcp_mac_pk_01 ON contractpartners (mac_id_creator);
CREATE   INDEX maf_i_02 ON access_flattened (id_level_2);
ALTER TABLE impmonthlysettlements ADD   CONSTRAINT mis_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources (capitalsourceid);
CREATE   INDEX mcp_mac_pk_02 ON contractpartners (mac_id_accessor);
CREATE   INDEX maf_i_03 ON access_flattened (id_level_3);
CREATE   PRIMARY KEY ON imp_data (dataid);
CREATE   UNIQUE INDEX mis_i_01 ON imp_mapping_source (source_from);
CREATE   UNIQUE INDEX mip_i_01 ON imp_mapping_partner (partner_from);
CREATE   INDEX mmf_mac_pk_01 ON moneyflows (mac_id_creator);
CREATE   PRIMARY KEY ON cmp_data_formats (formatid);
CREATE   INDEX ` mmf_mac_pk_02` ON moneyflows (mac_id_accessor);
CREATE   UNIQUE INDEX `name` ON cmp_data_formats (`name`);
CREATE   INDEX mmf_mcs_pk ON moneyflows (mcs_capitalsourceid);
CREATE   INDEX mmf_mcp_pk ON moneyflows (mcp_contractpartnerid);
CREATE   INDEX mmf_mpa_pk ON moneyflows (mpa_postingaccountid);
ALTER TABLE moneyflows ADD   CONSTRAINT mmf_mac_pk_01 FOREIGN KEY (mac_id_creator) REFERENCES access (id);
ALTER TABLE moneyflows ADD   CONSTRAINT mmf_mac_pk_02 FOREIGN KEY (mac_id_accessor) REFERENCES access (id);
ALTER TABLE moneyflows ADD   CONSTRAINT mmf_mcp_pk FOREIGN KEY (mcp_contractpartnerid) REFERENCES contractpartners (contractpartnerid);
CREATE   INDEX maf_i_04 ON access_flattened (id_level_4);
ALTER TABLE moneyflows ADD   CONSTRAINT mmf_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources (capitalsourceid);
CREATE   INDEX maf_i_05 ON access_flattened (id_level_5);
ALTER TABLE moneyflows ADD   CONSTRAINT mmf_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts (postingaccountid);
ALTER TABLE access_flattened ADD   CONSTRAINT maf_mac_pk_01 FOREIGN KEY (id) REFERENCES access (id);
ALTER TABLE access_flattened ADD   CONSTRAINT maf_mac_pk_02 FOREIGN KEY (id_level_1) REFERENCES access (id);
ALTER TABLE access_flattened ADD   CONSTRAINT maf_mac_pk_03 FOREIGN KEY (id_level_2) REFERENCES access (id);
CREATE   INDEX mpm_mac_pk ON predefmoneyflows (mac_id);
ALTER TABLE access_flattened ADD   CONSTRAINT maf_mac_pk_04 FOREIGN KEY (id_level_3) REFERENCES access (id);
CREATE   INDEX mpm_mpa_pk ON predefmoneyflows (mpa_postingaccountid);
ALTER TABLE access_flattened ADD   CONSTRAINT maf_mac_pk_05 FOREIGN KEY (id_level_4) REFERENCES access (id);
CREATE   INDEX mpm_mcs_pk ON predefmoneyflows (mcs_capitalsourceid);
ALTER TABLE access_flattened ADD   CONSTRAINT maf_mac_pk_06 FOREIGN KEY (id_level_5) REFERENCES access (id);
ALTER TABLE predefmoneyflows ADD   CONSTRAINT mpm_mac_pk FOREIGN KEY (mac_id) REFERENCES access (id);
CREATE   PRIMARY KEY ON settings (`name`,mac_id);
ALTER TABLE predefmoneyflows ADD   CONSTRAINT mpm_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources (capitalsourceid);
CREATE   INDEX mse_mac_pk ON settings (mac_id);
ALTER TABLE predefmoneyflows ADD   CONSTRAINT mpm_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts (postingaccountid);
CREATE   PRIMARY KEY ON impbalance (mcs_capitalsourceid);
CREATE   PRIMARY KEY ON impmoneyflows (impmoneyflowid);
CREATE   INDEX mcp_mpa_pk_01 ON contractpartners (mpa_postingaccountid);
CREATE   UNIQUE INDEX mim_i_01 ON impmoneyflows (externalid);
ALTER TABLE contractpartners ADD   CONSTRAINT mcp_mac_pk_01 FOREIGN KEY (mac_id_creator) REFERENCES access (id);
CREATE   INDEX mim_mcs_pk ON impmoneyflows (mcs_capitalsourceid);
ALTER TABLE contractpartners ADD   CONSTRAINT mcp_mac_pk_02 FOREIGN KEY (mac_id_accessor) REFERENCES access (id);
ALTER TABLE contractpartners ADD   CONSTRAINT mcp_mpa_pk_01 FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts (postingaccountid);
CREATE   PRIMARY KEY ON contractpartneraccounts (contractpartneraccountid);
CREATE   UNIQUE INDEX mca_i_01 ON contractpartneraccounts (accountnumber,bankcode);
CREATE   INDEX mca_mcp_pk_01 ON contractpartneraccounts (mcp_contractpartnerid);
ALTER TABLE contractpartneraccounts ADD   CONSTRAINT mca_mcp_pk_01 FOREIGN KEY (mcp_contractpartnerid) REFERENCES contractpartners (contractpartnerid);
CREATE   PRIMARY KEY ON moneyflows (moneyflowid);
CREATE   INDEX mmf_i_01 ON moneyflows (bookingdate,mac_id_accessor,moneyflowid);
CREATE   PRIMARY KEY ON monthlysettlements (monthlysettlementid);
CREATE   UNIQUE INDEX mms_i_01 ON monthlysettlements (`month`,`year`,mcs_capitalsourceid);
CREATE   INDEX mms_mac_pk_01 ON monthlysettlements (mac_id_creator);
CREATE   INDEX mms_mac_pk_02 ON monthlysettlements (mac_id_accessor);
CREATE   INDEX mms_mcs_pk ON monthlysettlements (mcs_capitalsourceid);
ALTER TABLE monthlysettlements ADD   CONSTRAINT mms_mac_pk_01 FOREIGN KEY (mac_id_creator) REFERENCES access (id);
ALTER TABLE monthlysettlements ADD   CONSTRAINT mms_mac_pk_02 FOREIGN KEY (mac_id_accessor) REFERENCES access (id);
ALTER TABLE monthlysettlements ADD   CONSTRAINT mms_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources (capitalsourceid);
ALTER TABLE settings ADD   CONSTRAINT mse_mac_pk FOREIGN KEY (mac_id) REFERENCES access (id);
CREATE   PRIMARY KEY ON access (id);
CREATE   PRIMARY KEY ON postingaccounts (postingaccountid);
CREATE   PRIMARY KEY ON capitalsources (capitalsourceid,mac_id_accessor);
CREATE   UNIQUE INDEX mac_i_01 ON access (`name`,att_user);

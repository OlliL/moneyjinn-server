-- MySQL dump 10.13  Distrib 5.6.51, for FreeBSD11.4 (amd64)
--
-- Host: db    Database: moneyflow
-- ------------------------------------------------------
-- Server version	5.6.51-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access`
--

DROP TABLE IF EXISTS `access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_bin NOT NULL,
  `password` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  `att_user` tinyint(1) unsigned NOT NULL,
  `att_change_password` tinyint(1) unsigned NOT NULL,
  `perm_login` tinyint(1) unsigned NOT NULL,
  `perm_admin` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac_i_01` (`name`,`att_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mac';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_relation`
--

DROP TABLE IF EXISTS `access_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_relation` (
  `id` int(10) unsigned NOT NULL,
  `ref_id` int(10) unsigned NOT NULL,
  `validfrom` date NOT NULL,
  `validtil` date NOT NULL,
  PRIMARY KEY (`id`,`validfrom`),
  KEY `mar_i_01` (`ref_id`),
  CONSTRAINT `mar_mac_pk_01` FOREIGN KEY (`id`) REFERENCES `access` (`id`),
  CONSTRAINT `mar_mac_pk_02` FOREIGN KEY (`ref_id`) REFERENCES `access` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_flattened`
--

DROP TABLE IF EXISTS `access_flattened`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_flattened` (
  `id` int(10) unsigned NOT NULL,
  `validfrom` date NOT NULL,
  `validtil` date NOT NULL,
  `id_level_1` int(10) unsigned NOT NULL,
  `id_level_2` int(10) unsigned NOT NULL,
  `id_level_3` int(10) unsigned DEFAULT NULL,
  `id_level_4` int(10) unsigned DEFAULT NULL,
  `id_level_5` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`,`validfrom`),
  KEY `maf_i_01` (`id_level_1`),
  KEY `maf_i_02` (`id_level_2`),
  KEY `maf_i_03` (`id_level_3`),
  KEY `maf_i_04` (`id_level_4`),
  KEY `maf_i_05` (`id_level_5`),
  CONSTRAINT `maf_mac_pk_01` FOREIGN KEY (`id`) REFERENCES `access` (`id`),
  CONSTRAINT `maf_mac_pk_02` FOREIGN KEY (`id_level_1`) REFERENCES `access` (`id`),
  CONSTRAINT `maf_mac_pk_03` FOREIGN KEY (`id_level_2`) REFERENCES `access` (`id`),
  CONSTRAINT `maf_mac_pk_04` FOREIGN KEY (`id_level_3`) REFERENCES `access` (`id`),
  CONSTRAINT `maf_mac_pk_05` FOREIGN KEY (`id_level_4`) REFERENCES `access` (`id`),
  CONSTRAINT `maf_mac_pk_06` FOREIGN KEY (`id_level_5`) REFERENCES `access` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='maf';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `mac_id` int(10) unsigned NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '',
  `value` varchar(2048) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`name`,`mac_id`),
  KEY `mse_mac_pk` (`mac_id`),
  CONSTRAINT `mse_mac_pk` FOREIGN KEY (`mac_id`) REFERENCES `access` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mse';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `capitalsources`
--

DROP TABLE IF EXISTS `capitalsources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `capitalsources` (
  `capitalsourceid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int(10) unsigned NOT NULL,
  `mac_id_accessor` int(10) unsigned NOT NULL,
  `type` tinyint(4) NOT NULL DEFAULT '1',
  `state` tinyint(4) NOT NULL DEFAULT '1',
  `accountnumber` varchar(34) COLLATE utf8_bin DEFAULT NULL,
  `bankcode` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_bin NOT NULL,
  `validtil` date NOT NULL DEFAULT '2999-12-31',
  `validfrom` date NOT NULL DEFAULT '1970-01-01',
  `att_group_use` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `import_allowed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`capitalsourceid`,`mac_id_accessor`),
  KEY `mcs_mac_pk_01` (`mac_id_creator`),
  KEY `mcs_mac_pk_02` (`mac_id_accessor`),
  CONSTRAINT `mcs_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`),
  CONSTRAINT `mcs_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mcs';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `postingaccounts`
--

DROP TABLE IF EXISTS `postingaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `postingaccounts` (
  `postingaccountid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postingaccountname` varchar(60) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`postingaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mpa';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contractpartners`
--

DROP TABLE IF EXISTS `contractpartners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contractpartners` (
  `contractpartnerid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int(10) unsigned NOT NULL,
  `mac_id_accessor` int(10) unsigned NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '',
  `street` varchar(100) COLLATE utf8_bin DEFAULT '',
  `postcode` int(12) DEFAULT '0',
  `town` varchar(100) COLLATE utf8_bin DEFAULT '',
  `country` varchar(100) COLLATE utf8_bin DEFAULT '',
  `validfrom` date NOT NULL,
  `validtil` date NOT NULL,
  `mmf_comment` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `mpa_postingaccountid` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`contractpartnerid`),
  UNIQUE KEY `mcp_i_01` (`mac_id_accessor`,`name`),
  KEY `mcp_mac_pk_01` (`mac_id_creator`),
  KEY `mcp_mac_pk_02` (`mac_id_accessor`),
  KEY `mcp_mpa_pk_01` (`mpa_postingaccountid`),
  CONSTRAINT `mcp_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`),
  CONSTRAINT `mcp_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`),
  CONSTRAINT `mcp_mpa_pk_01` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mcp';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contractpartneraccounts`
--

DROP TABLE IF EXISTS `contractpartneraccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contractpartneraccounts` (
  `contractpartneraccountid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mcp_contractpartnerid` int(10) unsigned NOT NULL,
  `accountnumber` varchar(34) COLLATE utf8_bin NOT NULL,
  `bankcode` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`contractpartneraccountid`),
  UNIQUE KEY `mca_i_01` (`accountnumber`,`bankcode`) USING BTREE,
  KEY `mca_mcp_pk_01` (`mcp_contractpartnerid`),
  CONSTRAINT `mca_mcp_pk_01` FOREIGN KEY (`mcp_contractpartnerid`) REFERENCES `contractpartners` (`contractpartnerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mca';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `etf`
--

DROP TABLE IF EXISTS `etf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etf` (
  `isin` varchar(30) COLLATE utf8_bin NOT NULL,
  `name` varchar(60) COLLATE utf8_bin NOT NULL,
  `wkn` varchar(10) COLLATE utf8_bin NOT NULL,
  `ticker` varchar(10) COLLATE utf8_bin NOT NULL,
  `chart_url` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`isin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='met';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `etfflows`
--

DROP TABLE IF EXISTS `etfflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etfflows` (
  `etfflowid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `flowdate` datetime(6) NOT NULL,
  `isin` varchar(30) COLLATE utf8_bin NOT NULL,
  `amount` decimal(10,3) NOT NULL,
  `price` decimal(6,3) NOT NULL,
  PRIMARY KEY (`etfflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mef';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `etfvalues`
--

DROP TABLE IF EXISTS `etfvalues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etfvalues` (
  `isin` varchar(30) COLLATE utf8_bin NOT NULL,
  `date` date NOT NULL,
  `buy_price` decimal(10,3) NOT NULL,
  `sell_price` decimal(10,3) NOT NULL,
  `changedate` datetime NOT NULL,
  UNIQUE KEY `etf_i_01` (`isin`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mev';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflows`
--

DROP TABLE IF EXISTS `moneyflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moneyflows` (
  `moneyflowid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int(10) unsigned NOT NULL,
  `mac_id_accessor` int(10) unsigned NOT NULL,
  `bookingdate` date NOT NULL DEFAULT '1970-01-01',
  `invoicedate` date NOT NULL DEFAULT '1970-01-01',
  `amount` float(8,2) NOT NULL DEFAULT '0.00',
  `mcs_capitalsourceid` int(10) unsigned NOT NULL,
  `mcp_contractpartnerid` int(10) unsigned NOT NULL,
  `comment` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '',
  `mpa_postingaccountid` int(10) unsigned NOT NULL,
  `private` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`moneyflowid`),
  KEY `mmf_i_01` (`bookingdate`,`mac_id_accessor`,`moneyflowid`),
  KEY `mmf_mac_pk_01` (`mac_id_creator`),
  KEY ` mmf_mac_pk_02` (`mac_id_accessor`),
  KEY `mmf_mcs_pk` (`mcs_capitalsourceid`),
  KEY `mmf_mcp_pk` (`mcp_contractpartnerid`),
  KEY `mmf_mpa_pk` (`mpa_postingaccountid`),
  CONSTRAINT `mmf_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`),
  CONSTRAINT `mmf_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`),
  CONSTRAINT `mmf_mcp_pk` FOREIGN KEY (`mcp_contractpartnerid`) REFERENCES `contractpartners` (`contractpartnerid`),
  CONSTRAINT `mmf_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`),
  CONSTRAINT `mmf_mpa_pk` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mmf';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflowsplitentries`
--

DROP TABLE IF EXISTS `moneyflowsplitentries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moneyflowsplitentries` (
  `moneyflowsplitentryid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mmf_moneyflowid` int(10) unsigned NOT NULL,
  `amount` decimal(8,2) NOT NULL,
  `comment` varchar(100) COLLATE utf8_bin NOT NULL,
  `mpa_postingaccountid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moneyflowsplitentryid`),
  KEY `mse_mmf_pk` (`mmf_moneyflowid`),
  KEY `mse_mpa_pk` (`mpa_postingaccountid`),
  CONSTRAINT `mse_mmf_pk` FOREIGN KEY (`mmf_moneyflowid`) REFERENCES `moneyflows` (`moneyflowid`),
  CONSTRAINT `mse_mpa_pk` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mse';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflowreceipts`
--

DROP TABLE IF EXISTS `moneyflowreceipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moneyflowreceipts` (
  `moneyflowreceiptid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mmf_moneyflowid` int(10) unsigned NOT NULL,
  `receipt` mediumblob NOT NULL,
  `receipt_type` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`moneyflowreceiptid`),
  UNIQUE KEY `mrp_i_01` (`mmf_moneyflowid`) USING BTREE,
  CONSTRAINT `mrp_mmf_pk` FOREIGN KEY (`mmf_moneyflowid`) REFERENCES `moneyflows` (`moneyflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mrp';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `monthlysettlements`
--

DROP TABLE IF EXISTS `monthlysettlements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monthlysettlements` (
  `monthlysettlementid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int(10) unsigned NOT NULL,
  `mac_id_accessor` int(10) unsigned NOT NULL,
  `mcs_capitalsourceid` int(10) unsigned NOT NULL,
  `month` tinyint(4) unsigned NOT NULL,
  `year` year(4) NOT NULL,
  `amount` float(8,2) NOT NULL,
  PRIMARY KEY (`monthlysettlementid`),
  UNIQUE KEY `mms_i_01` (`month`,`year`,`mcs_capitalsourceid`),
  KEY `mms_mac_pk_01` (`mac_id_creator`),
  KEY `mms_mac_pk_02` (`mac_id_accessor`),
  KEY `mms_mcs_pk` (`mcs_capitalsourceid`),
  CONSTRAINT `mms_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`),
  CONSTRAINT `mms_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`),
  CONSTRAINT `mms_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mms';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `predefmoneyflows`
--

DROP TABLE IF EXISTS `predefmoneyflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `predefmoneyflows` (
  `predefmoneyflowid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac_id` int(10) unsigned NOT NULL,
  `amount` float(8,2) NOT NULL DEFAULT '0.00',
  `mcs_capitalsourceid` int(10) unsigned NOT NULL,
  `mcp_contractpartnerid` int(10) unsigned NOT NULL,
  `comment` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '',
  `createdate` date NOT NULL DEFAULT '1970-01-01',
  `once_a_month` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `last_used` date DEFAULT NULL,
  `mpa_postingaccountid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`predefmoneyflowid`),
  KEY `mpm_mac_pk` (`mac_id`),
  KEY `mpm_mpa_pk` (`mpa_postingaccountid`),
  KEY `mpm_mcs_pk` (`mcs_capitalsourceid`),
  KEY `mpm_mcp_pk_01` (`mcp_contractpartnerid`),
  CONSTRAINT `mpm_mac_pk` FOREIGN KEY (`mac_id`) REFERENCES `access` (`id`),
  CONSTRAINT `mpm_mcp_pk_01` FOREIGN KEY (`mcp_contractpartnerid`) REFERENCES `contractpartners` (`contractpartnerid`),
  CONSTRAINT `mpm_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`),
  CONSTRAINT `mpm_mpa_pk` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mpm';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impbalance`
--

DROP TABLE IF EXISTS `impbalance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `impbalance` (
  `mcs_capitalsourceid` int(10) unsigned NOT NULL,
  `balance` float(8,2) NOT NULL,
  `changedate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mcs_capitalsourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mib';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmoneyflowreceipts`
--

DROP TABLE IF EXISTS `impmoneyflowreceipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `impmoneyflowreceipts` (
  `impmoneyflowreceiptid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int(10) unsigned NOT NULL,
  `mac_id_accessor` int(10) unsigned NOT NULL,
  `receipt` mediumblob NOT NULL,
  `filename` varchar(255) COLLATE utf8_bin NOT NULL,
  `mediatype` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`impmoneyflowreceiptid`),
  KEY `mir_mac_pk_01` (`mac_id_creator`),
  KEY `mir_mac_pk_02` (`mac_id_accessor`),
  CONSTRAINT `mir_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`),
  CONSTRAINT `mir_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mir';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmoneyflows`
--

DROP TABLE IF EXISTS `impmoneyflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `impmoneyflows` (
  `impmoneyflowid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `externalid` varchar(10) COLLATE utf8_bin NOT NULL,
  `mcs_capitalsourceid` int(10) unsigned NOT NULL,
  `bookingdate` date NOT NULL,
  `invoicedate` date NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `accountnumber` varchar(34) COLLATE utf8_bin NOT NULL,
  `bankcode` varchar(11) COLLATE utf8_bin NOT NULL,
  `comment` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `amount` float(8,2) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`impmoneyflowid`),
  UNIQUE KEY `mim_i_01` (`externalid`),
  KEY `mim_mcs_pk` (`mcs_capitalsourceid`),
  CONSTRAINT `mim_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mim';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmonthlysettlements`
--

DROP TABLE IF EXISTS `impmonthlysettlements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `impmonthlysettlements` (
  `impmonthlysettlementid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `externalid` varchar(10) COLLATE utf8_bin NOT NULL,
  `mcs_capitalsourceid` int(10) unsigned NOT NULL,
  `month` tinyint(4) unsigned NOT NULL,
  `year` year(4) NOT NULL,
  `amount` float(8,2) NOT NULL,
  PRIMARY KEY (`impmonthlysettlementid`),
  UNIQUE KEY `mit_i_01` (`externalid`) USING BTREE,
  KEY `mis_mcs_pk` (`mcs_capitalsourceid`),
  CONSTRAINT `mis_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mit';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_data`
--

DROP TABLE IF EXISTS `imp_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imp_data` (
  `dataid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` varchar(10) COLLATE utf8_bin NOT NULL,
  `amount` varchar(20) COLLATE utf8_bin NOT NULL,
  `source` varchar(100) COLLATE utf8_bin NOT NULL,
  `partner` varchar(100) COLLATE utf8_bin NOT NULL,
  `comment` varchar(100) COLLATE utf8_bin NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`dataid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mid';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_mapping_source`
--

DROP TABLE IF EXISTS `imp_mapping_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imp_mapping_source` (
  `source_from` varchar(100) COLLATE utf8_bin NOT NULL,
  `source_to` varchar(100) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `mis_i_01` (`source_from`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mis';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_mapping_partner`
--

DROP TABLE IF EXISTS `imp_mapping_partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imp_mapping_partner` (
  `partner_from` varchar(100) COLLATE utf8_bin NOT NULL,
  `partner_to` varchar(100) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `mip_i_01` (`partner_from`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mip';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmp_data_formats`
--

DROP TABLE IF EXISTS `cmp_data_formats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmp_data_formats` (
  `formatid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `start_trigger_0` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `start_trigger_1` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `start_trigger_2` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `startline` varchar(255) COLLATE utf8_bin NOT NULL,
  `delimiter` varchar(1) COLLATE utf8_bin NOT NULL,
  `pos_date` tinyint(2) NOT NULL,
  `pos_partner` tinyint(2) DEFAULT NULL,
  `pos_amount` tinyint(2) NOT NULL,
  `pos_comment` tinyint(2) DEFAULT NULL,
  `fmt_date` varchar(10) COLLATE utf8_bin NOT NULL,
  `fmt_amount_decimal` varchar(1) COLLATE utf8_bin NOT NULL,
  `fmt_amount_thousand` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `pos_partner_alt` tinyint(2) DEFAULT NULL,
  `pos_partner_alt_pos_key` tinyint(2) DEFAULT NULL,
  `pos_partner_alt_keyword` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pos_credit_debit_indicator` tinyint(2) DEFAULT NULL,
  `credit_indicator` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`formatid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='mcf';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-01  1:14:44
INSERT INTO cmp_data_formats VALUES (2,'Sparda Bank','Buchungstag','Wertstellungstag','Verwendungszweck','/^\"Buchungstag\";\"Wertstellungstag\";\"Verwendungszweck\"/',';',1,NULL,4,3,'DD.MM.YYYY',',','.',NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (3,'Postbank Online','Buchungstag','Wertstellung','Umsatzart','/^\"Buchungstag\";\"Wertstellung\";\"Umsatzart\"/',';',2,6,7,4,'DD.MM.YYYY',',','.',5,3,'/^(Gutschrift|Gehalt|Dauergutschrift)/',NULL,NULL);
INSERT INTO cmp_data_formats VALUES (4,'XML camt.052.001.03',NULL,NULL,NULL,'camt','',0,NULL,0,NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (5,'Sparkasse','Buchungstag','Wertstellung','Zahlungsgegner','/^\"Buchungstag\";\"Wertstellung\";\"Zahlungsgegner\"/',';',1,3,7,6,'DD.MM.YYYY',',','.',NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (6,'Volksbank','Buchungstag','Valuta','Auftraggeber/Zahlungsempfänger','',';',1,4,12,9,'DD.MM.YYYY',',',NULL,NULL,NULL,NULL,13,'S');
INSERT INTO access (name,password,att_user,att_change_password,perm_login,perm_admin) VALUES ('admin','d033e22ae348aeb5660fc2140aec35850c4da997',1,1,1,1);
INSERT INTO access (name,password,att_user,att_change_password,perm_login,perm_admin) VALUES ('admingroup',NULL,0,0,0,0);
INSERT INTO access (name,password,att_user,att_change_password,perm_login,perm_admin) VALUES ('root','NULL',0,0,0,0);
UPDATE access SET id=0 WHERE name='root';
UPDATE access SET id=3 WHERE name='admin';
UPDATE access SET id=1 WHERE name='admingroup';
UPDATE access SET id=2 WHERE name='admin';
INSERT INTO access_relation (id,ref_id,validfrom,validtil) VALUES (1,0,'2000-01-01','2999-12-31');
INSERT INTO access_relation (id,ref_id,validfrom,validtil) VALUES (2,1,'2000-01-01','2999-12-31');
INSERT INTO access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) VALUES (2,'2000-01-01','2999-12-31',2,1,0);
INSERT INTO settings VALUES (0,'displayed_language','1'),(0,'max_rows','40'),(0,'date_format','YYYY-MM-DD'),(0,'num_free_moneyflows','1');
INSERT INTO settings (SELECT 2,name,value FROM settings WHERE mac_id=0);

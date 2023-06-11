-- MySQL dump 10.13  Distrib 8.0.32, for FreeBSD13.2 (amd64)
--
-- Host: localhost    Database: moneyflow
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8mb3_bin NOT NULL,
  `password` varchar(40) COLLATE utf8mb3_bin DEFAULT NULL,
  `att_user` tinyint unsigned NOT NULL,
  `att_change_password` tinyint unsigned NOT NULL,
  `perm_login` tinyint unsigned NOT NULL,
  `perm_admin` tinyint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac_i_01` (`name`,`att_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mac';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_relation`
--

DROP TABLE IF EXISTS `access_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access_relation` (
  `id` int unsigned NOT NULL,
  `ref_id` int unsigned NOT NULL,
  `validfrom` date NOT NULL,
  `validtil` date NOT NULL,
  PRIMARY KEY (`id`,`validfrom`),
  KEY `mar_i_01` (`ref_id`),
  CONSTRAINT `mar_mac_pk_01` FOREIGN KEY (`id`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mar_mac_pk_02` FOREIGN KEY (`ref_id`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_flattened`
--

DROP TABLE IF EXISTS `access_flattened`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access_flattened` (
  `id` int unsigned NOT NULL,
  `validfrom` date NOT NULL,
  `validtil` date NOT NULL,
  `id_level_1` int unsigned NOT NULL,
  `id_level_2` int unsigned NOT NULL,
  `id_level_3` int unsigned DEFAULT NULL,
  `id_level_4` int unsigned DEFAULT NULL,
  `id_level_5` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`,`validfrom`),
  KEY `maf_i_01` (`id_level_1`),
  KEY `maf_i_02` (`id_level_2`),
  KEY `maf_i_03` (`id_level_3`),
  KEY `maf_i_04` (`id_level_4`),
  KEY `maf_i_05` (`id_level_5`),
  CONSTRAINT `maf_mac_pk_01` FOREIGN KEY (`id`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `maf_mac_pk_02` FOREIGN KEY (`id_level_1`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `maf_mac_pk_03` FOREIGN KEY (`id_level_2`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `maf_mac_pk_04` FOREIGN KEY (`id_level_3`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `maf_mac_pk_05` FOREIGN KEY (`id_level_4`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `maf_mac_pk_06` FOREIGN KEY (`id_level_5`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='maf';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `mac_id` int unsigned NOT NULL,
  `name` varchar(50) COLLATE utf8mb3_bin NOT NULL DEFAULT '',
  `value` varchar(2048) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`name`,`mac_id`),
  KEY `mse_mac_pk` (`mac_id`),
  CONSTRAINT `mse_mac_pk` FOREIGN KEY (`mac_id`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mse';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `capitalsources`
--

DROP TABLE IF EXISTS `capitalsources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `capitalsources` (
  `capitalsourceid` int unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int unsigned NOT NULL,
  `mac_id_accessor` int unsigned NOT NULL,
  `type` tinyint NOT NULL DEFAULT '1',
  `state` tinyint NOT NULL DEFAULT '1',
  `accountnumber` varchar(34) COLLATE utf8mb3_bin DEFAULT NULL,
  `bankcode` varchar(11) COLLATE utf8mb3_bin DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `validtil` date NOT NULL DEFAULT '2999-12-31',
  `validfrom` date NOT NULL DEFAULT '1970-01-01',
  `att_group_use` tinyint unsigned NOT NULL DEFAULT '0',
  `import_allowed` tinyint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`capitalsourceid`,`mac_id_accessor`),
  KEY `mcs_mac_pk_01` (`mac_id_creator`),
  KEY `mcs_mac_pk_02` (`mac_id_accessor`),
  CONSTRAINT `mcs_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mcs_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mcs';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `postingaccounts`
--

DROP TABLE IF EXISTS `postingaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `postingaccounts` (
  `postingaccountid` int unsigned NOT NULL AUTO_INCREMENT,
  `postingaccountname` varchar(60) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`postingaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mpa';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contractpartners`
--

DROP TABLE IF EXISTS `contractpartners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contractpartners` (
  `contractpartnerid` int unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int unsigned NOT NULL,
  `mac_id_accessor` int unsigned NOT NULL,
  `name` varchar(100) COLLATE utf8mb3_bin NOT NULL DEFAULT '',
  `street` varchar(100) COLLATE utf8mb3_bin DEFAULT '',
  `postcode` int DEFAULT '0',
  `town` varchar(100) COLLATE utf8mb3_bin DEFAULT '',
  `country` varchar(100) COLLATE utf8mb3_bin DEFAULT '',
  `validfrom` date NOT NULL,
  `validtil` date NOT NULL,
  `mmf_comment` varchar(100) COLLATE utf8mb3_bin DEFAULT NULL,
  `mpa_postingaccountid` int unsigned DEFAULT NULL,
  PRIMARY KEY (`contractpartnerid`),
  UNIQUE KEY `mcp_i_01` (`mac_id_accessor`,`name`),
  KEY `mcp_mac_pk_01` (`mac_id_creator`),
  KEY `mcp_mac_pk_02` (`mac_id_accessor`),
  KEY `mcp_mpa_pk_01` (`mpa_postingaccountid`),
  CONSTRAINT `mcp_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mcp_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mcp_mpa_pk_01` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mcp';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contractpartneraccounts`
--

DROP TABLE IF EXISTS `contractpartneraccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contractpartneraccounts` (
  `contractpartneraccountid` int unsigned NOT NULL AUTO_INCREMENT,
  `mcp_contractpartnerid` int unsigned NOT NULL,
  `accountnumber` varchar(34) COLLATE utf8mb3_bin NOT NULL,
  `bankcode` varchar(11) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`contractpartneraccountid`),
  UNIQUE KEY `mca_i_01` (`accountnumber`,`bankcode`) USING BTREE,
  KEY `mca_mcp_pk_01` (`mcp_contractpartnerid`),
  CONSTRAINT `mca_mcp_pk_01` FOREIGN KEY (`mcp_contractpartnerid`) REFERENCES `contractpartners` (`contractpartnerid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mca';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `etf`
--

DROP TABLE IF EXISTS `etf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etf` (
  `isin` varchar(30) COLLATE utf8mb3_bin NOT NULL,
  `name` varchar(60) COLLATE utf8mb3_bin NOT NULL,
  `wkn` varchar(10) COLLATE utf8mb3_bin NOT NULL,
  `ticker` varchar(10) COLLATE utf8mb3_bin NOT NULL,
  `chart_url` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`isin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='met';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `etfflows`
--

DROP TABLE IF EXISTS `etfflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etfflows` (
  `etfflowid` int unsigned NOT NULL AUTO_INCREMENT,
  `flowdate` datetime(6) NOT NULL,
  `isin` varchar(30) COLLATE utf8mb3_bin NOT NULL,
  `amount` decimal(10,3) NOT NULL,
  `price` decimal(8,3) NOT NULL,
  PRIMARY KEY (`etfflowid`),
  KEY `flowdate` (`isin`,`flowdate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mef';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `etfvalues`
--

DROP TABLE IF EXISTS `etfvalues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etfvalues` (
  `isin` varchar(30) COLLATE utf8mb3_bin NOT NULL,
  `date` date NOT NULL,
  `buy_price` decimal(10,3) NOT NULL,
  `sell_price` decimal(10,3) NOT NULL,
  `changedate` datetime NOT NULL,
  UNIQUE KEY `etf_i_01` (`isin`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mev';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflows`
--

DROP TABLE IF EXISTS `moneyflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `moneyflows` (
  `moneyflowid` int unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int unsigned NOT NULL,
  `mac_id_accessor` int unsigned NOT NULL,
  `bookingdate` date NOT NULL DEFAULT '1970-01-01',
  `invoicedate` date NOT NULL DEFAULT '1970-01-01',
  `amount` float(8,2) NOT NULL DEFAULT '0.00',
  `mcs_capitalsourceid` int unsigned NOT NULL,
  `mcp_contractpartnerid` int unsigned NOT NULL,
  `comment` varchar(100) COLLATE utf8mb3_bin NOT NULL DEFAULT '',
  `mpa_postingaccountid` int unsigned NOT NULL,
  `private` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`moneyflowid`),
  KEY `mmf_i_01` (`bookingdate`,`mac_id_accessor`,`moneyflowid`),
  KEY `mmf_mac_pk_01` (`mac_id_creator`),
  KEY ` mmf_mac_pk_02` (`mac_id_accessor`),
  KEY `mmf_mcs_pk` (`mcs_capitalsourceid`),
  KEY `mmf_mcp_pk` (`mcp_contractpartnerid`),
  KEY `mmf_mpa_pk` (`mpa_postingaccountid`),
  KEY `mac_id_accessor` (`mac_id_accessor`,`bookingdate`),
  CONSTRAINT `mmf_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mmf_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mmf_mcp_pk` FOREIGN KEY (`mcp_contractpartnerid`) REFERENCES `contractpartners` (`contractpartnerid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mmf_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mmf_mpa_pk` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mmf';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflowsplitentries`
--

DROP TABLE IF EXISTS `moneyflowsplitentries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `moneyflowsplitentries` (
  `moneyflowsplitentryid` int unsigned NOT NULL AUTO_INCREMENT,
  `mmf_moneyflowid` int unsigned NOT NULL,
  `amount` decimal(8,2) NOT NULL,
  `comment` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `mpa_postingaccountid` int unsigned NOT NULL,
  PRIMARY KEY (`moneyflowsplitentryid`),
  KEY `mse_mmf_pk` (`mmf_moneyflowid`),
  KEY `mse_mpa_pk` (`mpa_postingaccountid`),
  CONSTRAINT `mse_mmf_pk` FOREIGN KEY (`mmf_moneyflowid`) REFERENCES `moneyflows` (`moneyflowid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mse_mpa_pk` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mse';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moneyflowreceipts`
--

DROP TABLE IF EXISTS `moneyflowreceipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `moneyflowreceipts` (
  `moneyflowreceiptid` int unsigned NOT NULL AUTO_INCREMENT,
  `mmf_moneyflowid` int unsigned NOT NULL,
  `receipt` mediumblob NOT NULL,
  `receipt_type` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`moneyflowreceiptid`),
  UNIQUE KEY `mrp_i_01` (`mmf_moneyflowid`) USING BTREE,
  CONSTRAINT `mrp_mmf_pk` FOREIGN KEY (`mmf_moneyflowid`) REFERENCES `moneyflows` (`moneyflowid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mrp';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `monthlysettlements`
--

DROP TABLE IF EXISTS `monthlysettlements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monthlysettlements` (
  `monthlysettlementid` int unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int unsigned NOT NULL,
  `mac_id_accessor` int unsigned NOT NULL,
  `mcs_capitalsourceid` int unsigned NOT NULL,
  `month` tinyint unsigned NOT NULL,
  `year` year NOT NULL,
  `amount` float(8,2) NOT NULL,
  PRIMARY KEY (`monthlysettlementid`),
  UNIQUE KEY `mms_i_01` (`month`,`year`,`mcs_capitalsourceid`),
  KEY `mms_mac_pk_01` (`mac_id_creator`),
  KEY `mms_mac_pk_02` (`mac_id_accessor`),
  KEY `mms_mcs_pk` (`mcs_capitalsourceid`),
  CONSTRAINT `mms_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mms_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mms_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mms';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `predefmoneyflows`
--

DROP TABLE IF EXISTS `predefmoneyflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `predefmoneyflows` (
  `predefmoneyflowid` int unsigned NOT NULL AUTO_INCREMENT,
  `mac_id` int unsigned NOT NULL,
  `amount` float(8,2) NOT NULL DEFAULT '0.00',
  `mcs_capitalsourceid` int unsigned NOT NULL,
  `mcp_contractpartnerid` int unsigned NOT NULL,
  `comment` varchar(100) COLLATE utf8mb3_bin NOT NULL DEFAULT '',
  `createdate` date NOT NULL DEFAULT '1970-01-01',
  `once_a_month` tinyint unsigned NOT NULL DEFAULT '0',
  `last_used` date DEFAULT NULL,
  `mpa_postingaccountid` int unsigned NOT NULL,
  PRIMARY KEY (`predefmoneyflowid`),
  KEY `mpm_mac_pk` (`mac_id`),
  KEY `mpm_mpa_pk` (`mpa_postingaccountid`),
  KEY `mpm_mcs_pk` (`mcs_capitalsourceid`),
  KEY `mpm_mcp_pk_01` (`mcp_contractpartnerid`),
  CONSTRAINT `mpm_mac_pk` FOREIGN KEY (`mac_id`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mpm_mcp_pk_01` FOREIGN KEY (`mcp_contractpartnerid`) REFERENCES `contractpartners` (`contractpartnerid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mpm_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mpm_mpa_pk` FOREIGN KEY (`mpa_postingaccountid`) REFERENCES `postingaccounts` (`postingaccountid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mpm';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impbalance`
--

DROP TABLE IF EXISTS `impbalance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `impbalance` (
  `mcs_capitalsourceid` int unsigned NOT NULL,
  `balance` float(8,2) NOT NULL,
  `changedate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mcs_capitalsourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mib';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmoneyflowreceipts`
--

DROP TABLE IF EXISTS `impmoneyflowreceipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `impmoneyflowreceipts` (
  `impmoneyflowreceiptid` int unsigned NOT NULL AUTO_INCREMENT,
  `mac_id_creator` int unsigned NOT NULL,
  `mac_id_accessor` int unsigned NOT NULL,
  `receipt` mediumblob NOT NULL,
  `filename` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `mediatype` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`impmoneyflowreceiptid`),
  KEY `mir_mac_pk_01` (`mac_id_creator`),
  KEY `mir_mac_pk_02` (`mac_id_accessor`),
  CONSTRAINT `mir_mac_pk_01` FOREIGN KEY (`mac_id_creator`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `mir_mac_pk_02` FOREIGN KEY (`mac_id_accessor`) REFERENCES `access` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mir';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmoneyflows`
--

DROP TABLE IF EXISTS `impmoneyflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `impmoneyflows` (
  `impmoneyflowid` int unsigned NOT NULL AUTO_INCREMENT,
  `externalid` varchar(10) COLLATE utf8mb3_bin NOT NULL,
  `mcs_capitalsourceid` int unsigned NOT NULL,
  `bookingdate` date NOT NULL,
  `invoicedate` date NOT NULL,
  `name` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `accountnumber` varchar(34) COLLATE utf8mb3_bin NOT NULL,
  `bankcode` varchar(11) COLLATE utf8mb3_bin NOT NULL,
  `comment` varchar(512) COLLATE utf8mb3_bin DEFAULT NULL,
  `amount` float(8,2) NOT NULL,
  `status` tinyint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`impmoneyflowid`),
  UNIQUE KEY `mim_i_01` (`externalid`),
  KEY `mim_mcs_pk` (`mcs_capitalsourceid`),
  CONSTRAINT `mim_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mim';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `impmonthlysettlements`
--

DROP TABLE IF EXISTS `impmonthlysettlements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `impmonthlysettlements` (
  `impmonthlysettlementid` int unsigned NOT NULL AUTO_INCREMENT,
  `externalid` varchar(10) COLLATE utf8mb3_bin NOT NULL,
  `mcs_capitalsourceid` int unsigned NOT NULL,
  `month` tinyint unsigned NOT NULL,
  `year` year NOT NULL,
  `amount` float(8,2) NOT NULL,
  PRIMARY KEY (`impmonthlysettlementid`),
  UNIQUE KEY `mit_i_01` (`externalid`) USING BTREE,
  KEY `mis_mcs_pk` (`mcs_capitalsourceid`),
  CONSTRAINT `mis_mcs_pk` FOREIGN KEY (`mcs_capitalsourceid`) REFERENCES `capitalsources` (`capitalsourceid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mit';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_data`
--

DROP TABLE IF EXISTS `imp_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imp_data` (
  `dataid` int unsigned NOT NULL AUTO_INCREMENT,
  `date` varchar(10) COLLATE utf8mb3_bin NOT NULL,
  `amount` varchar(20) COLLATE utf8mb3_bin NOT NULL,
  `source` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `partner` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `comment` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`dataid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mid';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_mapping_source`
--

DROP TABLE IF EXISTS `imp_mapping_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imp_mapping_source` (
  `source_from` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `source_to` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  UNIQUE KEY `mis_i_01` (`source_from`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mis';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imp_mapping_partner`
--

DROP TABLE IF EXISTS `imp_mapping_partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imp_mapping_partner` (
  `partner_from` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  `partner_to` varchar(100) COLLATE utf8mb3_bin NOT NULL,
  UNIQUE KEY `mip_i_01` (`partner_from`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mip';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmp_data_formats`
--

DROP TABLE IF EXISTS `cmp_data_formats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cmp_data_formats` (
  `formatid` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb3_bin NOT NULL,
  `start_trigger_0` varchar(30) COLLATE utf8mb3_bin DEFAULT NULL,
  `start_trigger_1` varchar(30) COLLATE utf8mb3_bin DEFAULT NULL,
  `start_trigger_2` varchar(30) COLLATE utf8mb3_bin DEFAULT NULL,
  `startline` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `delimiter` varchar(1) COLLATE utf8mb3_bin NOT NULL,
  `pos_date` tinyint NOT NULL,
  `pos_partner` tinyint DEFAULT NULL,
  `pos_amount` tinyint NOT NULL,
  `pos_comment` tinyint DEFAULT NULL,
  `fmt_date` varchar(10) COLLATE utf8mb3_bin NOT NULL,
  `fmt_amount_decimal` varchar(1) COLLATE utf8mb3_bin NOT NULL,
  `fmt_amount_thousand` varchar(1) COLLATE utf8mb3_bin DEFAULT NULL,
  `pos_partner_alt` tinyint DEFAULT NULL,
  `pos_partner_alt_pos_key` tinyint DEFAULT NULL,
  `pos_partner_alt_keyword` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `pos_credit_debit_indicator` tinyint DEFAULT NULL,
  `credit_indicator` varchar(2) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`formatid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='mcf';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-11  2:28:49
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

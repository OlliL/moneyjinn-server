ALTER TABLE `access` ALTER COLUMN `id` RESTART WITH 1;
ALTER TABLE `capitalsources` ALTER COLUMN `capitalsourceid` RESTART WITH 1;
ALTER TABLE `postingaccounts` ALTER COLUMN `postingaccountid` RESTART WITH 1;
ALTER TABLE `contractpartners` ALTER COLUMN `contractpartnerid` RESTART WITH 1;
ALTER TABLE `contractpartneraccounts` ALTER COLUMN `contractpartneraccountid` RESTART WITH 1;
ALTER TABLE `etfflows` ALTER COLUMN `etfflowid` RESTART WITH 1;
ALTER TABLE `moneyflows` ALTER COLUMN `moneyflowid` RESTART WITH 1;
ALTER TABLE `moneyflowsplitentries` ALTER COLUMN `moneyflowsplitentryid` RESTART WITH 1;
ALTER TABLE `moneyflowreceipts` ALTER COLUMN `moneyflowreceiptid` RESTART WITH 1;
ALTER TABLE `monthlysettlements` ALTER COLUMN `monthlysettlementid` RESTART WITH 1;
ALTER TABLE `predefmoneyflows` ALTER COLUMN `predefmoneyflowid` RESTART WITH 1;
ALTER TABLE `impmoneyflowreceipts` ALTER COLUMN `impmoneyflowreceiptid` RESTART WITH 1;
ALTER TABLE `impmoneyflows` ALTER COLUMN `impmoneyflowid` RESTART WITH 1;
ALTER TABLE `impmonthlysettlements` ALTER COLUMN `impmonthlysettlementid` RESTART WITH 1;
ALTER TABLE `imp_data` ALTER COLUMN `dataid` RESTART WITH 1;
ALTER TABLE `cmp_data_formats` ALTER COLUMN `formatid` RESTART WITH 1;

DELETE FROM `cmp_data_formats`;
DELETE FROM `imp_mapping_partner`;
DELETE FROM `imp_mapping_source`;
DELETE FROM `imp_data`;
DELETE FROM `impmonthlysettlements`;
DELETE FROM `impmoneyflows`;
DELETE FROM `impmoneyflowreceipts`;
DELETE FROM `impbalance`;
DELETE FROM `predefmoneyflows`;
DELETE FROM `monthlysettlements`;
DELETE FROM `moneyflowreceipts`;
DELETE FROM `moneyflowsplitentries`;
DELETE FROM `moneyflows`;
DELETE FROM `etfvalues`;
DELETE FROM `etfflows`;
DELETE FROM `etf`;
DELETE FROM `contractpartneraccounts`;
DELETE FROM `contractpartners`;
DELETE FROM `postingaccounts`;
DELETE FROM `capitalsources`;
DELETE FROM `settings`;
DELETE FROM `access_flattened`;
DELETE FROM `access_relation`;
DELETE FROM `access`;
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

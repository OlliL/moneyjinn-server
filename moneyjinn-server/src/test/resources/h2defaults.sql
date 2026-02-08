SET search_path TO moneyjinn;

INSERT INTO cmp_data_formats VALUES (2,'Sparda Bank','Buchungstag','Wertstellungstag','Verwendungszweck',NULL,'/^\"Buchungstag\";\"Wertstellungstag\";\"Verwendungszweck\"/',';',1,NULL,NULL,4,3,'DD.MM.YYYY',',','.',NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (3,'Postbank Online','Buchungstag','Wert','Umsatzart',NULL,'/^Buchungstag;Wert;Umsatzart/',';',2,NULL,4,12,5,'d.M.YYYY',',','.',NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (4,'XML camt.052.001.03',NULL,NULL,NULL,NULL,'camt','',0,NULL,NULL,0,NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (5,'Sparkasse','Buchungstag','Wertstellung','Zahlungsgegner',NULL,'/^\"Buchungstag\";\"Wertstellung\";\"Zahlungsgegner\"/',';',1,NULL,3,7,6,'DD.MM.YYYY',',','.',NULL,NULL,NULL,NULL,NULL);
INSERT INTO cmp_data_formats VALUES (6,'Volksbank','Buchungstag','Valuta','Auftraggeber/Zahlungsempfänger',NULL,'',';',1,NULL,4,12,9,'DD.MM.YYYY',',',NULL,NULL,NULL,NULL,13,'S');
INSERT INTO cmp_data_formats VALUES (7,'Postbank Kreditkarte','Belegdatum','Eingangstag','Verwendungszweck','Saldo:','',';',2,1,NULL,7,3,'d.M.YYYY',',','.',NULL,NULL,NULL,NULL,NULL);
INSERT INTO access_users (name,password,role,change_password) VALUES ('admin','$2a$10$DeePZ05m1PYHOK0lii2crOsPaCiaaDkd5lJWiAm2eiXTKua5lF9dW','ADMIN',1);
INSERT INTO access_groups (name) VALUES ('admingroup');
UPDATE access_users SET userid=0 WHERE name='admin';
UPDATE access_groups SET groupid=0 WHERE name='admingroup';
INSERT INTO access_relation (mau_userid,mag_groupid,validfrom,validtil) VALUES (0,0,'2000-01-01','2999-12-31');

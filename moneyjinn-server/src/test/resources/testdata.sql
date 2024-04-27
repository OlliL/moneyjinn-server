insert into access_users (userid,name,password,role,change_password) values (3,'user1','$2a$10$PQ54whtOGLu4u/arNe/X3uxTf6SiyaGGbHGzKyZfeZ/5gtdVKeiai','STANDARD',1);
insert into access_users (userid,name,password,role,change_password) values (4,'user2','$2a$10$9VlC9RqT7luBNCrXtGl5rOnJtvxW78dKx3oafsmm9W0wNnmTi1FQK','INACTIVE',0);
insert into access_users (userid,name,password,role,change_password) values (5,'user3','$2a$10$9VlC9RqT7luBNCrXtGl5rOnJtvxW78dKx3oafsmm9W0wNnmTi1FQK','STANDARD',0);

insert into access_groups (groupid,name) values (6,'group1');
insert into access_groups (groupid,name) values (7,'group2');
insert into access_groups (groupid,name) values (8,'group3');

insert into access_users (userid,name,password,role,change_password) values (9,'importuser','$2a$10$OfpEhrOVAjFieHZjTuWYtOdHm.lsTo1X8CcA1X565bcxW7u3wEwAC','IMPORT',0);

insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (3,6,'2000-01-01','2599-12-31');
insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (3,7,'2600-01-01','2699-12-31');
insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (3,6,'2700-01-01','2799-12-31');
insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (3,7,'2800-01-01','2999-12-31');

insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (4,6,'2000-01-01','2999-12-31');
insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (5,6,'2000-01-01','2999-12-31');
insert into access_relation (mau_userid,mag_groupid,validfrom,validtil) values (9,6,'2000-01-01','2999-12-31');

INSERT INTO postingaccounts (postingaccountname) VALUES ('postingaccount1');
INSERT INTO postingaccounts (postingaccountname) VALUES ('postingaccount2');
INSERT INTO postingaccounts (postingaccountname) VALUES ('xostingaccount3');

INSERT INTO capitalsources (mau_userid,mag_groupid,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (3             ,6              ,1   ,1    ,'1234567'    ,'765432','Aource1','2999-12-31','1980-01-01',0            ,1             ); 
INSERT INTO capitalsources (mau_userid,mag_groupid,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (3             ,6              ,2   ,2    ,'1234567'    ,'ABCDEFG','Source2','2799-12-31','1981-01-01',1            ,0             ); 
INSERT INTO capitalsources (mau_userid,mag_groupid,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (5             ,6              ,3   ,1    ,'ZUTVEGT1'    ,'765432','Source3','2000-12-31','1982-01-01',1            ,2             ); 
INSERT INTO capitalsources (mau_userid,mag_groupid,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (5             ,6              ,4   ,1    ,'ZUTVEGT'    ,'765432','Xource4','2010-12-31','2000-01-02',1            ,1             ); 
INSERT INTO capitalsources (mau_userid,mag_groupid,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (5             ,6              ,5   ,1    ,'ZRTVEGT'    ,'765433','Xource5','2799-12-31','2014-01-01',1            ,2             ); 
INSERT INTO capitalsources (mau_userid,mag_groupid,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (5             ,6              ,1   ,1    ,'ZRTVEGT3'   ,'765433','Xource6','2799-12-31','2000-01-01',0            ,2             ); 
                    
INSERT INTO contractpartners (mau_userid,mag_groupid,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (3             ,6              ,'Partner1','Street1',12345   ,'Town1','Country1','2000-01-01','2999-12-31','Default Comment 1',1                   );
INSERT INTO contractpartners (mau_userid,mag_groupid,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (3             ,6              ,'Qartner2','Street2',12345   ,'Town2','Country2','2000-01-01','2999-12-31',NULL               ,NULL                );
INSERT INTO contractpartners (mau_userid,mag_groupid,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (5             ,6              ,'Qartner3','Street3',12345   ,'Town3','Country3','2000-01-01','2010-12-31','Default Comment 3',2                   );
INSERT INTO contractpartners (mau_userid,mag_groupid,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (5             ,6              ,'Sartner4','Street4',12345   ,'Town4','Country4','2000-01-02','2010-12-31',NULL               ,NULL                );
INSERT INTO contractpartners (mau_userid,mag_groupid,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (0             ,0              ,'AdminPartner','Street',12345   ,'Town','Country','2000-01-01','2999-12-31',NULL               ,NULL                );

INSERT INTO contractpartneraccounts (mcp_contractpartnerid, bankcode, accountnumber)
                             VALUES (1                    ,'ABC123' , 'DE1234567890');
INSERT INTO contractpartneraccounts (mcp_contractpartnerid, bankcode, accountnumber)
                             VALUES (1                    ,'ABC456' , 'DE0987654321');
INSERT INTO contractpartneraccounts (mcp_contractpartnerid, bankcode, accountnumber)
                             VALUES (4                    ,'ABC457' , 'DE0987654322');

                             
INSERT INTO predefmoneyflows (mau_userid,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,createdate  ,once_a_month,last_used,mpa_postingaccountid) 
                      VALUES (3     ,10.10 ,1                  ,1                    ,'Pre1' ,'2000-10-10',1           ,null     ,1                   );
INSERT INTO predefmoneyflows (mau_userid,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,createdate  ,once_a_month,last_used,mpa_postingaccountid) 
                      VALUES (5     ,11    ,4                  ,3                    ,'Qre2' ,'2000-10-10',1           ,null     ,2                   );
INSERT INTO predefmoneyflows (mau_userid,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,createdate  ,once_a_month,last_used,mpa_postingaccountid) 
                      VALUES (3     ,-10   ,2                  ,2                    ,'Rre3' ,'2000-10-10',0           ,null     ,2                   );

INSERT INTO impmoneyflowreceipts (impmoneyflowreceiptid,mau_userid,mag_groupid,filename  ,mediatype   ,receipt)
                           VALUES(1                    ,3             ,6              ,'1000.jpg','image/jpeg',x'FFD8FFE000104A46494600010101012C012C0000FFFE00134372656174656420776974682047494D50FFDB0043000302020302020303030304030304050805050404050A070706080C0A0C0C0B0A0B0B0D0E12100D0E110E0B0B1016101113141515150C0F171816141812141514FFDB00430103040405040509050509140D0B0D1414141414141414141414141414141414141414141414141414141414141414141414141414141414141414141414141414FFC20011080001000103011100021101031101FFC40014000100000000000000000000000000000008FFC40014010100000000000000000000000000000000FFDA000C03010002100310000001549FFFC40014100100000000000000000000000000000000FFDA00080101000105027FFFC40014110100000000000000000000000000000000FFDA0008010301013F017FFFC40014110100000000000000000000000000000000FFDA0008010201013F017FFFC40014100100000000000000000000000000000000FFDA0008010100063F027FFFC40014100100000000000000000000000000000000FFDA0008010100013F217FFFDA000C030100020003000000109FFFC40014110100000000000000000000000000000000FFDA0008010301013F107FFFC40014110100000000000000000000000000000000FFDA0008010201013F107FFFC40014100100000000000000000000000000000000FFDA0008010100013F107FFFD9');

INSERT INTO impmoneyflowreceipts (impmoneyflowreceiptid,mau_userid,mag_groupid,filename  ,mediatype   ,receipt)
                           VALUES(2                    ,3             ,6              ,'2000.jpg','application/pdf',x'255044462D312E350A25B5EDAEFB0A342030206F626A0A3C3C202F4C656E6774682035203020520A2020202F46696C746572202F466C6174654465636F64650A3E3E0A73747265616D0A789C33543000425D4310A56764A2909CCB55C815C8050030B004540A656E6473747265616D0A656E646F626A0A352030206F626A0A20202032370A656E646F626A0A332030206F626A0A3C3C0A3E3E0A656E646F626A0A322030206F626A0A3C3C202F54797065202F50616765202520310A2020202F506172656E742031203020520A2020202F4D65646961426F78205B2030203020302E323420302E3234205D0A2020202F436F6E74656E74732034203020520A2020202F47726F7570203C3C0A2020202020202F54797065202F47726F75700A2020202020202F53202F5472616E73706172656E63790A2020202020202F4920747275650A2020202020202F4353202F4465766963655247420A2020203E3E0A2020202F5265736F75726365732033203020520A3E3E0A656E646F626A0A312030206F626A0A3C3C202F54797065202F50616765730A2020202F4B696473205B203220302052205D0A2020202F436F756E7420310A3E3E0A656E646F626A0A362030206F626A0A3C3C202F50726F64756365722028636169726F20312E31352E31322028687474703A2F2F636169726F67726170686963732E6F726729290A2020202F4372656174696F6E446174652028443A32303232313232393030353433312B3031273030290A3E3E0A656E646F626A0A372030206F626A0A3C3C202F54797065202F436174616C6F670A2020202F50616765732031203020520A3E3E0A656E646F626A0A787265660A3020380A303030303030303030302036353533352066200A30303030303030333831203030303030206E200A30303030303030313631203030303030206E200A30303030303030313430203030303030206E200A30303030303030303135203030303030206E200A30303030303030313139203030303030206E200A30303030303030343436203030303030206E200A30303030303030353632203030303030206E200A747261696C65720A3C3C202F53697A6520380A2020202F526F6F742037203020520A2020202F496E666F2036203020520A3E3E0A7374617274787265660A3631340A2525454F460A');
INSERT INTO impmonthlysettlements (externalid,mcs_capitalsourceid,`month`,`year`,amount)
                           VALUES ('A'       ,4                  ,5    ,2010,9);

INSERT INTO impbalance (mcs_capitalsourceid,balance,changedate)
                VALUES (4                  ,9      ,'2009-12-01 20:20:20');

INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name  ,accountnumber ,bankcode,comment ,amount)
                   VALUES ('8765421A',1                  ,'2010-01-02','2010-01-01','Paul','DE1234567890','ABC123','secret',10.10);
INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name  ,accountnumber ,bankcode,comment ,amount)
                   VALUES ('8765421B',1                  ,'2010-01-02','2010-01-01','Jane','888888888888','999999','code',-20.20);

INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name     ,accountnumber ,bankcode,comment    ,amount ,status)
                   VALUES ('9999999A',2                  ,'2010-05-03','2010-05-03','Partner','DE1234567890','ABC123','generated',-5.00  ,1);
INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name     ,accountnumber ,bankcode,comment    ,amount ,status)
                   VALUES ('9999999B',2                  ,'2010-05-20','2010-05-20','Partner','DE0987654321','ABC456','generated',-5.00  ,1);
INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name     ,accountnumber ,bankcode,comment    ,amount ,status)
                   VALUES ('9999999C',2                  ,'2010-05-05','2010-05-05','Sartner','DE0987654322','ABC457','generated',-5.00  ,1);
INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name     ,accountnumber ,bankcode,comment    ,amount ,status)
                   VALUES ('oldthing',1                  ,'1999-01-02','1999-01-01','Paul'   ,'DE1234567890','ABC123','secret'   ,10.10  ,1);
INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name     ,accountnumber ,bankcode,comment    ,amount ,status)
                   VALUES ('newthing',1                  ,'2600-01-02','2600-01-01','Paul'   ,'DE1234567890','ABC123','secret'   ,10.10  ,1);

                   
INSERT INTO `moneyflows` VALUES (1,3,6,'2009-01-01','2009-01-01',-1.10,1,1,'flow1',1,0);
INSERT INTO `moneyflows` VALUES (2,3,6,'2008-12-01','2008-12-01',10.10,1,1,'generated',1,0);
INSERT INTO `moneyflows` VALUES (3,3,6,'2009-02-01','2009-02-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (4,3,6,'2009-03-01','2009-03-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (5,3,6,'2009-04-01','2009-04-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (6,3,6,'2009-05-01','2009-05-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (7,3,6,'2009-06-01','2009-06-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (8,3,6,'2009-07-01','2009-07-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (9,3,6,'2009-08-01','2009-08-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (10,3,6,'2009-09-01','2009-09-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (11,3,6,'2009-10-01','2009-10-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (12,3,6,'2009-11-01','2009-11-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (13,3,6,'2009-12-01','2009-12-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (14,3,6,'2010-01-01','2010-01-01',-10.00,2,1,'generated',2,0);
INSERT INTO `moneyflows` VALUES (15,3,6,'2010-02-01','2010-02-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (16,3,6,'2010-03-01','2010-03-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (17,3,6,'2010-04-01','2010-04-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (18,3,6,'2010-05-03','2010-05-03',-5.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (19,3,6,'2010-05-01','2010-05-01',-5.00,2,2,'generated',2,0);

INSERT INTO moneyflowsplitentries VALUES (1,1,-1,'split1',1);
INSERT INTO moneyflowsplitentries VALUES (2,1,-.1,'split2',2);

INSERT INTO moneyflowreceipts VALUES (1,1,'FFFFFFFF',1);
INSERT INTO moneyflowreceipts VALUES (2,2,'FFFFFFFF',2);

INSERT INTO `monthlysettlements` VALUES (1,3,6,1,12,2008,10.00);
INSERT INTO `monthlysettlements` VALUES (2,3,6,2,12,2008,100.00);
INSERT INTO `monthlysettlements` VALUES (3,5,6,4,12,2008,1000.00);
INSERT INTO `monthlysettlements` VALUES (4,3,6,1,11,2008,-0.10);
INSERT INTO `monthlysettlements` VALUES (5,3,6,2,11,2008,100.00);
INSERT INTO `monthlysettlements` VALUES (6,5,6,4,11,2008,1000.00);
INSERT INTO `monthlysettlements` VALUES (7,3,6,1,1,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (8,3,6,2,1,2009,100.00);
INSERT INTO `monthlysettlements` VALUES (9,5,6,4,1,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (10,3,6,1,2,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (11,3,6,2,2,2009,110.00);
INSERT INTO `monthlysettlements` VALUES (12,5,6,4,2,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (13,3,6,1,3,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (14,3,6,2,3,2009,100.00);
INSERT INTO `monthlysettlements` VALUES (15,5,6,4,3,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (16,3,6,1,4,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (17,3,6,2,4,2009,110.00);
INSERT INTO `monthlysettlements` VALUES (18,5,6,4,4,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (19,3,6,1,5,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (20,3,6,2,5,2009,100.00);
INSERT INTO `monthlysettlements` VALUES (21,5,6,4,5,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (22,3,6,1,6,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (23,3,6,2,6,2009,110.00);
INSERT INTO `monthlysettlements` VALUES (24,5,6,4,6,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (25,3,6,1,7,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (26,3,6,2,7,2009,100.00);
INSERT INTO `monthlysettlements` VALUES (27,5,6,4,7,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (28,3,6,1,8,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (29,3,6,2,8,2009,110.00);
INSERT INTO `monthlysettlements` VALUES (30,5,6,4,8,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (31,3,6,1,9,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (32,3,6,2,9,2009,100.00);
INSERT INTO `monthlysettlements` VALUES (33,5,6,4,9,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (34,3,6,1,10,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (35,3,6,2,10,2009,110.00);
INSERT INTO `monthlysettlements` VALUES (36,5,6,4,10,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (37,3,6,1,11,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (38,3,6,2,11,2009,100.00);
INSERT INTO `monthlysettlements` VALUES (39,5,6,4,11,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (40,3,6,1,12,2009,8.90);
INSERT INTO `monthlysettlements` VALUES (41,3,6,2,12,2009,110.00);
INSERT INTO `monthlysettlements` VALUES (42,5,6,4,12,2009,1000.00);
INSERT INTO `monthlysettlements` VALUES (43,3,6,1,1,2010,8.90);
INSERT INTO `monthlysettlements` VALUES (44,3,6,2,1,2010,100.00);
INSERT INTO `monthlysettlements` VALUES (45,3,6,1,2,2010,8.90);
INSERT INTO `monthlysettlements` VALUES (46,3,6,2,2,2010,110.00);
INSERT INTO `monthlysettlements` VALUES (47,3,6,1,3,2010,8.90);
INSERT INTO `monthlysettlements` VALUES (48,3,6,2,3,2010,100.00);
INSERT INTO `monthlysettlements` VALUES (49,5,6,4,3,2010,1000.00);
INSERT INTO `monthlysettlements` VALUES (50,3,6,2,4,2010,110.00);
INSERT INTO `monthlysettlements` VALUES (51,5,6,4,4,2010,1000.00);

INSERT INTO etf       VALUES ( 'ISIN123', 'name456', 'WKN789', 'TKR0', 'https://www.lipsum.com/');
INSERT INTO etfflows  VALUES ( 1, '2008-12-13 15:16:20.320000', 'ISIN123',  30.000, 777.666);
INSERT INTO etfflows  VALUES ( 2, '2008-12-14 15:16:20.320000', 'ISIN123', 100.000, 777.666);
INSERT INTO etfflows  VALUES ( 3, '2008-12-15 15:16:20.320000', 'ISIN123', -50.000, 877.000);
INSERT INTO etfflows  VALUES ( 4, '2008-12-16 15:16:20.320000', 'ISIN123',   1.234, 666.123);
INSERT INTO etfflows  VALUES ( 5, '2009-01-31 23:59:59.999000', 'ISIN123',   5.500, 789.123);
INSERT INTO etfflows  VALUES ( 6, '2009-02-20 23:59:59.999000', 'ISIN123',   6.500, 889.123);
INSERT INTO etfflows  VALUES ( 7, '2009-12-12 15:16:20.320000', 'ISIN123', -81.000, 877.000);
INSERT INTO etfflows  VALUES ( 8, '2010-01-01 15:16:20.320000', 'ISIN123',  81.000, 777.000);
INSERT INTO etfflows  VALUES ( 9, '2010-02-02 15:16:20.320000', 'ISIN123',  80.000, 777.000);
INSERT INTO etfflows  VALUES (10, '2010-02-03 15:16:20.320000', 'ISIN123', -10.000, 760.000);
INSERT INTO etfflows  VALUES (11, '2010-02-04 15:16:20.320000', 'ISIN123',  30.000, 750.000);
INSERT INTO etfvalues VALUES ( 'ISIN123', '2008-12-14',777.777, 777.000, '2008-12-14 22:05:02');
INSERT INTO etfvalues VALUES ( 'ISIN123', '2008-12-15',878.000, 878.500, '2008-12-15 22:05:02');
INSERT INTO etfvalues VALUES ( 'ISIN123', '2010-01-16',666.000, 666.543, '2012-01-16 22:05:02');
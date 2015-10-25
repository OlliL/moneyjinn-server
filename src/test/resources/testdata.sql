insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (3,'user1','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2',1,1,1,0);
insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (4,'user2','1c6637a8f2e1f75e06ff9984894d6bd16a3a36a9',1,0,0,0);
insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (5,'user3','1c6637a8f2e1f75e06ff9984894d6bd16a3a36a9',1,0,1,0);

insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (6,'group1',null,0,0,0,0);
insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (7,'group2',null,0,0,0,0);
insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (8,'group3',null,0,0,0,0);

insert into access_relation (id,ref_id,validfrom,validtil) values (6,0,'2000-01-01','2999-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (7,0,'2000-01-01','2999-12-31');

insert into access_relation (id,ref_id,validfrom,validtil) values (3,6,'2000-01-01','2599-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (3,7,'2600-01-01','2699-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (3,6,'2700-01-01','2799-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (3,7,'2800-01-01','2999-12-31');

insert into access_relation (id,ref_id,validfrom,validtil) values (4,6,'2000-01-01','2999-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (5,6,'2000-01-01','2999-12-31');

insert into access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) values (3,'2000-01-01','2599-12-31',3,6,0);
insert into access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) values (3,'2600-01-01','6599-12-31',3,7,0);
insert into access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) values (3,'2700-01-01','7599-12-31',3,6,0);
insert into access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) values (3,'2800-01-01','9599-12-31',3,7,0);
insert into access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) values (4,'2000-01-01','2999-12-31',4,6,0);
insert into access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) values (5,'2000-01-01','2999-12-31',5,6,0);

INSERT INTO settings (SELECT 3,name,value FROM settings WHERE mac_id=0);
INSERT INTO settings (SELECT 4,name,value FROM settings WHERE mac_id=0);
INSERT INTO settings (SELECT 5,name,value FROM settings WHERE mac_id=0);

UPDATE settings set value = '1' where mac_id=3 and name = 'max_rows';

INSERT INTO postingaccounts (postingaccountname) VALUES ('postingaccount1');
INSERT INTO postingaccounts (postingaccountname) VALUES ('postingaccount2');
INSERT INTO postingaccounts (postingaccountname) VALUES ('xostingaccount3');

INSERT INTO capitalsources (mac_id_creator,mac_id_accessor,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (3             ,6              ,1   ,1    ,'1234567'    ,'765432','Aource1','2999-12-31','1980-01-01',0            ,1             ); 
INSERT INTO capitalsources (mac_id_creator,mac_id_accessor,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (3             ,6              ,2   ,2    ,'1234567'    ,'ABCDEFG','Source2','2799-12-31','1981-01-01',1            ,0             ); 
INSERT INTO capitalsources (mac_id_creator,mac_id_accessor,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (5             ,6              ,3   ,1    ,'ZUTVEGT'    ,'765432','Source3','2000-12-31','1982-01-01',1            ,1             ); 
INSERT INTO capitalsources (mac_id_creator,mac_id_accessor,type,state,accountnumber,bankcode,comment  ,validtil    ,validfrom   ,att_group_use,import_allowed)
                    VALUES (5             ,6              ,4   ,1    ,'ZUTVEGT'    ,'765432','Xource4','2010-12-31','1983-01-01',1            ,1             ); 
                    
INSERT INTO contractpartners (mac_id_creator,mac_id_accessor,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (3             ,6              ,'Partner1','Street1',12345   ,'Town1','Country1','2000-01-01','2999-12-31','Default Comment 1',1                   );
INSERT INTO contractpartners (mac_id_creator,mac_id_accessor,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (3             ,6              ,'Qartner2','Street2',12345   ,'Town2','Country2','2000-01-01','2999-12-31',NULL               ,NULL                );
INSERT INTO contractpartners (mac_id_creator,mac_id_accessor,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (5             ,6              ,'Qartner3','Street3',12345   ,'Town3','Country3','2000-01-01','2010-12-31','Default Comment 3',2                   );
INSERT INTO contractpartners (mac_id_creator,mac_id_accessor,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (5             ,6              ,'Sartner4','Street4',12345   ,'Town4','Country4','2000-01-01','2010-12-31',NULL               ,NULL                );
INSERT INTO contractpartners (mac_id_creator,mac_id_accessor,name      ,street   ,postcode,town    ,country  ,validfrom   ,validtil    ,mmf_comment        ,mpa_postingaccountid)
                      VALUES (2             ,1              ,'AdminPartner','Street',12345   ,'Town','Country','2000-01-01','2999-12-31',NULL               ,NULL                );

INSERT INTO contractpartneraccounts (mcp_contractpartnerid, bankcode, accountnumber)
                             VALUES (1                    ,'ABC123' , 'DE1234567890');
INSERT INTO contractpartneraccounts (mcp_contractpartnerid, bankcode, accountnumber)
                             VALUES (1                    ,'ABC456' , 'DE0987654321');
INSERT INTO contractpartneraccounts (mcp_contractpartnerid, bankcode, accountnumber)
                             VALUES (4                    ,'ABC457' , 'DE0987654322');

                             
INSERT INTO predefmoneyflows (mac_id,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,createdate  ,once_a_month,last_used,mpa_postingaccountid) 
                      VALUES (3     ,10.10 ,1                  ,1                    ,'Pre1' ,'2000-10-10',1           ,null     ,1                   );
INSERT INTO predefmoneyflows (mac_id,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,createdate  ,once_a_month,last_used,mpa_postingaccountid) 
                      VALUES (5     ,11    ,4                  ,3                    ,'Qre2' ,'2000-10-10',1           ,null     ,2                   );
INSERT INTO predefmoneyflows (mac_id,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,createdate  ,once_a_month,last_used,mpa_postingaccountid) 
                      VALUES (3     ,-10   ,2                  ,2                    ,'Rre3' ,'2000-10-10',0           ,null     ,2                   );

INSERT INTO impmonthlysettlements (externalid,mcs_capitalsourceid,month,year,amount)
                           VALUES ('A'       ,4                  ,5    ,2010,9);

INSERT INTO impbalance (mcs_capitalsourceid,balance,changedate)
                VALUES (4                  ,9      ,'2009-12-01 20:20:20');

INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name  ,accountnumber ,bankcode,comment ,amount)
                   VALUES ('8765421A',1                  ,'2010-01-02','2010-01-01','Paul','DE1234567890','ABC123','secret',10.10);
INSERT INTO impmoneyflows (externalid,mcs_capitalsourceid,bookingdate ,invoicedate ,name  ,accountnumber ,bankcode,comment ,amount)
                   VALUES ('8765421B',1                  ,'2010-01-02','2010-01-01','Jane','888888888888','999999','code',-20.20);

INSERT INTO `moneyflows` VALUES (1,3,6,'2009-01-01','2009-01-01',-1.10,1,1,'flow1',1,0);
INSERT INTO `moneyflows` VALUES (2,3,6,'2008-12-25','2008-12-25',10.10,1,1,'Pre1',1,0);
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
INSERT INTO `moneyflows` VALUES (14,3,6,'2010-01-01','2010-01-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (15,3,6,'2010-02-01','2010-02-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (16,3,6,'2010-03-01','2010-03-01',-10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (17,3,6,'2010-04-01','2010-04-01',10.00,2,1,'generated',2,1);
INSERT INTO `moneyflows` VALUES (18,3,6,'2010-05-01','2010-05-01',-10.00,2,1,'generated',2,1);
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
INSERT INTO `monthlysettlements` VALUES (47,5,6,4,2,2010,1000.00);
INSERT INTO `monthlysettlements` VALUES (48,3,6,1,3,2010,8.90);
INSERT INTO `monthlysettlements` VALUES (49,3,6,2,3,2010,100.00);
INSERT INTO `monthlysettlements` VALUES (50,5,6,4,3,2010,1000.00);
INSERT INTO `monthlysettlements` VALUES (51,3,6,1,4,2010,8.90);
INSERT INTO `monthlysettlements` VALUES (52,3,6,2,4,2010,110.00);
INSERT INTO `monthlysettlements` VALUES (53,5,6,4,4,2010,1000.00);

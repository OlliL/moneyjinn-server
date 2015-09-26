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

INSERT INTO moneyflows (mac_id_creator,mac_id_accessor,bookingdate ,invoicedate ,amount,mcs_capitalsourceid,mcp_contractpartnerid,comment,mpa_postingaccountid,private)
                VALUES (3             ,6              ,'2009-01-01','2009-01-01',-1.10 ,1                  ,1                    ,'flow1',1                   ,0);
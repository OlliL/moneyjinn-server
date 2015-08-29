insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (3,'user1','111',1,1,1,1);
insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (4,'user2','222',1,0,0,0);

insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (5,'group1',null,0,0,0,0);
insert into access (id,name,password,att_user,att_change_password,perm_login,perm_admin) values (6,'group2',null,0,0,0,0);

insert into access_relation (id,ref_id,validfrom,validtil) values (5,0,'2000-01-01','2999-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (6,0,'2000-01-01','2999-12-31');

insert into access_relation (id,ref_id,validfrom,validtil) values (3,5,'2000-01-01','2500-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (3,6,'2600-01-01','2600-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (3,5,'2700-01-01','2700-12-31');
insert into access_relation (id,ref_id,validfrom,validtil) values (3,6,'2800-01-01','2999-12-31');

insert into access_relation (id,ref_id,validfrom,validtil) values (4,5,'2000-01-01','2999-12-31');

INSERT INTO settings (SELECT 3,name,value FROM settings WHERE mac_id=0);
INSERT INTO settings (SELECT 4,name,value FROM settings WHERE mac_id=0);

UPDATE settings set value = '1' where mac_id=4 and name = 'max_rows';
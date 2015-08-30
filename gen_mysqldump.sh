#!/bin/sh -e

PROGNAME=$(basename $0)
PROGPATH=$0
if [ $PROGNAME = $PROGPATH -o $PROGPATH = '.' ] ; then
        PROGPATH=$(pwd)
else
        [ -z "$(echo "$PROGPATH" | sed 's![^/]!!g')" ] && \
        PROGPATH=$(type "$PROGPATH" | sed 's/^.* //g')
        PROGPATH=$(dirname $PROGPATH)
fi

mysqldump -u moneyflow -pmoneyflow -h db --skip-quote-names --skip-triggers --default-character-set=latin1 --tables --add-drop-table --single-transaction --no-data moneyflow \
	access \
	access_relation \
	access_flattened \
	settings \
	capitalsources \
	postingaccounts \
	contractpartners \
	contractpartneraccounts \
	moneyflows \
	monthlysettlements \
	predefmoneyflows \
	impbalance \
	impmoneyflows \
	impmonthlysettlements \
	imp_data \
	imp_mapping_source \
	imp_mapping_partner \
	cmp_data_formats \
		| awk '
	{
		if( $1 == ")" ) {
			printf("%s",$1)
			for( i=2 ; i <= NF ; i++ ) {
				if( $i !~ /AUTO_INCREMENT=[0-9]*/ )
					printf (" %s",$i)
			}
			printf("\n")
		} else {
			print
		}
	}' > ${PROGPATH}/mysqldump.sql

mysqldump -u moneyflow -pmoneyflow -h db --skip-quote-names --skip-extended-insert --skip-triggers --default-character-set=latin1 --single-transaction --tables moneyflow \
	cmp_data_formats \
		|grep INSERT >> ${PROGPATH}/mysqldump.sql

cat << EOF >> ${PROGPATH}/mysqldump.sql
INSERT INTO access (name,password,att_user,att_change_password,perm_login,perm_admin) VALUES ('admin','d033e22ae348aeb5660fc2140aec35850c4da997',1,1,1,1);
INSERT INTO access (name,password,att_user,att_change_password,perm_login,perm_admin) VALUES ('admingroup',NULL,0,0,0,0);
INSERT INTO access (name,password,att_user,att_change_password,perm_login,perm_admin) VALUES ('root','NULL',0,0,0,0);
UPDATE access SET id=0 WHERE name='root';
UPDATE access SET id=3 WHERE name='admin';
UPDATE access SET id=1 WHERE name='admingroup';
UPDATE access SET id=2 WHERE name='admin';
INSERT INTO access_relation (id,ref_id,validfrom,validtil) VALUES (1,0,'0001-01-01','2999-12-31');
INSERT INTO access_relation (id,ref_id,validfrom,validtil) VALUES (2,1,'0001-01-01','2999-12-31');
INSERT INTO access_flattened (id,validfrom,validtil,id_level_1,id_level_2,id_level_3) VALUES (2,'0001-01-01','2999-12-31',2,1,0);
INSERT INTO settings VALUES (0,'displayed_language','1'),(0,'max_rows','40'),(0,'date_format','YYYY-MM-DD'),(0,'num_free_moneyflows','1');
INSERT INTO settings (SELECT 2,name,value FROM settings WHERE mac_id=0);
EOF

sed -i.bak "s/\\\'/''/g" ${PROGPATH}/mysqldump.sql && rm -f ${PROGPATH}/mysqldump.sql.bak

awk '
/\) ENGINE/ {
	print ");"
	next
}

/ COLLATE / {
	sub(/ COLLATE [^ ]*[ ,]/," ")
}

/ float\(/ {
	sub(/ float\(/," decimal(")
}

/ ON UPDATE CURRENT_TIMESTAMP/ {
	sub(/ ON UPDATE CURRENT_TIMESTAMP/,"")
}

/ USING BTREE/ {
	sub(/ USING BTREE/,"")
}

/^-- Dump completed/ {
	exit
}

{
	print
}
' <mysqldump.sql >h2dump.sql


awk '
BEGIN { 
	i=0
}

/^CREATE TABLE / {
	a[i]="DELETE FROM "$3";"
	table=$3
	i++
}

/ AUTO_INCREMENT/ {
	print "ALTER TABLE " table " ALTER COLUMN " $1 " RESTART WITH 1;";
}

END {
	for(;i>=0;i--) {
		print a[i]
	}
}' < mysqldump.sql > h2defaults.sql

awk '
/^(INSERT|UPDATE) / {
	print
}
' <mysqldump.sql >>h2defaults.sql

awk '
/ SQL SECURITY INVOKER/ {
	sub(/ SQL SECURITY INVOKER/,"")
}
/DELIMITER \$\$/ {
	exit
}

{
	print
}
' <mysqlext.sql >h2ext.sql

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

mysqldump -u root --set-gtid-purged=OFF --no-tablespaces --skip-triggers --quote-names --default-character-set=utf8 --tables --add-drop-table --single-transaction --no-data moneyflow \
	access_users \
	access_groups \
	access_relation \
	settings \
	capitalsources \
	postingaccounts \
	contractpartners \
	contractpartneraccounts \
	etf \
	etfflows \
	etfvalues \
	etfpreliminarylumpsum \
	moneyflows \
	moneyflowsplitentries \
	moneyflowreceipts \
	monthlysettlements \
	predefmoneyflows \
	impbalance \
	impmoneyflowreceipts \
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

mysqldump -u root --set-gtid-purged=OFF --no-tablespaces --skip-quote-names --skip-extended-insert --skip-triggers --default-character-set=utf8 --single-transaction --tables moneyflow \
	cmp_data_formats \
		|grep INSERT >> ${PROGPATH}/mysqldump.sql

cat << EOF >> ${PROGPATH}/mysqldump.sql
INSERT INTO access_users (name,password,role,change_password) VALUES ('admin','\$2a\$10\$DeePZ05m1PYHOK0lii2crOsPaCiaaDkd5lJWiAm2eiXTKua5lF9dW','ADMIN',1);
INSERT INTO access_groups (name) VALUES ('admingroup');
UPDATE access_users SET userid=0 WHERE name='admin';
UPDATE access_groups SET groupid=0 WHERE name='admingroup';
INSERT INTO access_relation (mau_userid,mag_groupid,validfrom,validtil) VALUES (0,0,'2000-01-01','2999-12-31');
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

/ CHARACTER SET / {
	sub(/ CHARACTER SET [^ ]*[ ,]/," ")
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

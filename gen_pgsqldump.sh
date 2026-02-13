#!/bin/sh

PROGNAME=$(basename $0)
PROGPATH=$0
if [ $PROGNAME = $PROGPATH -o $PROGPATH = '.' ] ; then
        PROGPATH=$(pwd)
else
        [ -z "$(echo "$PROGPATH" | sed 's![^/]!!g')" ] && \
        PROGPATH=$(type "$PROGPATH" | sed 's/^.* //g')
        PROGPATH=$(dirname $PROGPATH)
fi

pg_dump -U postgres -d moneyjinn --schema-only --clean > ${PROGPATH}/pgsqldump.sql
pg_dump -U postgres -d moneyjinn --column-inserts --data-only --table=moneyjinn.cmp_data_formats | grep -E '(SELECT|INSERT)' >> ${PROGPATH}/pgsqldump.sql
cat << EOF >> ${PROGPATH}/pgsqldump.sql
INSERT INTO moneyjinn.access_users (name,password,role,change_password) VALUES ('admin','\$2a\$10\$DeePZ05m1PYHOK0lii2crOsPaCiaaDkd5lJWiAm2eiXTKua5lF9dW','ADMIN',1);
INSERT INTO moneyjinn.access_groups (name) VALUES ('admingroup');
UPDATE moneyjinn.access_users SET userid=0 WHERE name='admin';
UPDATE moneyjinn.access_groups SET groupid=0 WHERE name='admingroup';
INSERT INTO moneyjinn.access_relation (mau_userid,mag_groupid,validfrom,validtil) VALUES (0,0,'2000-01-01','2999-12-31');
EOF

awk '
/ moneyjinn_hbci\./ {
  next
}
{
	gsub(/bigint/, "bigserial")
	gsub(/moneyjinn./, "")
	gsub(/ value /, " \"value\" ")
	gsub(/ year /, " \"year\" ")
	gsub(/ year\)/, " \"year\")")
	gsub(/ year,/, " \"year\",")
	gsub(/\.year,/, ".\"year\",")
	gsub(/ month /, " \"month\" ")
	gsub(/\.month\)/, ".\"month\")")
	gsub(/\.month,/, ".\"month\",")
	gsub(/\(month,/, "(\"month\",")
	gsub(/WITH \(security.*\)/,"")
	gsub(/'"'"'1 mon'"'"'::interval/,"INTERVAL '"'"'1'"'"' month")
	gsub(/'"'"'1 day'"'"'::interval/,"INTERVAL '"'"'1'"'"' day")
	gsub(/ALTER TABLE ONLY/,"ALTER TABLE")
}

/^CREATE (TABLE|VIEW)/ {
	printStuff=1
	print
	next
}

/^ALTER TABLE [^ ]*$/ {
	printStuff=1
	print
	next
}

/^CREATE INDEX/ {
	print
	next
}


/;$/ {
	if(printStuff == 1) {
		printStuff=0
		print
		next
	}
}

{
	if(printStuff == 1) {
		print
	}
}' < ${PROGPATH}/pgsqldump.sql > ${PROGPATH}/h2dump.sql

awk '
/^(INSERT|UPDATE) / {
  gsub(/moneyjinn./, "")
	print
}
' <${PROGPATH}/pgsqldump.sql > ${PROGPATH}/h2defaults.sql


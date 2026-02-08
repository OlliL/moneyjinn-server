#!/bin/sh

#pg_dump -U postgres -d moneyjinn --schema-only --clean > pgsqldump.sql

awk '
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
}' < pgsqldump.sql > h2dump.sql

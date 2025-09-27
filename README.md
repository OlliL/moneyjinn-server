# moneyjinn

<a href="https://github.com/OlliL/moneyjinn-server/actions/workflows/codeql.yml"><img src="https://github.com/OlliL/moneyjinn-server/workflows/CodeQL/badge.svg" alt="build status"></a> <a href='https://coveralls.io/github/OlliL/moneyjinn-server?branch=master'><img src='https://coveralls.io/repos/github/OlliL/moneyjinn-server/badge.svg?branch=master' alt='Coverage Status' /></a>

# Installation

- create a MySQL DB
    - `create database moneyflow;`
- create a MySQL user to be used by moneyflow (SELECT, INSERT, UPDATE, DELETE privileges are sufficient)
    -
    `GRANT SELECT,INSERT,UPDATE,DELETE ON moneyflow.* TO moneyflow@localhost IDENTIFIED BY 'moneyflow' WITH GRANT OPTION;`
- change DATASOURCE in moneyjinn-server/src/main/resources/application.properties
- execute mysqldump.sql
    - `mysql -u root -p<password> -h <dbhost> <dbname> < mysqldump.sql`
- execute mysqlext.sql
    - `mysql -u root -p<password> -h <dbhost> <dbname> < mysqlext.sql`
- configure client (see <a href="https://github.com/OlliL/moneyjinn-client/blob/main/README.md">Client README</a>)

# Notes

- execute piest for moneyjinn-server:
    - `mvn test-compile org.pitest:pitest-maven:mutationCoverage -Dverbose`
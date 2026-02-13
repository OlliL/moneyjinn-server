# moneyjinn

<a href="https://github.com/OlliL/moneyjinn-server/actions/workflows/codeql.yml"><img src="https://github.com/OlliL/moneyjinn-server/workflows/CodeQL/badge.svg" alt="build status"></a> <a href='https://coveralls.io/github/OlliL/moneyjinn-server?branch=master'><img src='https://coveralls.io/repos/github/OlliL/moneyjinn-server/badge.svg?branch=master' alt='Coverage Status' /></a>

# Installation

- create new schemas in your database
    - `$ psql -U postgres`
   ```
   CREATE SCHEMA moneyjinn;
   CREATE SCHEMA moneyjinn_hbci;
   ```
- create the new user/roles and transfer schema ownership to the schema owners
    - `$ psql -U postgres`
   ```
   CREATE ROLE moneyjinn_owner;
   CREATE ROLE moneyjinn_hbci_owner;
   ALTER SCHEMA moneyjinn OWNER TO moneyjinn_owner;
   ALTER SCHEMA moneyjinn_hbci OWNER TO moneyjinn_hbci_owner;
   CREATE USER moneyjinn_app WITH PASSWORD '<your password goes here>';
   CREATE USER moneyjinn_hbci_app WITH PASSWORD '<your password goes here>';
   GRANT CONNECT ON DATABASE moneyjinn TO moneyjinn_app;
   GRANT CONNECT ON DATABASE moneyjinn TO moneyjinn_hbci_app;
   ```
- load DB Schema and initial Data
    - `$ psql -U postgres -f pgsqldump.sql`
- configure client (see <a href="https://github.com/OlliL/moneyjinn-client/blob/main/README.md">Client README</a>)

# Notes

- execute pitest for moneyjinn-server:
    - `mvn test-compile org.pitest:pitest-maven:mutationCoverage -Dverbose`
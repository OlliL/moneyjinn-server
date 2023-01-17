# moneyjinn-hbci-batch

moneyjinn-hbci-batch utilizes [hbci4java](https://github.com/willuhn/hbci4java) to collect several data from the bank accounts specified. It stores the collected information in a database and notifies all given Observers to for further data processing.

## Setup

# database

- create a database
- execute `src/main/resources/database.sql` to create all needed tables
- create a user with insert/select rights on the created tables

# properties
- TODO: explain how to build the propperties file here...
  * hbci.passport.password
    * must be the password to all of your passport files
  * hbci.PASSPORT-FILENAME.pin
    * replace PASSPORT-FILENAME with the filename of your passport file
    * generate the passport file with [InitAndTest.java](https://github.com/hbci4j/hbci4java/blob/master/src/main/java/org/kapott/hbci/tools/InitAndTest.java) supplied with hbci4java
    * must contain the pin for logging in to the bank account specified in the passport file
    * specify as much of this properties as you have passport files
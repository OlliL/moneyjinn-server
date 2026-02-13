# moneyjinn-hbci-batch

moneyjinn-hbci-batch utilizes [hbci4java](https://github.com/willuhn/hbci4java) to collect several data from the bank
accounts specified. It stores the collected information in a database and notifies all given Observers to for further
data processing.

# Setup

## properties

- TODO: explain how to build the propperties file here...
    * hbci.PASSPORT-FILENAME.pin
        * replace PASSPORT-FILENAME with the filename of your passport file
        * generate the passport file
          with [InitAndTest.java](https://github.com/hbci4j/hbci4java/blob/master/src/main/java/org/kapott/hbci/tools/InitAndTest.java)
          supplied with hbci4java
        * must contain the pin for logging in to the bank account specified in the passport file
        * specify as much of these properties as you have passport files
    * hbci.PASSPORT-FILENAME.password
        * must be the password to your passport file
    * hbci.server.username
        * the Moneyjinn user which has import-rights
    * hbci.server.password
        * the Moneyjinn users password
    * hbci.database.url
        * jdbc:postgresql://db/postgres?currentSchema=moneyjinn_hbci
    * hbci.database.username
        * moneyjinn_hbci_app
    * hbci.database.password
        * your-password 
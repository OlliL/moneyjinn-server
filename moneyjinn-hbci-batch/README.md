# lalaHBCI

lalaHBCI utilizes [hbci4java](https://github.com/willuhn/hbci4java) to collect serveral data from given bank accounts, stores this information in a database and notifies given Observer to handle the fetched data.

## Setup

- create a Database
- execute `src/main/resources/database.sql` to create all needed tables
- create a user with insert/select rights on the created tables

## Usage

* create a new java project and add lalaHBCI as a dependency to it
* create a `static Main` class which executes `lala.hbci.Main` it could look like this:

```Java
public final class Main {
	public static void main(final String[] args) throws Exception {
		final org.laladev.hbci.Main main = new org.laladev.hbci.Main();
		final List<String> passports = new ArrayList<String>(3);
		passports.add(System.getProperty("user.home") + File.separator + "hbci_account_1.dat");
		passports.add(System.getProperty("user.home") + File.separator + "hbci_account_2.dat");
		passports.add(System.getProperty("user.home") + File.separator + "hbci_account_3.dat");

		final String propertyFilename = System.getProperty("user.home") + File.separator + "hbci_pass.properties";
		final List<Observer> observers = new ArrayList<Observer>(1);
		observers.add(new LalaMoneyflowSubscriber());

		main.main(passports, propertyFilename, observers);

	}
}
```


* you need a hbci.properties file which declares two types of properties
  * hbci.passport.password
    * must be the password to all of your passport files
  * hbci.PASSPORT-FILENAME.pin
    * replace PASSPORT-FILENAME with the filename of your passport file
    * must be the pin for logging in to the account with the specified number
    * specify as much of this properties as you have passport files
* create `a hibernate.cfg.xml` in your project in `src/main/resources`

```XML
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<property name="hibernate.bytecode.use_reflection_optimizer">false</property>
		<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
		<property name="hibernate.connection.password">password</property>
		<property name="hibernate.connection.url">jdbc:mysql://host:3306/database</property>
		<property name="hibernate.connection.username">username</property>
		<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
		<!-- <property name="show_sql">true</property> -->
		<mapping class="org.laladev.hbci.entity.AccountMovement"></mapping>
	</session-factory>
</hibernate-configuration>
```
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE IF NOT EXISTS `transactions` (
  `id` int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `iban` varchar(34) NOT NULL,
  `bic` varchar(11) NOT NULL,
  `valuta` date NOT NULL,
  `bdate` date NOT NULL,
  `invoicedate` date DEFAULT NULL,
  `value` decimal(10,2) NOT NULL,
  `gvcode` int(3) unsigned NOT NULL,
  `addkey` int(3) DEFAULT NULL,
  `gvtext` varchar(100) NOT NULL,
  `customerref` varchar(100) NOT NULL,
  `other_number` varchar(34) DEFAULT NULL,
  `other_blz` varchar(11) DEFAULT NULL,
  `other_name` varchar(100) DEFAULT NULL,
  `saldo_value` decimal(10,2) NOT NULL,
  `saldo_timestamp` date NOT NULL,
  `usagetext` text NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


ALTER TABLE `transactions`
  ADD UNIQUE KEY `hbci_i_01` (`valuta`,`bdate`,`value`,`gvcode`,`gvtext`,`customerref`,`saldo_value`,`saldo_timestamp`,`iban`,`bic`);

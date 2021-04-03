package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.service.dao.data.BankAccountData;
import org.laladev.moneyjinn.service.dao.data.mapper.BankAccountDataMapper;

public class BankAccountDataMapperTest {
	@Test
	public void testBankAccountMapping() {
		final BankAccountData bankAccountData = new BankAccountData();
		bankAccountData.setAccountNumber("1234");
		bankAccountData.setBankCode("ABCD");

		final BankAccountDataMapper bankAccountDataMapper = new BankAccountDataMapper();

		final BankAccount bankAccount = bankAccountDataMapper.mapBToA(bankAccountData);

		Assertions.assertEquals(bankAccount.getAccountNumber(), bankAccountData.getAccountNumber());
		Assertions.assertEquals(bankAccount.getBankCode(), bankAccountData.getBankCode());
	}
}

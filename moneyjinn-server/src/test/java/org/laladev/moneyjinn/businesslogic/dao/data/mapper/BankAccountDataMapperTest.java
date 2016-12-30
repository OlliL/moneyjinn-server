package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.Assert;
import org.junit.Test;
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

		Assert.assertEquals(bankAccount.getAccountNumber(), bankAccountData.getAccountNumber());
		Assert.assertEquals(bankAccount.getBankCode(), bankAccountData.getBankCode());
	}
}

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.service.dao.data.BankAccountData;
import org.laladev.moneyjinn.service.dao.data.mapper.BankAccountDataMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankAccountDataMapperTest extends AbstractTest {
    @Inject
    private BankAccountDataMapper bankAccountDataMapper;

    @Test
    void testBankAccountMapping() {
        final BankAccountData bankAccountData = new BankAccountData();
        bankAccountData.setAccountNumber("1234");
        bankAccountData.setBankCode("ABCD");
        final BankAccount bankAccount = this.bankAccountDataMapper.mapBToA(bankAccountData);
        assertEquals(bankAccount.getAccountNumber(), bankAccountData.getAccountNumber());
        assertEquals(bankAccount.getBankCode(), bankAccountData.getBankCode());
    }
}


package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.service.dao.data.MoneyflowData;
import org.laladev.moneyjinn.service.dao.data.mapper.MoneyflowDataMapper;

import jakarta.inject.Inject;

class MoneyflowDataMapperTest extends AbstractTest {
	@Inject
	MoneyflowDataMapper moneyflowDataMapper;

	@Test
	void testWithNullId() {
		final var moneyflow = this.moneyflowDataMapper.mapBToA(new MoneyflowData());
		assertNull(moneyflow.getUser());
		assertNull(moneyflow.getGroup());
		assertNull(moneyflow.getCapitalsource());
		assertNull(moneyflow.getContractpartner());
		assertNull(moneyflow.getPostingAccount());
	}
}

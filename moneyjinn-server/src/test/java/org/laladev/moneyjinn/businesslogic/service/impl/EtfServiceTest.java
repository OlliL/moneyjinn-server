
package org.laladev.moneyjinn.businesslogic.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class EtfServiceTest extends AbstractTest {
	@Inject
	private IEtfService etfService;

	@Test
	void test_createWithInvalidEntity_raisesException() {
		final EtfFlow etfFlow = new EtfFlow();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.createEtfFlow(etfFlow);
		});
	}

	@Test
	void test_updateWithInvalidEntity_raisesException() {
		final EtfFlow etfFlow = new EtfFlow();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.updateEtfFlow(etfFlow);
		});
	}
}

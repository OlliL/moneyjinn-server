
package org.laladev.moneyjinn.businesslogic.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class EtfServiceTest extends AbstractTest {
	@Inject
	private IEtfService etfService;

	@Test
	void test_createEtfFlowWithInvalidEntity_raisesException() {
		final EtfFlow etfFlow = new EtfFlow();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.createEtfFlow(new UserID(1L), etfFlow);
		});
	}

	@Test
	void test_updateEtfFlowWithInvalidEntity_raisesException() {
		final EtfFlow etfFlow = new EtfFlow();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.updateEtfFlow(new UserID(1L), etfFlow);
		});
	}

	@Test
	void test_createEtfWithInvalidEntity_raisesException() {
		final Etf etf = new Etf();
		etf.setUser(new User(new UserID(1L)));
		etf.setGroup(new Group(new GroupID(2L)));
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.createEtf(etf);
		});
	}

	@Test
	void test_updateWithInvalidEntity_raisesException() {
		final Etf etf = new Etf();
		etf.setUser(new User(new UserID(1L)));
		etf.setGroup(new Group(new GroupID(2L)));
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.updateEtf(etf);
		});
	}

	@Test
	void test_createEtfPreliminaryLumpSumWithInvalidEntity_raisesException() {
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = new EtfPreliminaryLumpSum();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.createEtfPreliminaryLumpSum(new UserID(1L), etfPreliminaryLumpSum);
		});
	}

	@Test
	void test_updateEtfPreliminaryLumpSumWithInvalidEntity_raisesException() {
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = new EtfPreliminaryLumpSum();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.etfService.updateEtfPreliminaryLumpSum(new UserID(1L), etfPreliminaryLumpSum);
		});
	}
}

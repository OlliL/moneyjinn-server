
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.UpsertMonthlySettlementRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;

import jakarta.inject.Inject;

class UpsertMonthlySettlementTest extends AbstractWebUserControllerTest {
	@Inject
	private IMonthlySettlementService monthlySettlementService;

	@Override
	protected void loadMethod() {
		super.getMock(MonthlySettlementControllerApi.class).upsertMonthlySettlement(null);
	}

	private void testError(final List<MonthlySettlementTransport> transports, final ErrorCode errorCode)
			throws Exception {
		final UpsertMonthlySettlementRequest request = new UpsertMonthlySettlementRequest();
		request.setMonthlySettlementTransports(transports);
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transports.getFirst().getId().toString())
				.withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);
		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_regularMonthlySettlementInsert_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final UpsertMonthlySettlementRequest request = new UpsertMonthlySettlementRequest();
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forNewMonthlySettlement().build();
		request.setMonthlySettlementTransports(Arrays.asList(monthlySettlementTransport));
		super.callUsecaseExpect204(request);
		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, monthlySettlementTransport.getYear(),
						Month.of(monthlySettlementTransport.getMonth().intValue()));
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertEquals(1, monthlySettlements.size());
		Assertions.assertEquals(monthlySettlementTransport.getYear(), monthlySettlements.getFirst().getYear());
		Assertions.assertEquals(monthlySettlementTransport.getMonth().intValue(),
				monthlySettlements.getFirst().getMonth().getValue());
	}

	@Test
	void test_MonthlySettlementForOtherUserUpdate_Error() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement3().build();
		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	void test_notOwnedCapitalsource_Error() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement1().build();
		monthlySettlementTransport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	void test_AmountToBig_Error() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement1().build();
		monthlySettlementTransport.setAmount(new BigDecimal(9999999));
		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.AMOUNT_TO_BIG);
	}

	@Test
	void test_AmountNull_Error() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement1().build();
		monthlySettlementTransport.setAmount(null);
		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.AMOUNT_HAS_TO_BE_SPECIFIED);
	}

	@Test
	void test_regularMonthlySettlementUpdate_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final UpsertMonthlySettlementRequest request = new UpsertMonthlySettlementRequest();
		final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		final MonthlySettlementTransport monthlySettlementTransport1 = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement1().withAmount(BigDecimal.ZERO).build();
		final MonthlySettlementTransport monthlySettlementTransport2 = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement2().withAmount(BigDecimal.ONE).build();
		monthlySettlementTransports.add(monthlySettlementTransport1);
		monthlySettlementTransports.add(monthlySettlementTransport2);
		request.setMonthlySettlementTransports(monthlySettlementTransports);
		super.callUsecaseExpect204(request);
		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, monthlySettlementTransports.getFirst().getYear(),
						Month.of(monthlySettlementTransports.getFirst().getMonth().intValue()));
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertEquals(3, monthlySettlements.size());
		Assertions.assertEquals(0,
				monthlySettlementTransport1.getAmount().compareTo(monthlySettlements.getFirst().getAmount()));
		Assertions.assertEquals(0,
				monthlySettlementTransport2.getAmount().compareTo(monthlySettlements.get(1).getAmount()));
		Assertions.assertEquals(0, new MonthlySettlementTransportBuilder().forMonthlySettlement3().build().getAmount()
				.compareTo(monthlySettlements.get(2).getAmount()));
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new UpsertMonthlySettlementRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement3().build();
		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}
}
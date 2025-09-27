package org.laladev.moneyjinn.server.controller.monthlysettlement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementCreateResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class ShowMonthlySettlementCreateTest extends AbstractWebUserControllerTest {
    @Override
    protected void loadMethod() {
        super.getMock(MonthlySettlementControllerApi.class).showMonthlySettlementCreate();
    }

    @Test
    void test_nextUnsettledMonthExplicitlyAndByDefault_FullContentWithCalculatedAmount() throws Exception {
        final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
        final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
        monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().withId(null)
                .withYear(2010).withMonth(5).withAmount(BigDecimal.ZERO).build());
        monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().withId(null)
                .withYear(2010).withMonth(5).build());
        expected.setMonthlySettlementTransports(monthlySettlementTransports);
        expected.setYear(2010);
        expected.setMonth(5);
        expected.setEditMode(0);

        final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
                ShowMonthlySettlementCreateResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_nextUnsettledMonthWithImportedData_FullContentWithCalculatedAmount() throws Exception {
        super.setUsername(UserTransportBuilder.USER3_NAME);
        super.setPassword(UserTransportBuilder.USER3_PASSWORD);

        final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
        final List<MonthlySettlementTransport> importedMonthlySettlementTransports = new ArrayList<>();
        importedMonthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement3()
                .withId(null).withYear(2010).withMonth(5).withAmount(new BigDecimal("9.00")).build());
        expected.setImportedMonthlySettlementTransports(importedMonthlySettlementTransports);
        final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
        monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forCapitalsource6().build());
        expected.setMonthlySettlementTransports(monthlySettlementTransports);
        expected.setYear(2010);
        expected.setMonth(5);
        expected.setEditMode(0);

        final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
                ShowMonthlySettlementCreateResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
        final LocalDate now = LocalDate.now();
        expected.setYear(now.getYear());
        expected.setMonth(now.getMonthValue());
        expected.setEditMode(0);

        final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
                ShowMonthlySettlementCreateResponse.class);

        Assertions.assertEquals(expected, actual);
    }
}
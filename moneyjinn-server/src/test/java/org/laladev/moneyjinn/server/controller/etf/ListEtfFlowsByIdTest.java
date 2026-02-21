package org.laladev.moneyjinn.server.controller.etf;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.server.builder.EtfEffectiveFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.*;
import org.laladev.moneyjinn.service.api.ISettingService;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ListEtfFlowsByIdTest extends AbstractWebUserControllerTest {
    private static final BigDecimal SETTING_SALE_PIECES = new BigDecimal("10");
    @Inject
    private ISettingService settingService;

    @Override
    protected void loadMethod() {
        super.getMock(EtfControllerApi.class).listEtfFlowsById(null);
    }

    private UserID getUserId() {
        return new UserID(UserTransportBuilder.USER1_ID);
    }

    private void initEtfSettings() {
        this.settingService.setClientCalcEtfSalePieces(this.getUserId(),
                new ClientCalcEtfSalePieces(SETTING_SALE_PIECES));
    }

    private ListEtfFlowsResponse getResponseForEtf1() {
        final ListEtfFlowsResponse expected = new ListEtfFlowsResponse();

        final List<EtfFlowTransport> allTransports = new ArrayList<>();
        allTransports.add(new EtfFlowTransportBuilder().forFlow11().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow10().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow9().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow8().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow7().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow6().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow5().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow4().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow3().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow2().build());
        allTransports.add(new EtfFlowTransportBuilder().forFlow1().build());
        expected.setEtfFlowTransports(allTransports);

        final List<EtfEffectiveFlowTransport> effectiveTransports = new ArrayList<>();
        effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow11().build());
        effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow9().build());
        effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow8().build());
        effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow6().build());
        expected.setEtfEffectiveFlowTransports(effectiveTransports);

        final EtfTransport etf = new EtfTransportBuilder().forEtf1().build();
        final EtfSummaryTransport transport = new EtfSummaryTransport();
        final BigDecimal amount = new BigDecimal("193.234000");
        final BigDecimal spentValue = new BigDecimal("149583.300782000000");
        transport.setEtfId(etf.getEtfId());
        transport.setName(etf.getName());
        transport.setChartUrl(etf.getChartUrl());
        transport.setAmount(amount);
        transport.setSpentValue(spentValue);
        // latest etfvalues table entry
        transport.setBuyPrice(new BigDecimal("666.000"));
        transport.setSellPrice(new BigDecimal("666.543"));
        transport.setPricesTimestamp(ZonedDateTime.of(2012, 1, 16, 22, 5, 2, 0, ZoneId.systemDefault()).toInstant()
                .atOffset(ZoneOffset.UTC));
        expected.setEtfSummaryTransport(transport);

        return expected;
    }

    @Test
    void test_standardRequestWithoutSettings_FullResponseObject() throws Exception {
        final ListEtfFlowsResponse expected = this.getResponseForEtf1();

        final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
                EtfTransportBuilder.ETF_ID_1);

        assertEquals(expected, actual);

    }

    @Test
    void test_yearlyLumpSums_correctlyCalculated() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

        final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
                EtfTransportBuilder.ETF_ID_2);

        final var flows = actual.getEtfEffectiveFlowTransports();
        assertEquals(13, flows.size());
        assertLumpSump(0.96506392, flows.getFirst());
        assertLumpSump(1.89615945, flows.get(1));
        assertLumpSump(2.97832516, flows.get(2));
        assertLumpSump(4.09150714, flows.get(3));
        assertLumpSump(5.17632912, flows.get(4));
        assertLumpSump(6.44866963, flows.get(5));
        assertLumpSump(7.63185894, flows.get(6));
        assertLumpSump(9.01757435, flows.get(7));
        assertLumpSump(9.90531380, flows.get(8));
        assertLumpSump(10.13623328, flows.get(9));
        assertLumpSump(11.16077714, flows.get(10));
        assertLumpSump(12.41842457, flows.get(11));
        assertLumpSump(634.42376350, flows.get(12));
    }

    private static void assertLumpSump(final double expected, final EtfEffectiveFlowTransport flows) {
        assertNotNull(flows.getAccumulatedPreliminaryLumpSum());
        assertEquals(expected, flows.getAccumulatedPreliminaryLumpSum().doubleValue(), 0.0001);
    }

    @Test
    void test_standardRequestWithSettings_FullResponseObject() throws Exception {
        final ListEtfFlowsResponse expected = this.getResponseForEtf1();

        this.initEtfSettings();

        expected.setCalcEtfSalePieces(SETTING_SALE_PIECES);

        final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
                EtfTransportBuilder.ETF_ID_1);

        assertEquals(expected, actual);
    }

    @Test
    void test_notExisting_NotFoundRaised() throws Exception {
        super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
    }

    @Test
    void test_etfFromOtherGroup_nothingHappens() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

        super.callUsecaseExpect404(EtfTransportBuilder.ETF_ID_1);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.NON_EXISTING_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
    }
}
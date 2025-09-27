package org.laladev.moneyjinn.server.controller.etf;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
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
        final BigDecimal spentValue = new BigDecimal("149583.300782000");
        transport.setEtfId(etf.getEtfId());
        transport.setName(etf.getName());
        transport.setChartUrl(etf.getChartUrl());
        transport.setAmount(amount);
        transport.setSpentValue(spentValue);
        // latest etfvalues table entry
        transport.setBuyPrice(new BigDecimal("666.000"));
        transport.setSellPrice(new BigDecimal("666.543"));
        transport.setPricesTimestamp(ZonedDateTime.of(2012, 01, 16, 22, 5, 2, 0, ZoneId.systemDefault()).toInstant()
                .atOffset(ZoneOffset.UTC));
        expected.setEtfSummaryTransport(transport);

        return expected;
    }

    @Test
    void test_standardRequestWithoutSettings_FullResponseObject() throws Exception {
        final ListEtfFlowsResponse expected = this.getResponseForEtf1();

        final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
                EtfTransportBuilder.ETF_ID_1);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    void test_standardRequestWithSettings_FullResponseObject() throws Exception {
        final ListEtfFlowsResponse expected = this.getResponseForEtf1();

        this.initEtfSettings();

        expected.setCalcEtfSalePieces(SETTING_SALE_PIECES);

        final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
                EtfTransportBuilder.ETF_ID_1);

        Assertions.assertEquals(expected, actual);

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
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.ShowTrendsFormResponse;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.ISettingService;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

class ShowTrendsFormTest extends AbstractWebUserControllerTest {
    @Inject
    private ISettingService settingService;
    @Inject
    private IMonthlySettlementService monthlySettlementService;

    @Override
    protected void loadMethod() {
        super.getMock(ReportControllerApi.class).showTrendsForm();
    }

    private ShowTrendsFormResponse getDefaultResponse() {
        final ShowTrendsFormResponse expected = new ShowTrendsFormResponse();

        expected.setMinDate(LocalDate.parse("2008-11-01"));
        expected.setMaxDate(LocalDate.parse("2010-05-03"));
        return expected;
    }

    @Test
    void test_TestLastSettlementInDecemberButFlowsInNextYear_nextYearIncludedInAllYears() throws Exception {
        final UserID userId1 = new UserID(UserTransportBuilder.USER1_ID);
        final UserID userId3 = new UserID(UserTransportBuilder.USER3_ID);
        this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.JANUARY);
        this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.FEBRUARY);
        this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.MARCH);
        this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.APRIL);
        this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.JANUARY);
        this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.FEBRUARY);
        this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.MARCH);
        this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.APRIL);
        final ShowTrendsFormResponse expected = this.getDefaultResponse();
        final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_noSetting_defaultsResponse() throws Exception {
        final ShowTrendsFormResponse expected = this.getDefaultResponse();
        final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_noSettingNoSettlements_minEqualsMaxDate() throws Exception {
        final UserID userId1 = new UserID(UserTransportBuilder.USER1_ID);
        final UserID userId3 = new UserID(UserTransportBuilder.USER3_ID);
        for (final var userId : List.of(userId1, userId3)) {
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2008, Month.NOVEMBER);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2008, Month.DECEMBER);

            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.JANUARY);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.FEBRUARY);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.MARCH);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.APRIL);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.MAY);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.JUNE);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.JULY);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.AUGUST);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.SEPTEMBER);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.OCTOBER);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.NOVEMBER);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2009, Month.DECEMBER);

            this.monthlySettlementService.deleteMonthlySettlement(userId, 2010, Month.JANUARY);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2010, Month.FEBRUARY);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2010, Month.MARCH);
            this.monthlySettlementService.deleteMonthlySettlement(userId, 2010, Month.APRIL);
        }

        final ShowTrendsFormResponse expected = this.getDefaultResponse();
        expected.setMinDate(LocalDate.parse("2010-05-03"));

        final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_witDefaultSelection_defaultsResponse() throws Exception {
        final ClientTrendCapitalsourceIDsSetting setting = new ClientTrendCapitalsourceIDsSetting(
                Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID),
                        new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID),
                        new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID),
                        new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE6_ID)));
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        this.settingService.setClientTrendCapitalsourceIDsSetting(userId, setting);
        final ShowTrendsFormResponse expected = this.getDefaultResponse();
        expected.setSettingTrendCapitalsourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE5_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE6_ID));
        final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect200(ShowTrendsFormResponse.class);
    }
}
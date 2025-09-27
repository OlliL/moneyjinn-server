package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.ShowReportingFormResponse;
import org.laladev.moneyjinn.service.api.ISettingService;

import java.time.LocalDate;
import java.util.Arrays;

class ShowReportingFormTest extends AbstractWebUserControllerTest {
    @Inject
    private ISettingService settingService;

    @Override
    protected void loadMethod() {
        super.getMock(ReportControllerApi.class).showReportingForm();
    }

    private ShowReportingFormResponse getDefaultResponse() {
        final ShowReportingFormResponse expected = new ShowReportingFormResponse();

        expected.setMinDate(LocalDate.parse("2008-12-01"));
        expected.setMaxDate(LocalDate.parse("2010-05-03"));
        return expected;
    }

    @Test
    void test_noSetting_defaultsResponse() throws Exception {
        final ShowReportingFormResponse expected = this.getDefaultResponse();
        final ShowReportingFormResponse actual = super.callUsecaseExpect200(ShowReportingFormResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_witDefaultSelection_defaultsResponse() throws Exception {
        final ClientReportingUnselectedPostingAccountIdsSetting setting = new ClientReportingUnselectedPostingAccountIdsSetting(
                Arrays.asList(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID),
                        new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID)));
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        this.settingService.setClientReportingUnselectedPostingAccountIdsSetting(userId, setting);
        final ShowReportingFormResponse expected = this.getDefaultResponse();
        expected.setPostingAccountIds(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
                PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
        final ShowReportingFormResponse actual = super.callUsecaseExpect200(ShowReportingFormResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect200(ShowReportingFormResponse.class);
    }
}
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.server.builder.PostingAccountAmountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.PostingAccountAmountTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlyReportGraphRequest;
import org.laladev.moneyjinn.server.model.ShowMonthlyReportGraphResponse;
import org.laladev.moneyjinn.service.api.ISettingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ShowMonthlyReportGraphTest extends AbstractWebUserControllerTest {
    @Inject
    private ISettingService settingService;

    @Override
    protected void loadMethod() {
        super.getMock(ReportControllerApi.class).showMonthlyReportGraph(null);
    }

    @Test
    void test_maxDateRange_response() throws Exception {
        final ShowMonthlyReportGraphRequest request = new ShowMonthlyReportGraphRequest();
        request.setStartDate(LocalDate.parse("1970-01-01"));
        request.setEndDate(LocalDate.parse("2099-12-31"));
        request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
                PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
        final ShowMonthlyReportGraphResponse expected = new ShowMonthlyReportGraphResponse();
        final List<PostingAccountAmountTransport> postingAccountAmountTransports = new ArrayList<>();
        // Respect Moneyflow Split Entries -1.10 -> -1.00
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
                .withDate("2008-12-01").withAmount("10.10").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
                .withDate("2009-01-01").withAmount("-1.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-01-01").withAmount("-0.10").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-02-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-03-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-04-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-05-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-06-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-07-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-08-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-09-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-10-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-11-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-12-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-01-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-02-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-03-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-04-01").withAmount("10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-05-01").withAmount("-10.00").build());
        expected.setPostingAccountAmountTransports(postingAccountAmountTransports);

        final ShowMonthlyReportGraphResponse actual = super.callUsecaseExpect200(request,
                ShowMonthlyReportGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_empty_PostingAccountIdsYes_nullResponseNoError() throws Exception {
        final ShowMonthlyReportGraphRequest request = new ShowMonthlyReportGraphRequest();
        request.setStartDate(LocalDate.parse("1970-01-01"));
        request.setEndDate(LocalDate.parse("2099-12-31"));
        final ShowMonthlyReportGraphResponse expected = new ShowMonthlyReportGraphResponse();
        final ShowMonthlyReportGraphResponse actual = super.callUsecaseExpect200(request,
                ShowMonthlyReportGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_withUnselectedPostingAccountIDs_idsSaved() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ShowMonthlyReportGraphRequest request = new ShowMonthlyReportGraphRequest();
        request.setStartDate(LocalDate.parse("1970-01-01"));
        request.setEndDate(LocalDate.parse("2099-12-31"));
        request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
                PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
        request.setPostingAccountIdsNo(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));
        Optional<ClientReportingUnselectedPostingAccountIdsSetting> setting = this.settingService
                .getClientReportingUnselectedPostingAccountIdsSetting(userId);
        Assertions.assertFalse(setting.isPresent());
        super.callUsecaseExpect200(request, ShowMonthlyReportGraphResponse.class);
        setting = this.settingService.getClientReportingUnselectedPostingAccountIdsSetting(userId);
        Assertions.assertTrue(setting.isPresent());
        Assertions.assertNotNull(setting.get().getSetting());
        Assertions.assertEquals(1, setting.get().getSetting().size());
        Assertions.assertEquals(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID,
                setting.get().getSetting().getFirst().getId());
    }

    @Test
    void test_privateMoneyflows_ignored() throws Exception {
        super.setUsername(UserTransportBuilder.USER3_NAME);
        super.setPassword(UserTransportBuilder.USER3_PASSWORD);

        final ShowMonthlyReportGraphRequest request = new ShowMonthlyReportGraphRequest();
        request.setStartDate(LocalDate.parse("1970-01-01"));
        request.setEndDate(LocalDate.parse("2099-12-31"));
        request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
                PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
        final ShowMonthlyReportGraphResponse expected = new ShowMonthlyReportGraphResponse();
        final List<PostingAccountAmountTransport> postingAccountAmountTransports = new ArrayList<>();
        // Respect Moneyflow Split Entries -1.10 -> -1.00
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
                .withDate("2008-12-01").withAmount("10.10").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
                .withDate("2009-01-01").withAmount("-1.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2009-01-01").withAmount("-0.10").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-01-01").withAmount("-10.00").build());
        postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
                .withDate("2010-05-01").withAmount("-5.00").build());
        expected.setPostingAccountAmountTransports(postingAccountAmountTransports);

        final ShowMonthlyReportGraphResponse actual = super.callUsecaseExpect200(request,
                ShowMonthlyReportGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403(new ShowMonthlyReportGraphRequest());
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowMonthlyReportGraphRequest request = new ShowMonthlyReportGraphRequest();
        request.setStartDate(LocalDate.parse("2010-04-01"));
        request.setEndDate(LocalDate.parse("2010-12-31"));
        request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
                PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));

        super.callUsecaseExpect200(request, ShowMonthlyReportGraphResponse.class);
    }
}
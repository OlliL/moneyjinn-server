package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.TrendsTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.ShowTrendsGraphRequest;
import org.laladev.moneyjinn.server.model.ShowTrendsGraphResponse;
import org.laladev.moneyjinn.server.model.TrendsTransport;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ShowTrendsGraphTest extends AbstractWebUserControllerTest {
    @Inject
    private ICapitalsourceService capitalsourceService;

    @Override
    protected void loadMethod() {
        super.getMock(ReportControllerApi.class).showTrendsGraph(null);
    }

    @Test
    void test_maxDateRange_response() throws Exception {
        final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
        request.setStartDate(LocalDate.parse("1970-01-01"));
        request.setEndDate(LocalDate.parse("2099-12-31"));
        request.setSettingTrendCapitalsourcesActive(true);
        request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));
        final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
        final List<TrendsTransport> trendsSettledTransports = new ArrayList<>();
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2008).withMonth(11).withAmount("1099.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2008).withMonth(12).withAmount("1110.00").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(1).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(2).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(3).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(4).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(5).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(6).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(7).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(8).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(9).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(10).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(11).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(12).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(1).withAmount("108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(2).withAmount("118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(3).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(4).withAmount("1110.00").build());
        expected.setTrendsSettledTransports(trendsSettledTransports);
        final List<TrendsTransport> trendsCalculatedTransports = new ArrayList<>();
        trendsCalculatedTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(5).withAmount("1100.00").build());
        expected.setTrendsCalculatedTransports(trendsCalculatedTransports);
        final ShowTrendsGraphResponse actual = super.callUsecaseExpect200(request, ShowTrendsGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_only2009_response() throws Exception {
        final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
        request.setStartDate(LocalDate.parse("2009-01-01"));
        request.setEndDate(LocalDate.parse("2009-12-31"));
        request.setSettingTrendCapitalsourcesActive(true);
        request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));
        final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
        final List<TrendsTransport> trendsSettledTransports = new ArrayList<>();
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(1).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(2).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(3).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(4).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(5).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(6).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(7).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(8).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(9).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(10).withAmount("1118.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(11).withAmount("1108.90").build());
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2009).withMonth(12).withAmount("1118.90").build());
        expected.setTrendsSettledTransports(trendsSettledTransports);
        final ShowTrendsGraphResponse actual = super.callUsecaseExpect200(request, ShowTrendsGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_onlyOneUnsettledMonth_response() throws Exception {
        final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
        request.setStartDate(LocalDate.parse("2010-05-01"));
        request.setEndDate(LocalDate.parse("2010-12-31"));
        request.setSettingTrendCapitalsourcesActive(true);
        request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
                CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));
        final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
        final List<TrendsTransport> trendsCalculatedTransports = new ArrayList<>();
        trendsCalculatedTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(5).withAmount("-10.00").build());
        expected.setTrendsCalculatedTransports(trendsCalculatedTransports);
        final ShowTrendsGraphResponse actual = super.callUsecaseExpect200(request, ShowTrendsGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_validtyPeriodOfCapitalsource_response() throws Exception {
        final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
        final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
        final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
        final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
                capitalsourceId);
        capitalsource.setValidTil(LocalDate.of(2010, Month.APRIL, 30));
        this.capitalsourceService.updateCapitalsource(capitalsource);
        final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
        request.setStartDate(LocalDate.parse("2010-04-01"));
        request.setEndDate(LocalDate.parse("2010-12-31"));
        request.setSettingTrendCapitalsourcesActive(true);
        request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));
        final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
        final List<TrendsTransport> trendsSettledTransports = new ArrayList<>();
        trendsSettledTransports
                .add(new TrendsTransportBuilder().withYear(2010).withMonth(4).withAmount("1000.00").build());
        expected.setTrendsSettledTransports(trendsSettledTransports);
        final ShowTrendsGraphResponse actual = super.callUsecaseExpect200(request, ShowTrendsGraphResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403(new ShowTrendsGraphRequest());
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
        request.setStartDate(LocalDate.parse("2010-04-01"));
        request.setEndDate(LocalDate.parse("2010-12-31"));
        request.setSettingTrendCapitalsourcesActive(true);
        request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));

        super.callUsecaseExpect200(request, ShowTrendsGraphResponse.class);
    }
}
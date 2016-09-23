package org.laladev.moneyjinn.server.controller.report;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowTrendsGraphRequest;
import org.laladev.moneyjinn.core.rest.model.report.ShowTrendsGraphResponse;
import org.laladev.moneyjinn.core.rest.model.report.transport.TrendsCalculatedTransport;
import org.laladev.moneyjinn.core.rest.model.report.transport.TrendsSettledTransport;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.TrendsTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowTrendsGraphTest extends AbstractControllerTest {

	@Inject
	private ICapitalsourceService capitalsourceService;

	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@Before
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.userPassword;
	}

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_maxDateRange_response() throws Exception {
		final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
		request.setStartDate(new Date(70, 0, 1));
		request.setEndDate(new Date(199, 11, 31));
		request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));

		final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
		final List<TrendsSettledTransport> trendsSettledTransports = new ArrayList<>();
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2008).withMonth(11).withAmount("1099.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2008).withMonth(12).withAmount("1110.00")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(1).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(2).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(3).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(4).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(5).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(6).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(7).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(8).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(9).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(10).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(11).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(12).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(1).withAmount("108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(2).withAmount("118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(3).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(4).withAmount("1110.00")
				.build(TrendsSettledTransport.class));
		expected.setTrendsSettledTransports(trendsSettledTransports);

		final List<TrendsCalculatedTransport> trendsCalculatedTransports = new ArrayList<>();
		trendsCalculatedTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(5).withAmount("1100.00")
				.build(TrendsCalculatedTransport.class));
		expected.setTrendsCalculatedTransports(trendsCalculatedTransports);

		final ShowTrendsGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowTrendsGraphResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_only2009_response() throws Exception {
		final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
		request.setStartDate(new Date(109, 0, 1));
		request.setEndDate(new Date(109, 11, 31));
		request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));

		final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
		final List<TrendsSettledTransport> trendsSettledTransports = new ArrayList<>();
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(1).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(2).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(3).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(4).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(5).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(6).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(7).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(8).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(9).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(10).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(11).withAmount("1108.90")
				.build(TrendsSettledTransport.class));
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2009).withMonth(12).withAmount("1118.90")
				.build(TrendsSettledTransport.class));
		expected.setTrendsSettledTransports(trendsSettledTransports);

		final ShowTrendsGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowTrendsGraphResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_onlyOneUnsettledMonth_response() throws Exception {
		final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
		request.setStartDate(new Date(110, 4, 1));
		request.setEndDate(new Date(110, 11, 31));
		request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));

		final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
		final List<TrendsCalculatedTransport> trendsCalculatedTransports = new ArrayList<>();
		trendsCalculatedTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(5).withAmount("-10.00")
				.build(TrendsCalculatedTransport.class));
		expected.setTrendsCalculatedTransports(trendsCalculatedTransports);

		final ShowTrendsGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowTrendsGraphResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_validtyPeriodOfCapitalsource_response() throws Exception {
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		capitalsource.setValidTil(LocalDate.of(2010, Month.APRIL, 30));
		this.capitalsourceService.updateCapitalsource(capitalsource);

		final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
		request.setStartDate(new Date(110, 3, 1));
		request.setEndDate(new Date(110, 11, 31));
		request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));

		final ShowTrendsGraphResponse expected = new ShowTrendsGraphResponse();
		final List<TrendsSettledTransport> trendsSettledTransports = new ArrayList<>();
		trendsSettledTransports.add(new TrendsTransportBuilder().withYear(2010).withMonth(4).withAmount("1000.00")
				.build(TrendsSettledTransport.class));
		expected.setTrendsSettledTransports(trendsSettledTransports);

		final ShowTrendsGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowTrendsGraphResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
		super.callUsecaseWithContent("", this.method, request, false, ShowTrendsGraphResponse.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabaseFakeRequestData_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowTrendsGraphRequest request = new ShowTrendsGraphRequest();
		request.setStartDate(new Date(110, 3, 1));
		request.setEndDate(new Date(110, 11, 31));
		request.setCapitalSourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID));
		super.callUsecaseWithContent("", this.method, request, false, ShowTrendsGraphResponse.class);
	}
}
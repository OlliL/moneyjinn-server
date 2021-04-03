package org.laladev.moneyjinn.server.controller.moneyflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.transport.MoneyflowSearchParamsTransport;
import org.laladev.moneyjinn.core.rest.model.moneyflow.transport.MoneyflowSearchResultTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.MoneyflowSearchResultTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class SearchMoneyflowsTest extends AbstractControllerTest {

	private static final Short SHORT_1 = (short) 1;

	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@BeforeEach
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

	private SearchMoneyflowsResponse getDefaultResponse() {
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner3().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner4().build());
		expected.setContractpartnerTransports(contractpartnerTransports);

		return expected;
	}

	private void assertEquals(final SearchMoneyflowsResponse expected, final SearchMoneyflowsResponse actual) {
		if (expected.getMoneyflowSearchResultTransports() != null) {
			Collections.sort(expected.getMoneyflowSearchResultTransports(),
					new MoneyflowSearchResultTransportComparator());
		}
		if (actual.getMoneyflowSearchResultTransports() != null) {
			Collections.sort(actual.getMoneyflowSearchResultTransports(),
					new MoneyflowSearchResultTransportComparator());
		}

		Assertions.assertEquals(expected, actual);
	}

	private class MoneyflowSearchResultTransportComparator implements Comparator<MoneyflowSearchResultTransport> {

		@Override
		public int compare(final MoneyflowSearchResultTransport o1, final MoneyflowSearchResultTransport o2) {

			if (o1 == null) {
				if (o2 == null) {
					return 0;
				}
				return Integer.MIN_VALUE;
			} else {
				if (o2 == null) {
					return Integer.MAX_VALUE;
				}

				int result = 0;

				if (o1.getYear() == null) {
					if (o2.getYear() != null) {
						return Integer.MIN_VALUE;
					}
				} else {
					result = o1.getYear().compareTo(o2.getYear());
				}
				if (result != 0) {
					return result;
				}

				if (o1.getMonth() == null) {
					if (o2.getMonth() != null) {
						return Integer.MIN_VALUE;
					}
				} else {
					result = o1.getMonth().compareTo(o2.getMonth());
				}
				if (result != 0) {
					return result;
				}

				if (o1.getContractpartnerid() == null) {
					if (o2.getContractpartnerid() != null) {
						return Integer.MIN_VALUE;
					}
				} else {
					result = o1.getContractpartnerid().compareTo(o2.getContractpartnerid());
				}

				return result;

			}
		}

	}

	@Test
	public void test_searchString_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("ENERATED");
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_searchStringDateRange_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("ENERATED");
		transport.setStartDate(DateUtil.getGmtDate("2009-05-01"));
		transport.setEndDate(DateUtil.getGmtDate("2009-11-10"));
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009)
				.withAmount("-10.00").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_searchStringDateRangeYearMonthGrouping_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setGroupBy2("month");
		transport.setSearchString("ENERATED");
		transport.setStartDate(DateUtil.getGmtDate("2009-05-01"));
		transport.setEndDate(DateUtil.getGmtDate("2009-11-10"));
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(5)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(6)
				.withAmount("10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(7)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(8)
				.withAmount("10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(9)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(10)
				.withAmount("10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009).withMonth(11)
				.withAmount("-10.00").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_searchStringDateRangeMonthGroupingBy2Only_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy2("month");
		transport.setSearchString("ENERATED");
		transport.setStartDate(DateUtil.getGmtDate("2009-05-01"));
		transport.setEndDate(DateUtil.getGmtDate("2009-11-10"));
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(5)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(6).withAmount("10.00")
				.withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(7)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(8).withAmount("10.00")
				.withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(9)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(10)
				.withAmount("10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withMonth(11)
				.withAmount("-10.00").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_searchStringDateRangeContractpartnerGrouping_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("contractpartner");
		transport.setSearchString("ENERATED");
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder()
				.withContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID)
				.withContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME).withAmount("15.10")
				.withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder()
				.withContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID)
				.withContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME).withAmount("-5.00")
				.withComment("generated").build());

		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_searchStringDateRangeYearContractpartnerGrouping_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setGroupBy2("contractpartner");
		transport.setSearchString("ENERATED");
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder()
				.withContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID)
				.withContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME).withYear(2008)
				.withAmount("10.10").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder()
				.withContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID)
				.withContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME).withYear(2009)
				.withAmount("10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder()
				.withContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID)
				.withContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME).withYear(2010)
				.withAmount("-5.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder()
				.withContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID)
				.withContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME).withYear(2010)
				.withAmount("-5.00").withComment("generated").build());

		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_caseSensitive_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("ENERATED");
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_caseSensitive_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("enerated");
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_regexp_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("[E][N][E][R][A][T][E][D]");
		transport.setFeatureRegexp(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_regexpCaseSensitive_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("[E][N][E][R][A][T][E][D]");
		transport.setFeatureRegexp(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_regexpCaseSensitive_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("[e][n][e][r][a][t][e][d]");
		transport.setFeatureRegexp(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_equals_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("GENERATED");
		transport.setFeatureEqual(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_equals_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("ENERATED");
		transport.setFeatureEqual(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_equalsCaseSensitive_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("GENERATED");
		transport.setFeatureEqual(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_equalsCaseSensitive_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("generated");
		transport.setFeatureEqual(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_onlyMinusAmounts_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("generated");
		transport.setFeatureOnlyMinusAmounts(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009)
				.withAmount("-50.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2010)
				.withAmount("-30.00").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_postingAccount_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		// Moneyflow Split Entries suppot checked here -1.10 -> -1.00
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009)
				.withAmount("-1.00").withComment("split1").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2008)
				.withAmount("10.10").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_contractpartner_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009)
				.withAmount("8.90").withComment("split2,split1,generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2008)
				.withAmount("10.10").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2010)
				.withAmount("-5.00").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	public void test_searchStringContractpartnerPostingAccount_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		transport.setContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		transport.setSearchString("generated");
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = this.getDefaultResponse();
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2008)
				.withAmount("10.10").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	private void fillYearlySearchGenerated(final SearchMoneyflowsResponse expected) {
		final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = new ArrayList<>();
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2009)
				.withAmount("10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2010)
				.withAmount("-10.00").withComment("generated").build());
		moneyflowSearchResultTransports.add(new MoneyflowSearchResultTransportBuilder().withYear(2008)
				.withAmount("10.10").withComment("generated").build());
		expected.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_noTransport_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();

		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner5().build());
		expected.setContractpartnerTransports(contractpartnerTransports);

		final ValidationItemTransport validationItemTransport1 = new ValidationItemTransport();
		validationItemTransport1.setError(ErrorCode.NO_SEARCH_CRITERIA_ENTERED.getErrorCode());
		final ValidationItemTransport validationItemTransport2 = new ValidationItemTransport();
		validationItemTransport2.setError(ErrorCode.NO_GROUPING_CRITERIA_GIVEN.getErrorCode());
		final List<ValidationItemTransport> validationItemTransports = Arrays.asList(validationItemTransport1,
				validationItemTransport2);
		expected.setValidationItemTransports(validationItemTransports);
		expected.setResult(Boolean.FALSE);

		final SearchMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				SearchMoneyflowsResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setGroupBy1("year");
		transport.setSearchString("hugo");
		request.setMoneyflowSearchParamsTransport(transport);
		super.callUsecaseWithContent("", this.method, request, false, SearchMoneyflowsResponse.class);
	}

}
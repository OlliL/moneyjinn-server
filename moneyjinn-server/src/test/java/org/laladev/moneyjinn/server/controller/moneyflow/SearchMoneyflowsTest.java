
package org.laladev.moneyjinn.server.controller.moneyflow;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.MoneyflowSearchParamsTransport;
import org.laladev.moneyjinn.server.model.MoneyflowTransport;
import org.laladev.moneyjinn.server.model.SearchMoneyflowsRequest;
import org.laladev.moneyjinn.server.model.SearchMoneyflowsResponse;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;

class SearchMoneyflowsTest extends AbstractWebUserControllerTest {
	private static final Integer SHORT_1 = 1;

	@Override
	protected void loadMethod() {
		super.getMock(MoneyflowControllerApi.class).searchMoneyflows(null);
	}

	private void assertEquals(final SearchMoneyflowsResponse expected, final SearchMoneyflowsResponse actual) {
		if (expected.getMoneyflowTransports() != null) {
			Collections.sort(expected.getMoneyflowTransports(), new MoneyflowTransportComparator());
		}
		if (actual.getMoneyflowTransports() != null) {
			Collections.sort(actual.getMoneyflowTransports(), new MoneyflowTransportComparator());
		}
		Assertions.assertEquals(expected, actual);
	}

	private static class MoneyflowTransportComparator implements Comparator<MoneyflowTransport>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(final MoneyflowTransport o1, final MoneyflowTransport o2) {
			if (o1 == null) {
				if (o2 == null) {
					return 0;
				}
				return -1;
			} else {
				if (o2 == null) {
					return 1;
				}
				int result = 0;
				if (o1.getId() == null) {
					if (o2.getId() != null) {
						return -1;
					}
				} else {
					result = o1.getId().compareTo(o2.getId());
				}
				if (result != 0) {
					return result;
				}
				if (o1.getAmount() == null) {
					if (o2.getAmount() != null) {
						return -1;
					}
				} else {
					result = o1.getAmount().compareTo(o2.getAmount());
				}
				if (result != 0) {
					return result;
				}
				if (o1.getComment() == null) {
					if (o2.getComment() != null) {
						return -1;
					}
				} else {
					result = o1.getComment().compareTo(o2.getComment());
				}
				return result;
			}
		}
	}

	@Test
	void test_searchString_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("ENERATED");
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_searchStringDateRange_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("ENERATED");
		transport.setStartDate(LocalDate.parse("2009-05-01"));
		transport.setEndDate(LocalDate.parse("2009-11-10"));
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow6().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow7().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow8().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow9().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow10().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow11().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow12().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_caseSensitive_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("ENERATED");
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_caseSensitive_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("enerated");
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_regexp_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("[E][N][E][R][A][T][E][D]");
		transport.setFeatureRegexp(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_regexpCaseSensitive_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("[E][N][E][R][A][T][E][D]");
		transport.setFeatureRegexp(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_regexpCaseSensitive_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("[e][n][e][r][a][t][e][d]");
		transport.setFeatureRegexp(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_equals_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("GENERATED");
		transport.setFeatureEqual(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_equals_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("ENERATED");
		transport.setFeatureEqual(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_equalsCaseSensitive_noMatches() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("GENERATED");
		transport.setFeatureEqual(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_equalsCaseSensitive_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("generated");
		transport.setFeatureEqual(SHORT_1);
		transport.setFeatureCaseSensitive(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		this.fillYearlySearchGenerated(expected);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_onlyMinusAmounts_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("generated");
		transport.setFeatureOnlyMinusAmounts(SHORT_1);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow4().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow6().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow8().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow10().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow12().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow16().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_postingAccount_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		final MoneyflowTransport transport1a = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport1a.setComment(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_COMMENT);
		transport1a.setAmount(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_AMOUNT);
		transport1a.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		transport1a.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		moneyflowTransports.add(transport1a);
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow2().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_contractpartner_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		final MoneyflowTransport transport1a = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport1a.setComment(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_COMMENT);
		transport1a.setAmount(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_AMOUNT);
		transport1a.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		transport1a.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		final MoneyflowTransport transport1b = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport1b.setComment(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_COMMENT);
		transport1b.setAmount(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_AMOUNT);
		transport1b.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		transport1b.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		moneyflowTransports.add(transport1a);
		moneyflowTransports.add(transport1b);
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow2().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow3().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow4().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow5().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow6().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow7().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow8().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow9().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow10().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow11().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow12().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow15().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow16().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow17().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_searchStringContractpartnerPostingAccount_successfull() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		transport.setContractpartnerId(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		transport.setSearchString("generated");
		request.setMoneyflowSearchParamsTransport(transport);
		final SearchMoneyflowsResponse expected = new SearchMoneyflowsResponse();
		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow2().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsResponse actual = super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);

		this.assertEquals(expected, actual);
	}

	private void fillYearlySearchGenerated(final SearchMoneyflowsResponse expected) {
		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow2().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow3().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow4().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow5().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow6().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow7().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow8().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow9().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow10().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow11().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow12().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow15().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow16().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow17().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
		expected.setMoneyflowTransports(moneyflowTransports);
	}

	@Test
	void test_noTransport_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final ValidationResponse expected = new ValidationResponse();
		final ValidationItemTransport validationItemTransport1 = new ValidationItemTransport();
		validationItemTransport1.setError(ErrorCode.NO_SEARCH_CRITERIA_ENTERED.getErrorCode());
		final List<ValidationItemTransport> validationItemTransports = Arrays.asList(validationItemTransport1);
		expected.setValidationItemTransports(validationItemTransports);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new SearchMoneyflowsRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final SearchMoneyflowsRequest request = new SearchMoneyflowsRequest();
		final MoneyflowSearchParamsTransport transport = new MoneyflowSearchParamsTransport();
		transport.setSearchString("hugo");
		request.setMoneyflowSearchParamsTransport(transport);

		super.callUsecaseExpect200(request, SearchMoneyflowsResponse.class);
	}
}
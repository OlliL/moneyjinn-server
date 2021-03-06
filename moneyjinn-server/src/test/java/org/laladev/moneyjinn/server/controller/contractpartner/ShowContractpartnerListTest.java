package org.laladev.moneyjinn.server.controller.contractpartner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.ShowContractpartnerListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.impl.SettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowContractpartnerListTest extends AbstractControllerTest {

	@Inject
	private SettingService settingService;
	@Inject
	private IContractpartnerService contractpartnerService;

	private final HttpMethod method = HttpMethod.GET;
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

	private ShowContractpartnerListResponse getCompleteResponse() {
		final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q', 'S')));

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner3().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner4().build());
		expected.setContractpartnerTransports(contractpartnerTransports);
		expected.setCurrentlyValid(false);

		return expected;
	}

	private ShowContractpartnerListResponse getCurrentlyValidResponse() {
		final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q')));

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		expected.setContractpartnerTransports(contractpartnerTransports);
		expected.setCurrentlyValid(true);

		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(10);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		// set default to 0
		ShowContractpartnerListResponse expected = this.getCompleteResponse();
		ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0", this.method,
				false, ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// now the new default 0 must be taken
		actual = super.callUsecaseWithoutContent("/currentlyValid/", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// this must change the default-setting to 1
		expected = this.getCurrentlyValidResponse();
		actual = super.callUsecaseWithoutContent("/currentlyValid/1", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// now the default 1 must be taken
		actual = super.callUsecaseWithoutContent("/currentlyValid/", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
		final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q', 'S')));

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0", this.method,
				false, ShowContractpartnerListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_explicitAll_FullResponseObject() throws Exception {
		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		// set default to 0
		ShowContractpartnerListResponse expected = this.getCompleteResponse();
		ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0", this.method,
				false, ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// now the new default 0 must be taken
		actual = super.callUsecaseWithoutContent("/all/currentlyValid/", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// this must change the default-setting to 1
		expected = this.getCurrentlyValidResponse();
		actual = super.callUsecaseWithoutContent("/all/currentlyValid/1", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// now the default 1 must be taken
		actual = super.callUsecaseWithoutContent("/all/currentlyValid/", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

	}

	@Test
	public void test_initialQ_AResponseObject() throws Exception {
		// set default to 0
		ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q', 'S')));
		List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner3().build());
		expected.setContractpartnerTransports(contractpartnerTransports);
		expected.setCurrentlyValid(false);
		ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/Q/currentlyValid/0", this.method,
				false, ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// now the new default 0 must be taken
		actual = super.callUsecaseWithoutContent("/Q/currentlyValid/", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// this must change the default-setting to 1
		expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q')));
		contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		expected.setContractpartnerTransports(contractpartnerTransports);
		expected.setCurrentlyValid(true);
		actual = super.callUsecaseWithoutContent("/Q/currentlyValid/1", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

		// now the default 1 must be taken
		actual = super.callUsecaseWithoutContent("/Q/currentlyValid/", this.method, false,
				ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

	}

	@Test
	public void test_initialUnderscore_AResponseObject() throws Exception {
		// make sure that requesting data starting with _ only returns matching data and _ is not
		// interpreted as LIKE SQL special char
		final Contractpartner contractpartner = new Contractpartner();
		contractpartner.setUser(new User(new UserID(UserTransportBuilder.USER1_ID)));
		contractpartner.setAccess(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
		contractpartner.setName("_1");
		contractpartner.setValidFrom(LocalDate.now());
		contractpartner.setValidTil(LocalDate.now());
		this.contractpartnerService.createContractpartner(contractpartner);

		final ContractpartnerTransport contractpartnerTransport = new ContractpartnerTransport();
		contractpartnerTransport.setId(ContractpartnerTransportBuilder.NEXT_ID);
		contractpartnerTransport.setUserid(UserTransportBuilder.USER1_ID);
		contractpartnerTransport.setName(contractpartner.getName());
		contractpartnerTransport.setValidFrom(DateUtil.getGmtDate(contractpartner.getValidFrom().toString()));
		contractpartnerTransport.setValidTil(DateUtil.getGmtDate(contractpartner.getValidTil().toString()));

		final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q', 'S', '_')));
		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(contractpartnerTransport);
		expected.setContractpartnerTransports(contractpartnerTransports);
		expected.setCurrentlyValid(false);
		final ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/_/currentlyValid/0",
				this.method, false, ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_initialPercent_AResponseObject() throws Exception {
		// make sure that requesting data starting with % only returns matching data and % is not
		// interpreted as LIKE SQL special char
		final Contractpartner contractpartner = new Contractpartner();
		contractpartner.setUser(new User(new UserID(UserTransportBuilder.USER1_ID)));
		contractpartner.setAccess(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
		contractpartner.setName("%1");
		contractpartner.setValidFrom(LocalDate.now());
		contractpartner.setValidTil(LocalDate.now());
		this.contractpartnerService.createContractpartner(contractpartner);

		final ContractpartnerTransport contractpartnerTransport = new ContractpartnerTransport();
		contractpartnerTransport.setId(ContractpartnerTransportBuilder.NEXT_ID);
		contractpartnerTransport.setUserid(UserTransportBuilder.USER1_ID);
		contractpartnerTransport.setName(contractpartner.getName());
		contractpartnerTransport.setValidFrom(DateUtil.getGmtDate(contractpartner.getValidFrom().toString()));
		contractpartnerTransport.setValidTil(DateUtil.getGmtDate(contractpartner.getValidTil().toString()));

		final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		expected.setInitials(new HashSet<>(Arrays.asList('P', 'Q', 'S', '%')));
		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(contractpartnerTransport);
		expected.setContractpartnerTransports(contractpartnerTransports);
		expected.setCurrentlyValid(false);
		final ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/%/currentlyValid/0",
				this.method, false, ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired1_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("//currentlyValid/", this.method, false,
				ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired2_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/", this.method, false,
				ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired3_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("//currentlyValid/0", this.method, false,
				ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired4_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0", this.method, false,
				ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
		final ShowContractpartnerListResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0",
				this.method, false, ShowContractpartnerListResponse.class);
		Assertions.assertEquals(expected, actual);

	}
}

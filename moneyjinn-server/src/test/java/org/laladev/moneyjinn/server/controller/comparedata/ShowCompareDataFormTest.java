package org.laladev.moneyjinn.server.controller.comparedata;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.ShowCompareDataFormResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataFormatTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.CompareDataFormatTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowCompareDataFormTest extends AbstractControllerTest {

	@Inject
	ISettingService settingService;
	@Inject
	IMonthlySettlementService monthlySettlementService;

	private final HttpMethod method = HttpMethod.GET;
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

	private ShowCompareDataFormResponse getDefaultResponse() {
		final ShowCompareDataFormResponse expected = new ShowCompareDataFormResponse();

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		final List<CompareDataFormatTransport> compareDataFormatTransports = new ArrayList<>();
		compareDataFormatTransports.add(new CompareDataFormatTransportBuilder().forCompareDataFormat2().build());
		compareDataFormatTransports.add(new CompareDataFormatTransportBuilder().forCompareDataFormat3().build());
		compareDataFormatTransports.add(new CompareDataFormatTransportBuilder().forCompareDataFormat4().build());
		compareDataFormatTransports.add(new CompareDataFormatTransportBuilder().forCompareDataFormat5().build());
		compareDataFormatTransports.add(new CompareDataFormatTransportBuilder().forCompareDataFormat6().build());
		expected.setCompareDataFormatTransports(compareDataFormatTransports);

		return expected;

	}

	@Test
	public void test_noSetting_defaultsResponse() throws Exception {
		final ShowCompareDataFormResponse expected = this.getDefaultResponse();

		final ShowCompareDataFormResponse actual = super.callUsecaseWithoutContent("", this.method, false, ShowCompareDataFormResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_witDefaultSelection_defaultsResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		final ClientCompareDataSelectedCapitalsource settingCapitalsource = new ClientCompareDataSelectedCapitalsource(
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		this.settingService.setClientCompareDataSelectedCapitalsource(userId, settingCapitalsource);

		final ClientCompareDataSelectedFormat settingFormat = new ClientCompareDataSelectedFormat(
				new CompareDataFormatID(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID));
		this.settingService.setClientCompareDataSelectedFormat(userId, settingFormat);

		final ClientCompareDataSelectedSourceIsFile settingSource = new ClientCompareDataSelectedSourceIsFile(Boolean.TRUE);
		this.settingService.setClientCompareDataSelectedSourceIsFile(userId, settingSource);

		final ShowCompareDataFormResponse expected = this.getDefaultResponse();
		expected.setSelectedCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		expected.setSelectedDataFormat(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID);
		expected.setSelectedSourceIsFile((short) 1);

		final ShowCompareDataFormResponse actual = super.callUsecaseWithoutContent("", this.method, false, ShowCompareDataFormResponse.class);

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
		super.callUsecaseWithoutContent("", this.method, false, ShowCompareDataFormResponse.class);
	}
}
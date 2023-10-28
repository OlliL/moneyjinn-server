
package org.laladev.moneyjinn.server.controller.comparedata;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.CompareDataFormatTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CompareDataControllerApi;
import org.laladev.moneyjinn.server.model.CompareDataFormatTransport;
import org.laladev.moneyjinn.server.model.ShowCompareDataFormResponse;
import org.laladev.moneyjinn.service.api.ISettingService;

import jakarta.inject.Inject;

class ShowCompareDataFormTest extends AbstractWebUserControllerTest {
	@Inject
	private ISettingService settingService;

	@Override
	protected void loadMethod() {
		super.getMock(CompareDataControllerApi.class).showCompareDataForm();
	}

	private ShowCompareDataFormResponse getDefaultResponse() {
		final ShowCompareDataFormResponse expected = new ShowCompareDataFormResponse();
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
	void test_noSetting_defaultsResponse() throws Exception {
		final ShowCompareDataFormResponse expected = this.getDefaultResponse();

		final ShowCompareDataFormResponse actual = super.callUsecaseExpect200(false, ShowCompareDataFormResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_witDefaultSelection_defaultsResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ClientCompareDataSelectedCapitalsource settingCapitalsource = new ClientCompareDataSelectedCapitalsource(
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		this.settingService.setClientCompareDataSelectedCapitalsource(userId, settingCapitalsource);
		final ClientCompareDataSelectedFormat settingFormat = new ClientCompareDataSelectedFormat(
				new CompareDataFormatID(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID));
		this.settingService.setClientCompareDataSelectedFormat(userId, settingFormat);
		final ClientCompareDataSelectedSourceIsFile settingSource = new ClientCompareDataSelectedSourceIsFile(
				Boolean.TRUE);
		this.settingService.setClientCompareDataSelectedSourceIsFile(userId, settingSource);
		final ShowCompareDataFormResponse expected = this.getDefaultResponse();
		expected.setSelectedCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		expected.setSelectedDataFormat(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID);
		expected.setSelectedSourceIsFile(1);

		final ShowCompareDataFormResponse actual = super.callUsecaseExpect200(false, ShowCompareDataFormResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(ShowCompareDataFormResponse.class);
	}
}
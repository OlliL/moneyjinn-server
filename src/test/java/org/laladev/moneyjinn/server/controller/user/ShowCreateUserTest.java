package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.user.ShowCreateUserResponse;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowCreateUserTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName("user", this.getClass());
	}

	@Test
	public void test_completeResponseObject() throws Exception {
		final ShowCreateUserResponse expected = new ShowCreateUserResponse();
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		expected.setGroupTransports(groupTransports);

		final ShowCreateUserResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowCreateUserResponse.class);

		Assert.assertEquals(expected, actual);
	}
}
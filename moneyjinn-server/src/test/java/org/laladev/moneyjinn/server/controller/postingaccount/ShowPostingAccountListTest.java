
package org.laladev.moneyjinn.server.controller.postingaccount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.PostingAccountControllerApi;
import org.laladev.moneyjinn.server.model.PostingAccountTransport;
import org.laladev.moneyjinn.server.model.ShowPostingAccountListResponse;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

import jakarta.inject.Inject;

class ShowPostingAccountListTest extends AbstractWebUserControllerTest {
	@Inject
	private IPostingAccountService postingAccountService;

	@Override
	protected void loadMethod() {
		super.getMock(PostingAccountControllerApi.class).showPostingAccountList();
	}

	private ShowPostingAccountListResponse getCompleteResponse() {
		final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);
		return expected;
	}

	@Test
	void test_default_FullResponseObject() throws Exception {
		final ShowPostingAccountListResponse expected = this.getCompleteResponse();

		final ShowPostingAccountListResponse actual = super.callUsecaseExpect200(ShowPostingAccountListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();

		final ShowPostingAccountListResponse actual = super.callUsecaseExpect200(ShowPostingAccountListResponse.class);

		Assertions.assertEquals(expected, actual);
	}
}

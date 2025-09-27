package org.laladev.moneyjinn.server.controller.postingaccount;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.PostingAccountControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

class DeletePostingAccountByIdTest extends AbstractAdminUserControllerTest {
    @Inject
    private IPostingAccountService postingAccountService;

    @Override
    protected void loadMethod() {
        super.getMock(PostingAccountControllerApi.class).deletePostingAccountById(null);
    }

    @Test
    void test_regularPostingAccountNoData_SuccessfullNoContent() throws Exception {
        PostingAccount postingAccount = this.postingAccountService
                .getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));
        Assertions.assertNotNull(postingAccount);

        super.callUsecaseExpect204WithUriVariables(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID);

        postingAccount = this.postingAccountService
                .getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));
        Assertions.assertNull(postingAccount);
    }

    @Test
    void test_nonExistingPostingAccount_SuccessfullNoContent() throws Exception {
        PostingAccount postingAccount = this.postingAccountService
                .getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.NON_EXISTING_ID));
        Assertions.assertNull(postingAccount);

        super.callUsecaseExpect204WithUriVariables(PostingAccountTransportBuilder.NON_EXISTING_ID);

        postingAccount = this.postingAccountService
                .getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.NON_EXISTING_ID));
        Assertions.assertNull(postingAccount);
    }

    @Test
    void test_regularPostingAccountWithData_SuccessfullNoContent() throws Exception {
        final ErrorResponse expected = new ErrorResponse();
        expected.setCode(ErrorCode.POSTINGACCOUNT_STILL_REFERENCED.getErrorCode());
        expected.setMessage(
                "The posting account cannot be deleted because it is still referenced by a flow of money or a predefined flow of money!");
        PostingAccount postingAccount = this.postingAccountService
                .getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));
        Assertions.assertNotNull(postingAccount);

        final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class,
                PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);

        postingAccount = this.postingAccountService
                .getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));
        Assertions.assertNotNull(postingAccount);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect204WithUriVariables(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
    }
}
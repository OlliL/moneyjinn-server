package org.laladev.moneyjinn.server.controller.crud.etf;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IEtfService;

class DeleteEtfTest extends AbstractEtfTest {
    @Inject
    private IEtfService etfService;
    @Inject
    private EtfFavoriteTestUtil favoriteTestUtil;

    @Override
    protected void loadMethod() {
        super.getMock().delete(null);
    }

    @Test
    void test_etfWithData_ErrorResponse() throws Exception {
        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
        final var userId = new UserID(UserTransportBuilder.USER1_ID);

        final ErrorResponse expected = new ErrorResponse();
        expected.setCode(ErrorCode.ETF_STILL_REFERENCED.getErrorCode());
        expected.setMessage("You may not delete an ETF while it is referenced by a flows or preliminary lump sums!");
        var etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNotNull(etf);

        final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class, EtfTransportBuilder.ETF_ID_1);

        etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNotNull(etf);
        this.favoriteTestUtil.assertDefaultFavorite(userId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_etfWithoutDataSameUser_Successful() throws Exception {
        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_3);
        final var userId = new UserID(UserTransportBuilder.USER1_ID);

        var etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNotNull(etf);

        this.favoriteTestUtil.assertDefaultFavorite(userId);
        this.favoriteTestUtil.makeFavorite(userId, etfId);
        this.favoriteTestUtil.assertIsFavorite(userId, etfId);

        super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.ETF_ID_3);

        etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNull(etf);
        this.favoriteTestUtil.assertNoFavoriteSet(userId);
    }

    @Test
    void test_etfWithoutDataSameGroup_Successful() throws Exception {
        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_4);
        final var userId = new UserID(UserTransportBuilder.USER3_ID);

        var etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNotNull(etf);

        super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.ETF_ID_4);

        etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNull(etf);
    }

    @Test
    void test_etfFromOtherGroup_nothingHappened() throws Exception {
        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_2);
        final var userId = new UserID(UserTransportBuilder.ADMIN_ID);

        var etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNotNull(etf);

        super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.ETF_ID_2);

        etf = this.etfService.getEtfById(userId, etfId);
        Assertions.assertNotNull(etf);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.NON_EXISTING_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.NON_EXISTING_ID);
    }
}
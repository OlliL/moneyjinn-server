package org.laladev.moneyjinn.server.controller.crud.etf;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.setting.ClientListEtfDepotDefaultEtfId;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.service.api.ISettingService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Named
public class EtfFavoriteTestUtil {
    private static final EtfID DEFAULT_FAVORITE_ETF_ID = new EtfID(EtfTransportBuilder.FAVORITE_ETF_ID);
    @Inject
    private ISettingService settingService;

    public void assertDefaultFavorite(final UserID userId) {
        this.assertIsFavorite(userId, DEFAULT_FAVORITE_ETF_ID);
    }

    public void assertDefaultIsNotFavorite(final UserID userId) {
        this.assertIsNotFavorite(userId, DEFAULT_FAVORITE_ETF_ID);
    }

    public void assertIsFavorite(final UserID userId, final EtfID etfId) {
        final Optional<ClientListEtfDepotDefaultEtfId> setting = this.settingService
                .getClientListEtfDepotDefaultEtfId(userId);
        assertTrue(setting.isPresent());
        final var favoriteEtfId = setting.get().getSetting();
        assertEquals(etfId, favoriteEtfId);
    }

    public void assertIsNotFavorite(final UserID userId, final EtfID etfId) {
        final Optional<ClientListEtfDepotDefaultEtfId> setting = this.settingService
                .getClientListEtfDepotDefaultEtfId(userId);
        if (setting.isPresent()) {
            final var favoriteEtfId = setting.get().getSetting();
            assertNotEquals(etfId, favoriteEtfId);
        }
    }

    public void makeFavorite(final UserID userId, final EtfID etfId) {
        this.settingService.setClientListEtfDepotDefaultEtfId(userId, new ClientListEtfDepotDefaultEtfId(etfId));
    }

    public void assertNoFavoriteSet(final UserID userId) {
        final Optional<ClientListEtfDepotDefaultEtfId> setting = this.settingService
                .getClientListEtfDepotDefaultEtfId(userId);
        assertFalse(setting.isPresent());
    }
}

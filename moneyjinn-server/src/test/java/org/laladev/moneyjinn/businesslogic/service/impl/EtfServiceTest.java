package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IEtfService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class EtfServiceTest extends AbstractTest {
    private static final UserID USER_ID = new UserID(1L);
    @Inject
    private IEtfService etfService;

    @Test
    void test_createEtfFlowWithInvalidEntity_raisesException() {
        final EtfFlow etfFlow = new EtfFlow();
        assertThrows(BusinessException.class, () -> this.etfService.createEtfFlow(USER_ID, etfFlow));
    }

    @Test
    void test_updateEtfFlowWithInvalidEntity_raisesException() {
        final EtfFlow etfFlow = new EtfFlow();
        assertThrows(BusinessException.class, () -> this.etfService.updateEtfFlow(USER_ID, etfFlow));
    }

    @Test
    void test_createEtfWithInvalidEntity_raisesException() {
        final Etf etf = new Etf();
        etf.setUser(new User(USER_ID));
        etf.setGroup(new Group(new GroupID(2L)));
        assertThrows(BusinessException.class, () -> this.etfService.createEtf(etf));
    }

    @Test
    void test_updateEtfWithInvalidEntity_raisesException() {
        final Etf etf = new Etf();
        etf.setUser(new User(USER_ID));
        etf.setGroup(new Group(new GroupID(2L)));
        assertThrows(BusinessException.class, () -> this.etfService.updateEtf(etf));
    }

    @Test
    void test_userAeditsEtf_userBsameGroupSeesPreviouslyCachedChange() {
        final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
        final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);

        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
        // this caches
        var etf = this.etfService.getEtfById(user1Id, etfId);
        assertNotNull(etf);
        etf = this.etfService.getEtfById(user2Id, etfId);
        assertNotNull(etf);

        final String name = String.valueOf(System.currentTimeMillis());
        etf.getUser().setId(user2Id);
        etf.setName(name);
        // this should also modify the cache of user 1!
        this.etfService.updateEtf(etf);
        // this should now retrieve the changed entry!
        etf = this.etfService.getEtfById(user1Id, etfId);
        assertEquals(name, etf.getName());
    }

    @Test
    void test_userAdeletesEtf_userBsameGroupAlsoCantGetIt() {
        final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
        final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);

        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_3);
        // this caches
        var etf = this.etfService.getEtfById(user1Id, etfId);
        assertNotNull(etf);
        etf = this.etfService.getEtfById(user2Id, etfId);
        assertNotNull(etf);

        // this should also modify the cache of user 1!
        this.etfService.deleteEtf(user2Id, new GroupID(GroupTransportBuilder.GROUP1_ID), etfId);
        // this should now retrieve the changed entry!
        etf = this.etfService.getEtfById(user1Id, etfId);
        assertNull(etf);
    }

    @Test
    void test_createEtfPreliminaryLumpSumWithInvalidEntity_raisesException() {
        final EtfPreliminaryLumpSum etfPreliminaryLumpSum = new EtfPreliminaryLumpSum();
        assertThrows(BusinessException.class,
                () -> this.etfService.createEtfPreliminaryLumpSum(USER_ID, etfPreliminaryLumpSum));
    }

    @Test
    void test_updateEtfPreliminaryLumpSumWithInvalidEntity_raisesException() {
        final EtfPreliminaryLumpSum etfPreliminaryLumpSum = new EtfPreliminaryLumpSum();
        assertThrows(BusinessException.class,
                () -> this.etfService.updateEtfPreliminaryLumpSum(USER_ID, etfPreliminaryLumpSum));
    }

    @Test
    void test_calculateEffectiveEtfFlows_LumpSumConsidersSalesLaterThisYear() {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
        final LocalDateTime untilDate = LocalDateTime.of(2009, 12, 31, 23, 59, 59);
        final var etfFlows = this.etfService.getAllEtfFlowsUntil(userId, etfId, untilDate);
        final var effectiveFlows = this.etfService.calculateEffectiveEtfFlows(userId, etfFlows, untilDate);
        assertNotNull(effectiveFlows);
        assertEquals(3, effectiveFlows.size());
        assertEquals(0,
                new BigDecimal("5.47782002").compareTo(effectiveFlows.getFirst().getAccumulatedPreliminaryLumpSum()));
        assertEquals(0,
                new BigDecimal("128.75217998").compareTo(effectiveFlows.get(1).getAccumulatedPreliminaryLumpSum()));
        assertEquals(0,
                new BigDecimal("9.22").compareTo(effectiveFlows.get(2).getAccumulatedPreliminaryLumpSum()));
    }

    @Test
    void test_calculateEffectiveEtfFlows_LumpSumConsidersSalesNextYear() {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
        final LocalDateTime untilDate = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
        final var etfFlows = this.etfService.getAllEtfFlowsUntil(userId, etfId, untilDate);
        final var effectiveFlows = this.etfService.calculateEffectiveEtfFlows(userId, etfFlows, untilDate);
        assertNotNull(effectiveFlows);
        assertEquals(4, effectiveFlows.size());

        assertEquals(0,
                new BigDecimal("6.90942849").compareTo(effectiveFlows.getFirst().getAccumulatedPreliminaryLumpSum()));
        assertEquals(2, effectiveFlows.getFirst().getPreliminaryLumpSumPerYear().size());
        assertEquals(0,
                new BigDecimal("3.16884308").compareTo(effectiveFlows.getFirst().getPreliminaryLumpSumPerYear().get(
                        Year.of(2009))));
        assertEquals(0,
                new BigDecimal("3.74058541").compareTo(effectiveFlows.getFirst().getPreliminaryLumpSumPerYear().get(
                        Year.of(2010))));

        assertEquals(0,
                new BigDecimal("135.62552277").compareTo(effectiveFlows.get(1).getAccumulatedPreliminaryLumpSum()));
        assertEquals(0,
                new BigDecimal("122.78853913").compareTo(effectiveFlows.get(2).getAccumulatedPreliminaryLumpSum()));
        assertEquals(0,
                new BigDecimal("46.04570217").compareTo(effectiveFlows.get(3).getAccumulatedPreliminaryLumpSum()));
    }
}

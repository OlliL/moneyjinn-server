
package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.service.dao.data.AccessRelationData;
import org.laladev.moneyjinn.service.dao.data.mapper.AccessRelationDataMapper;

class AccessRelationDataMapperTest extends AbstractTest {
  @Inject
  private AccessRelationDataMapper accessRelationDataMapper;

  @Test
  void testNoParentAccessRelation() {
    final AccessRelation accessRelation = new AccessRelation(new AccessID(0l));
    final AccessRelationData accessRelationData = this.accessRelationDataMapper
        .mapAToB(accessRelation);
    Assertions.assertNull(accessRelationData.getRefId());
  }
}

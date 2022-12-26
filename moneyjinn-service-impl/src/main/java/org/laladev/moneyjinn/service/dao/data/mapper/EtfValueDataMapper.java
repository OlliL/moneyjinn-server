
package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;

public class EtfValueDataMapper implements IMapper<EtfValue, EtfValueData> {
  @Override
  public EtfValue mapBToA(final EtfValueData b) {
    final EtfValue a = new EtfValue();
    a.setIsin(new EtfIsin(b.getIsin()));
    a.setDate(b.getDate());
    a.setBuyPrice(b.getBuyPrice());
    a.setSellPrice(b.getSellPrice());
    a.setChangeDate(b.getChangedate());
    return a;
  }

  @Override
  public EtfValueData mapAToB(final EtfValue a) {
    return null;
  }
}

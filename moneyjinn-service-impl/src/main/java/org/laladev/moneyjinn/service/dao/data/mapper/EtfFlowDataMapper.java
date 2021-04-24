package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;

public class EtfFlowDataMapper implements IMapper<EtfFlow, EtfFlowData> {

	@Override
	public EtfFlow mapBToA(final EtfFlowData b) {
		final EtfFlow a = new EtfFlow();
		a.setId(new EtfFlowID(b.getEtfflowid()));

		a.setIsin(new EtfIsin(b.getIsin()));
		a.setTime(b.getFlowdate());
		a.setAmount(b.getAmount());
		a.setPrice(b.getPrice());

		return a;
	}

	@Override
	public EtfFlowData mapAToB(final EtfFlow a) {
		final EtfFlowData b = new EtfFlowData();
		b.setAmount(a.getAmount());
		if (a.getId() != null) {
			b.setEtfflowid(a.getId().getId());
		}
		b.setFlowdate(a.getTime());
		b.setIsin(a.getIsin().getId());
		b.setPrice(a.getPrice());
		return b;
	}

}

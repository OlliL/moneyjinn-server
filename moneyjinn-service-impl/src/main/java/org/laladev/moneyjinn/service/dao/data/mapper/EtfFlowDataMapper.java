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
		a.setDate(b.getFlowdate());
		a.setAmount(b.getAmount());
		a.setPrice(b.getPrice());

		return a;
	}

	@Override
	public EtfFlowData mapAToB(final EtfFlow a) {
		// TODO Auto-generated method stub
		return null;
	}

}

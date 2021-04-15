package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.service.dao.data.EtfData;

public class EtfDataMapper implements IMapper<Etf, EtfData> {

	@Override
	public Etf mapBToA(final EtfData b) {
		final Etf a = new Etf();
		a.setId(new EtfIsin(b.getIsin()));

		a.setName(b.getName());
		a.setWkn(b.getWkn());
		a.setTicker(b.getTicker());
		a.setChartUrl(b.getChartUrl());

		return a;
	}

	@Override
	public EtfData mapAToB(final Etf a) {
		// TODO Auto-generated method stub
		return null;
	}

}

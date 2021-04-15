package org.laladev.moneyjinn.service.dao.data;

public class EtfData {
	private String isin;
	private String name;
	private String wkn;
	private String ticker;
	private String chartUrl;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getWkn() {
		return this.wkn;
	}

	public final void setWkn(final String wkn) {
		this.wkn = wkn;
	}

	public final String getTicker() {
		return this.ticker;
	}

	public final void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	public final String getChartUrl() {
		return this.chartUrl;
	}

	public final void setChartUrl(final String chartUrl) {
		this.chartUrl = chartUrl;
	}

}

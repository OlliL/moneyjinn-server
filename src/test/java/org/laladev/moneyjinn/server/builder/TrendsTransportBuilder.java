package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.AbstractTrendsTransport;

public class TrendsTransportBuilder extends AbstractTrendsTransport {

	public TrendsTransportBuilder withYear(final Integer year) {
		super.setYear(year.shortValue());
		return this;
	}

	public TrendsTransportBuilder withMonth(final Integer month) {
		super.setMonth(month.shortValue());
		return this;
	}

	public TrendsTransportBuilder withAmount(final String amount) {
		super.setAmount(new BigDecimal(amount));
		return this;
	}

	public <T extends AbstractTrendsTransport> T build(final Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		final T transport = clazz.newInstance();

		transport.setYear(super.getYear());
		transport.setMonth(super.getMonth());
		transport.setAmount(super.getAmount());

		return transport;
	}
}

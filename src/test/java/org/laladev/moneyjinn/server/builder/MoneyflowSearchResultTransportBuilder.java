package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.moneyflow.transport.MoneyflowSearchResultTransport;

public class MoneyflowSearchResultTransportBuilder extends MoneyflowSearchResultTransport {

	public MoneyflowSearchResultTransportBuilder withYear(final int year) {
		super.setYear((short) year);
		return this;
	}

	public MoneyflowSearchResultTransportBuilder withMonth(final int month) {
		super.setMonth((short) month);
		return this;
	}

	public MoneyflowSearchResultTransportBuilder withContractpartnerId(final Long id) {
		super.setContractpartnerid(id);
		return this;
	}

	public MoneyflowSearchResultTransportBuilder withContractpartnerName(final String name) {
		super.setContractpartnername(name);
		return this;
	}

	public MoneyflowSearchResultTransportBuilder withComment(final String comment) {
		super.setComment(comment);
		return this;
	}

	public MoneyflowSearchResultTransportBuilder withAmount(final String amount) {
		super.setAmount(new BigDecimal(amount));
		return this;
	}

	public MoneyflowSearchResultTransport build() {
		final MoneyflowSearchResultTransport transport = new MoneyflowSearchResultTransport();

		transport.setComment(super.getComment());
		transport.setAmount(super.getAmount());
		transport.setContractpartnerid(super.getContractpartnerid());
		transport.setContractpartnername(super.getContractpartnername());
		transport.setYear(super.getYear());
		transport.setMonth(super.getMonth());

		return transport;
	}
}

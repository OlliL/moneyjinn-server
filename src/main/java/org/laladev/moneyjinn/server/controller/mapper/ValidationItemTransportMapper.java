package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;

public class ValidationItemTransportMapper implements IMapper<ValidationItemTransport, ValidationResultItem> {
	@Override
	public ValidationResultItem mapAToB(final ValidationItemTransport a) {
		throw new UnsupportedOperationException("Mapping not implemented!");
	}

	@Override
	public ValidationItemTransport mapBToA(final ValidationResultItem b) {
		final ValidationItemTransport a = new ValidationItemTransport();
		a.setKey(b.getKey().getId());
		a.setError(b.getError().getErrorCode());
		a.setVariableArray(b.getVariableArray());
		return a;
	}

}

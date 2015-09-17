package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;

public class ValidationItemTransportMapper implements IMapper<ValidationItemTransport, ValidationResultItem> {
	@Override
	public ValidationResultItem mapAToB(final ValidationItemTransport a) {
		throw new UnsupportedOperationException("Mapping not supported!");
	}

	@Override
	public ValidationItemTransport mapBToA(final ValidationResultItem b) {
		final ValidationItemTransport a = new ValidationItemTransport();
		// key is not set when reporting errors for objects to be created as they have no id yet
		if (b.getKey() != null) {
			a.setKey(b.getKey().getId());
		}
		a.setError(b.getError().getErrorCode());
		a.setVariableArray(b.getVariableArray());
		return a;
	}

}

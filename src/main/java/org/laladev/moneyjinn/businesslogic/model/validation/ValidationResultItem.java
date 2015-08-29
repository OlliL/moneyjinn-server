package org.laladev.moneyjinn.businesslogic.model.validation;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntityID;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;

public class ValidationResultItem {
	private AbstractEntityID<?> key;
	private ErrorCode error;
	private List<String> variableArray;

	public ValidationResultItem(final AbstractEntityID<?> key, final ErrorCode error,
			final List<String> variableArray) {
		super();
		this.key = key;
		this.error = error;
		this.variableArray = variableArray;
	}

	public ValidationResultItem(final AbstractEntityID<?> key, final ErrorCode error) {
		super();
		this.key = key;
		this.error = error;
	}

	public final AbstractEntityID<?> getKey() {
		return this.key;
	}

	public final void setKey(final AbstractEntityID<?> key) {
		this.key = key;
	}

	public final ErrorCode getError() {
		return this.error;
	}

	public final void setError(final ErrorCode error) {
		this.error = error;
	}

	public final List<String> getVariableArray() {
		return this.variableArray;
	}

	public final void setVariableArray(final List<String> variableArray) {
		this.variableArray = variableArray;
	}

}

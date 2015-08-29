package org.laladev.moneyjinn.businesslogic.model.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	public boolean result;
	public List<ValidationResultItem> validationResultItems;

	public ValidationResult() {
		this.result = true;
		this.validationResultItems = new ArrayList<ValidationResultItem>();
	}

	public boolean isValid() {
		return this.result;
	}

	public void addValidationResultItem(final ValidationResultItem validationResultItem) {
		this.result = false;
		this.validationResultItems.add(validationResultItem);
	}

	public List<ValidationResultItem> getValidationResultItems() {
		return this.validationResultItems;
	}

	public void mergeValidationResult(final ValidationResult validationResult) {
		if (!validationResult.isValid()) {
			this.result = false;
			this.validationResultItems.addAll(validationResult.getValidationResultItems());
		}
	}

}

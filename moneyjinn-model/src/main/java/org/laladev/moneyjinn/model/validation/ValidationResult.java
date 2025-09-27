package org.laladev.moneyjinn.model.validation;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Getter
    private final List<ValidationResultItem> validationResultItems;
    private boolean result;

    public ValidationResult() {
        this.result = true;
        this.validationResultItems = new ArrayList<>();
    }

    public boolean isValid() {
        return this.result;
    }

    public void addValidationResultItem(final ValidationResultItem validationResultItem) {
        this.result = false;
        this.validationResultItems.add(validationResultItem);
    }

    public void mergeValidationResult(final ValidationResult validationResult) {
        if (!validationResult.isValid()) {
            this.result = false;
            this.validationResultItems.addAll(validationResult.getValidationResultItems());
        }
    }
}

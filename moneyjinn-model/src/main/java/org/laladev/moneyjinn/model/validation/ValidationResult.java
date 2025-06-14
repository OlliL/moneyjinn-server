
package org.laladev.moneyjinn.model.validation;

import java.io.Serializable;

//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import java.util.ArrayList;
import java.util.List;

public class ValidationResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean result;
	private final List<ValidationResultItem> validationResultItems;

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

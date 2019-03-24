package org.laladev.moneyjinn.model.validation;

import java.io.Serializable;

//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.List;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.AbstractEntityID;

public class ValidationResultItem {
	private AbstractEntityID<? extends Serializable> key;
	private ErrorCode error;
	private List<String> variableArray;

	public ValidationResultItem(final AbstractEntityID<? extends Serializable> key, final ErrorCode error,
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

	public final AbstractEntityID<? extends Serializable> getKey() {
		return this.key;
	}

	public final void setKey(final AbstractEntityID<? extends Serializable> key) {
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

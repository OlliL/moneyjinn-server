//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// $Id: ValidationItemTransport.java,v 1.5 2015/08/23 20:31:10 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.transport;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("validationItemTransport")
public class ValidationItemTransport {
	private Object key;
	private Integer error;
	private List<String> variableArray;

	public final Object getKey() {
		return key;
	}

	public final void setKey(final Object key) {
		this.key = key;
	}

	public final Integer getError() {
		return error;
	}

	public final void setError(final Integer error) {
		this.error = error;
	}

	public final List<String> getVariableArray() {
		return variableArray;
	}

	public final void setVariableArray(final List<String> variableArray) {
		this.variableArray = variableArray;
	}

}

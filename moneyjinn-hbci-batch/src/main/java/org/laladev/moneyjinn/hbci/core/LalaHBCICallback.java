//
// Copyright (c) 2014-2015 Oliver Lehmann <lehmann@ans-netz.de>
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
package org.laladev.moneyjinn.hbci.core;

import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.passport.HBCIPassport;

public class LalaHBCICallback extends HBCICallbackConsole {
	private String pin;
	private String passportPassword;

	public LalaHBCICallback() {
	}

	public final void setPin(final String pin) {
		this.pin = pin;
	}

	public final void setPassportPassword(final String passportPassword) {
		this.passportPassword = passportPassword;
	}

	@Override
	public void callback(final HBCIPassport passport, final int reason, final String msg, final int dataType,
			final StringBuffer retData) {
		switch (reason) {
		case NEED_PASSPHRASE_LOAD:
		case NEED_PASSPHRASE_SAVE:
			retData.replace(0, retData.length(), this.passportPassword);
			break;
		case NEED_PT_PIN:
			if (passport != null && passport.getAccounts().length > 0) {
				retData.replace(0, retData.length(), this.pin);
			} else {
				super.callback(passport, reason, msg, dataType, retData);

			}
			break;
		case NEED_CONNECTION:
		case CLOSE_CONNECTION:
			break;
		default:
			super.callback(passport, reason, msg, dataType, retData);
		}
	}

	@Override
	public synchronized void status(final HBCIPassport passport, final int statusTag, final Object[] o) {
		switch (statusTag) {
		case STATUS_MSG_RAW_RECV:
		case STATUS_MSG_RAW_SEND:
			break;
		default:
			super.status(passport, statusTag, o);
		}
	}
}

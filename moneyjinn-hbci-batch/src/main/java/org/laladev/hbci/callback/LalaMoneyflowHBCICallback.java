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
// $Id: LalaMoneyflowHBCICallback.java,v 1.4 2015/03/31 18:55:58 olivleh1 Exp $
//
package org.laladev.hbci.callback;

import java.util.Properties;

import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.structures.Konto;

public class LalaMoneyflowHBCICallback extends HBCICallbackConsole {
	private final Properties properties;

	public LalaMoneyflowHBCICallback(final Properties properties) {
		this.properties = properties;
	}

	@Override
	public void callback(final HBCIPassport passport, final int reason, final String msg, final int dataType,
			final StringBuffer retData) {
		switch (reason) {
		case NEED_PASSPHRASE_LOAD:
		case NEED_PASSPHRASE_SAVE:
			retData.replace(0, retData.length(), properties.getProperty("hbci.passport.password"));
			break;
		case NEED_PT_PIN:
			if (passport != null && passport.getAccounts().length > 0) {
				final Konto konto = passport.getAccounts()[0];
				final String pin = properties.getProperty("hbci." + konto.number + ".pin");
				if (pin == null) {
					throw new RuntimeException("pin for account " + konto.number + " not defined as property (hbci."
							+ konto.number + ".pin)");
				}
				retData.replace(0, retData.length(), pin);
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

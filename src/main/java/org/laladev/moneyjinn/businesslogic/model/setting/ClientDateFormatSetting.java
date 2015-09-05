package org.laladev.moneyjinn.businesslogic.model.setting;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

import org.laladev.moneyjinn.businesslogic.model.access.User;

/**
 * <p>
 * This Setting describes the maximum number of entities the user wants to see in the "default view"
 * when listing Entites ({@link User}, {@link Capitalsource}, ...) before the default switches to
 * "show nothing".
 * </p>
 * <p>
 * This helps to prevent showing a massive amount of data when navigating into the entity for just
 * looking up a specific entity
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientDateFormatSetting extends AbstractSetting<String> {

	public ClientDateFormatSetting(final String setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_DATE_FORMAT;
	}

}

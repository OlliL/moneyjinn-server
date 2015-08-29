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
// $Id: GetUserSettingsForStartupResponse.java,v 1.2 2015/08/26 19:06:17 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("getUserSettingsForStartupResponse")
public class GetUserSettingsForStartupResponse extends AbstractResponse {
	private Long userId;
	private String settingDateFormat;
	private Integer settingDisplayedLanguage;
	private Boolean permissionAdmin;
	private Boolean attributeNew;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	/**
	 * @deprecated the client should hold that information
	 * @param settingDateFormat
	 */
	@Deprecated
	public String getSettingDateFormat() {
		return this.settingDateFormat;
	}

	/**
	 * @deprecated the client should hold that information
	 * @param settingDateFormat
	 */
	@Deprecated
	public void setSettingDateFormat(final String settingDateFormat) {
		this.settingDateFormat = settingDateFormat;
	}

	/**
	 * @deprecated the client should hold that information
	 * @param settingDateFormat
	 */
	@Deprecated
	public Integer getSettingDisplayedLanguage() {
		return this.settingDisplayedLanguage;
	}

	/**
	 * @deprecated the client should hold that information
	 * @param settingDateFormat
	 */
	@Deprecated
	public void setSettingDisplayedLanguage(final Integer settingDisplayedLanguage) {
		this.settingDisplayedLanguage = settingDisplayedLanguage;
	}

	public Boolean getPermissionAdmin() {
		return this.permissionAdmin;
	}

	public void setPermissionAdmin(final Boolean permissionAdmin) {
		this.permissionAdmin = permissionAdmin;
	}

	public Boolean getAttributeNew() {
		return this.attributeNew;
	}

	public void setAttributeNew(final Boolean attributeNew) {
		this.attributeNew = attributeNew;
	}

	@Override
	public String toString() {
		return "GetUserSettingsForStartupResponse [userId=" + this.userId + ", settingDateFormat="
				+ this.settingDateFormat + ", settingDisplayedLanguage=" + this.settingDisplayedLanguage
				+ ", permissionAdmin=" + this.permissionAdmin + ", attributeNew=" + this.attributeNew + "]";
	}

}

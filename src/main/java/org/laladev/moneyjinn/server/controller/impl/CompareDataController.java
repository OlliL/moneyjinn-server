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

package org.laladev.moneyjinn.server.controller.impl;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.ICompareDataService;
import org.laladev.moneyjinn.core.rest.model.comparedata.ShowCompareDataFormResponse;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/comparedata/")
public class CompareDataController extends AbstractController {

	@Inject
	ICompareDataService compareDataService;

	@Override
	protected void addBeanMapper() {
		// No Mapping needed.
	}

	@RequestMapping(value = "showCompareDataForm", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowCompareDataFormResponse showCompareDataForm() {
		final UserID userId = super.getUserId();
		final ShowCompareDataFormResponse response = new ShowCompareDataFormResponse();

		return response;
	}

}

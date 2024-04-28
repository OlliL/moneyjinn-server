//Copyright (c) 2023-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl.crud;

import java.time.Year;
import java.util.List;

import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumIDValues;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudEtfPreliminaryLumpSumControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.EtfPreliminaryLumpSumTransportMapper;
import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CrudEtfPreliminaryLumpSumController extends AbstractController
		implements CrudEtfPreliminaryLumpSumControllerApi {
	private final IEtfService etfService;
	private final EtfPreliminaryLumpSumTransportMapper etfPreliminaryLumpSumTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.etfPreliminaryLumpSumTransportMapper);
	}

	@Override
	public ResponseEntity<List<Integer>> readAllYears(@PathVariable("isin") final String isin) {
		final EtfIsin etfIsin = new EtfIsin(isin);

		final List<Year> allYears = this.etfService.getAllEtfPreliminaryLumpSumYears(etfIsin);

		return allYears.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(allYears.stream().map(Year::getValue).toList());
	}

	@Override
	public ResponseEntity<EtfPreliminaryLumpSumTransport> readOne(@PathVariable("isin") final String isin,
			@PathVariable("year") final Integer requestYear) {
		final EtfIsin etfIsin = new EtfIsin(isin);
		final Year year = Year.of(requestYear);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfIsin, year));

		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);

		return etfPreliminaryLumpSum == null ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(super.map(etfPreliminaryLumpSum, EtfPreliminaryLumpSumTransport.class));
	}

	@Override
	public ResponseEntity<EtfPreliminaryLumpSumTransport> create(
			@RequestBody final EtfPreliminaryLumpSumTransport transport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = super.map(transport, EtfPreliminaryLumpSum.class);
		final ValidationResult validationResult = this.etfService.validateEtfPreliminaryLumpSum(etfPreliminaryLumpSum);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.etfService.createEtfPreliminaryLumpSum(etfPreliminaryLumpSum);

		return this.preferedReturn(prefer, etfPreliminaryLumpSum, EtfPreliminaryLumpSumTransport.class);
	}

	@Override
	public ResponseEntity<EtfPreliminaryLumpSumTransport> update(
			@RequestBody final EtfPreliminaryLumpSumTransport transport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = super.map(transport, EtfPreliminaryLumpSum.class);
		final ValidationResult validationResult = this.etfService.validateEtfPreliminaryLumpSum(etfPreliminaryLumpSum);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.etfService.updateEtfPreliminaryLumpSum(etfPreliminaryLumpSum);

		return this.preferedReturn(prefer, etfPreliminaryLumpSum, EtfPreliminaryLumpSumTransport.class);
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable("isin") final String isin,
			@PathVariable("year") final Integer requestYear) {
		final EtfIsin etfIsin = new EtfIsin(isin);
		final Year year = Year.of(requestYear);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfIsin, year));

		this.etfService.deleteEtfPreliminaryLumpSum(id);

		return ResponseEntity.noContent().build();
	}
}
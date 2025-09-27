//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.service.dao.data.CapitalsourceData;
import org.laladev.moneyjinn.service.dao.mapper.ICapitalsourceDaoMapper;

import java.time.LocalDate;
import java.util.List;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CapitalsourceDao {
    private final ICapitalsourceDaoMapper mapper;

    public List<CapitalsourceData> getAllCapitalsources(final Long userId) {
        return this.mapper.getAllCapitalsources(userId);
    }

    public List<CapitalsourceData> getAllCapitalsourcesByDateRange(final Long userId, final LocalDate validFrom,
                                                                   final LocalDate validTil) {
        return this.mapper.getAllCapitalsourcesByDateRange(userId, validFrom, validTil);
    }

    public CapitalsourceData getCapitalsourceById(final Long userId, final Long magGroupId, final Long id) {
        return this.mapper.getCapitalsourceById(userId, magGroupId, id);
    }

    public CapitalsourceData getCapitalsourceByComment(final Long userId, final String comment, final LocalDate date) {
        return this.mapper.getCapitalsourceByComment(userId, comment, date);
    }

    public Long createCapitalsource(final CapitalsourceData capitalsourceData) {
        this.mapper.createCapitalsource(capitalsourceData);
        return capitalsourceData.getId();
    }

    public void updateCapitalsource(final CapitalsourceData capitalsourceData) {
        this.mapper.updateCapitalsource(capitalsourceData);
    }

    public void deleteCapitalsource(final Long userId, final Long magGroupId, final Long id) {
        this.mapper.deleteCapitalsource(userId, magGroupId, id);
    }

    public boolean checkCapitalsourceInUseOutOfDate(final Long userId, final Long id, final LocalDate validFrom,
                                                    final LocalDate validTil) {
        return Boolean.TRUE.equals(this.mapper.checkCapitalsourceInUseOutOfDate(userId, id, validFrom, validTil));
    }

    public List<CapitalsourceData> getGroupCapitalsourcesByDateRange(final Long userId, final LocalDate validFrom,
                                                                     final LocalDate validTil) {
        return this.mapper.getGroupCapitalsourcesByDateRange(userId, validFrom, validTil);
    }

    public CapitalsourceData getCapitalsourceByAccount(final String bankCode, final String accountNumber,
                                                       final LocalDate date) {
        return this.mapper.getCapitalsourceByAccount(bankCode, accountNumber, date);
    }
}

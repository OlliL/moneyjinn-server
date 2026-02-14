package org.laladev.moneyjinn.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.ContractpartnerMatching;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.dao.ContractpartnerMatchingDao;
import org.laladev.moneyjinn.service.dao.data.mapper.ContractpartnerMatchingDataMapper;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ContractpartnerMatchingService implements IContractpartnerMatchingService {
    private final IContractpartnerService contractpartnerService;
    private final ContractpartnerMatchingDao contractpartnerMatchingDao;
    private final ContractpartnerMatchingDataMapper contractpartnerMatchingDataMapper;

    @Override
    public ContractpartnerMatching getContractpartnerBySearchString(@NonNull final UserID userId,
                                                                    @NonNull final String searchString) {
        final var data = this.contractpartnerMatchingDao
                .getContractpartnerMatchingBySearchString(userId.getId(), searchString);

        if (data == null) {
            return null;
        }

        final var contractpartnerMatching = this.contractpartnerMatchingDataMapper.mapBToA(data);
        final var contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                contractpartnerMatching.getContractpartner().getId());
        contractpartnerMatching.setContractpartner(contractpartner);
        return contractpartnerMatching;
    }
}

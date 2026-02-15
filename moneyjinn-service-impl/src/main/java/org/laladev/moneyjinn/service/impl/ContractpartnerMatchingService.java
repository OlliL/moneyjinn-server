package org.laladev.moneyjinn.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerMatching;
import org.laladev.moneyjinn.model.ContractpartnerMatchingID;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.dao.ContractpartnerMatchingDao;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerMatchingData;
import org.laladev.moneyjinn.service.dao.data.mapper.ContractpartnerMatchingDataMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ContractpartnerMatchingService implements IContractpartnerMatchingService {
    private final IContractpartnerService contractpartnerService;
    private final IPostingAccountService postingAccountService;
    private final ContractpartnerMatchingDao contractpartnerMatchingDao;
    private final ContractpartnerMatchingDataMapper contractpartnerMatchingDataMapper;

    private ContractpartnerMatching mapContractpartnerMatchingData(final UserID userId,
                                                                   final ContractpartnerMatchingData contractpartnerMatchingData) {
        if (contractpartnerMatchingData != null) {
            final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingDataMapper
                    .mapBToA(contractpartnerMatchingData);
            final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                    contractpartnerMatching.getContractpartner().getId());
            // this secures the Account - a user which has no access to the partner may not
            // modify its accounts
            if (contractpartner != null) {
                contractpartnerMatching.setContractpartner(contractpartner);
                this.postingAccountService.enrichEntity(contractpartnerMatching);
                return contractpartnerMatching;
            }
        }
        return null;
    }

    private List<ContractpartnerMatching> mapContractpartnerMatchingDataList(final UserID userId,
                                                                             final List<ContractpartnerMatchingData> contractpartnerMatchingDataList) {
        return contractpartnerMatchingDataList.stream()
                .map(element -> this.mapContractpartnerMatchingData(userId, element)).toList();
    }

    @Override
    public ValidationResult validateContractpartnerMatching(final UserID userId,
                                                            final ContractpartnerMatching contractpartnerMatching) {
        final ValidationResult validationResult = new ValidationResult();
        final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
                new ValidationResultItem(contractpartnerMatching.getId(), errorCode));

        final boolean matchingTextIsEmpty = contractpartnerMatching.getMatchingText() == null ||
                contractpartnerMatching.getMatchingText().isBlank();
        if (matchingTextIsEmpty) {
            addResult.accept(ErrorCode.COMMENT_IS_NOT_SET);
        }

        if (contractpartnerMatching.getContractpartner() == null) {
            addResult.accept(ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
        } else {
            final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                    contractpartnerMatching.getContractpartner().getId());
            if (contractpartner == null) {
                addResult.accept(ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
            } else if (!matchingTextIsEmpty) {
                final var data = this.contractpartnerMatchingDao
                        .getContractpartnerMatchingByValue(contractpartner.getId().getId(),
                                contractpartnerMatching.getMatchingText());
                if (data != null && (contractpartnerMatching.getId() == null ||
                        !Objects.equals(data.getId(), contractpartnerMatching.getId().getId()))) {
                    addResult.accept(ErrorCode.CONTRACTPARTNER_MAPPING_DUPLICATE);
                }
            }
            contractpartnerMatching.setContractpartner(contractpartner);
        }

        if (contractpartnerMatching.getPostingAccount() != null) {
            final PostingAccount postingAccount = this.postingAccountService
                    .getPostingAccountById(contractpartnerMatching.getPostingAccount().getId());
            if (postingAccount == null) {
                addResult.accept(ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
            } else {
                contractpartnerMatching.setPostingAccount(postingAccount);
            }
        }

        return validationResult;
    }

    @Override
    public ContractpartnerMatching getContractpartnerMatchingBySearchString(@NonNull final UserID userId,
                                                                            @NonNull final String searchString,
                                                                            @NonNull final LocalDate bookingDate) {
        return this.mapContractpartnerMatchingData(userId, this.contractpartnerMatchingDao
                .getContractpartnerMatchingBySearchString(userId.getId(), searchString, bookingDate));
    }

    @Override
    public ContractpartnerMatching getContractpartnerMatchingById(@NonNull final UserID userId,
                                                                  @NonNull final ContractpartnerMatchingID contractpartnerMatchingId) {
        return this.mapContractpartnerMatchingData(userId, this.contractpartnerMatchingDao
                .getContractpartnerMatchingById(userId.getId(), contractpartnerMatchingId.getId()));
    }

    @Override
    public List<ContractpartnerMatching> getContractpartnerMatchings(@NonNull final UserID userId) {
        return this.mapContractpartnerMatchingDataList(userId, this.contractpartnerMatchingDao
                .getContractpartnerMatchings(userId.getId()));
    }

    @Override
    public ContractpartnerMatchingID createContractpartnerMatching(@NonNull final UserID userId,
                                                                   @NonNull final ContractpartnerMatching contractpartnerMatching) {
        contractpartnerMatching.setId(null);

        final ValidationResult validationResult = this.validateContractpartnerMatching(userId, contractpartnerMatching);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("ContractpartnerMatching creation failed!", validationResultItem.getError());
        }

        final ContractpartnerMatchingData contractpartnerMatchingData = this.contractpartnerMatchingDataMapper
                .mapAToB(contractpartnerMatching);
        final Long contractpartnerMatchingId = this.contractpartnerMatchingDao
                .createContractpartnerMatching(contractpartnerMatchingData);
        final ContractpartnerMatchingID id = new ContractpartnerMatchingID(contractpartnerMatchingId);
        contractpartnerMatching.setId(id);

        return id;
    }

    @Override
    public void updateContractpartnerMatching(@NonNull final UserID userId,
                                              @NonNull final ContractpartnerMatching contractpartnerMatching) {
        final ValidationResult validationResult = this.validateContractpartnerMatching(userId, contractpartnerMatching);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("ContractpartnerMatching update failed!", validationResultItem.getError());
        }
        final ContractpartnerMatching contractpartnerMatchingOld = this.getContractpartnerMatchingById(userId,
                contractpartnerMatching.getId());
        if (contractpartnerMatchingOld != null) {
            final ContractpartnerMatchingData contractpartnerMatchingData = this.contractpartnerMatchingDataMapper
                    .mapAToB(contractpartnerMatching);
            this.contractpartnerMatchingDao.updateContractpartnerMatching(contractpartnerMatchingData);
        }
    }

    @Override
    public void deleteContractpartnerMatchingById(@NonNull final UserID userId,
                                                  @NonNull final ContractpartnerMatchingID contractpartnerMatchingId) {
        final ContractpartnerMatching contractpartnerMatching = this.getContractpartnerMatchingById(userId,
                contractpartnerMatchingId);
        if (contractpartnerMatching != null) {
            this.contractpartnerMatchingDao.deleteContractpartnerMatching(contractpartnerMatchingId.getId());
        }
    }

}

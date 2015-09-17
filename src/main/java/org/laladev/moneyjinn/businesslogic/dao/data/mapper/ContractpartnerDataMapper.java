package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerData;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class ContractpartnerDataMapper implements IMapper<Contractpartner, ContractpartnerData> {

	@Override
	public Contractpartner mapBToA(final ContractpartnerData contractpartnerData) {
		final Contractpartner contractpartner = new Contractpartner(new ContractpartnerID(contractpartnerData.getId()));

		contractpartner.setUser(new User(new UserID(contractpartnerData.getMacIdCreator())));
		contractpartner.setAccess(new Group(new GroupID(contractpartnerData.getMacIdAccessor())));

		final LocalDate validFrom = contractpartnerData.getValidFrom().toLocalDate();
		final LocalDate validTil = contractpartnerData.getValidTil().toLocalDate();

		contractpartner.setValidFrom(validFrom);
		contractpartner.setValidTil(validTil);

		contractpartner.setName(contractpartnerData.getName());
		contractpartner.setStreet(contractpartnerData.getStreet());
		contractpartner.setPostcode(contractpartnerData.getPostcode());
		contractpartner.setTown(contractpartnerData.getTown());
		contractpartner.setCountry(contractpartnerData.getCountry());

		contractpartner.setMoneyflowComment(contractpartnerData.getMmfComment());

		final Long postingAccountId = contractpartnerData.getMpaPostingAccountId();
		if (postingAccountId != null) {
			contractpartner.setPostingAccount(new PostingAccount(new PostingAccountID(postingAccountId)));
		}

		return contractpartner;
	}

	@Override
	public ContractpartnerData mapAToB(final Contractpartner contractpartner) {
		final ContractpartnerData contractpartnerData = new ContractpartnerData();

		if (contractpartner.getId() != null) {
			contractpartnerData.setId(contractpartner.getId().getId());
		}

		if (contractpartner.getUser() != null) {
			contractpartnerData.setMacIdCreator(contractpartner.getUser().getId().getId());
		}
		if (contractpartner.getAccess() != null) {
			contractpartnerData.setMacIdAccessor(contractpartner.getAccess().getId().getId());
		}
		final Date validFrom = Date.valueOf(contractpartner.getValidFrom());
		final Date validTil = Date.valueOf(contractpartner.getValidTil());

		contractpartnerData.setValidFrom(validFrom);
		contractpartnerData.setValidTil(validTil);

		contractpartnerData.setName(contractpartner.getName());
		contractpartnerData.setStreet(contractpartner.getStreet());
		contractpartnerData.setPostcode(contractpartner.getPostcode());
		contractpartnerData.setTown(contractpartner.getTown());
		contractpartnerData.setCountry(contractpartner.getCountry());

		contractpartnerData.setMmfComment(contractpartner.getMoneyflowComment());

		final PostingAccount postingAccount = contractpartner.getPostingAccount();
		if (postingAccount != null) {
			contractpartnerData.setMpaPostingAccountId(postingAccount.getId().getId());
		}

		return contractpartnerData;
	}
}

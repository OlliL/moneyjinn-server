package org.laladev.moneyjinn.server.controller.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

public class ContractpartnerTransportMapper implements IMapper<Contractpartner, ContractpartnerTransport> {

	@Override
	public Contractpartner mapBToA(final ContractpartnerTransport contractpartnerTransport) {
		final Contractpartner contractpartner = new Contractpartner(
				new ContractpartnerID(contractpartnerTransport.getId()));

		contractpartner.setUser(new User(new UserID(contractpartnerTransport.getUserid())));

		if (contractpartnerTransport.getValidFrom() != null) {
			final LocalDate validFrom = contractpartnerTransport.getValidFrom().toLocalDate();
			contractpartner.setValidFrom(validFrom);
		}
		if (contractpartnerTransport.getValidTil() != null) {
			final LocalDate validTil = contractpartnerTransport.getValidTil().toLocalDate();
			contractpartner.setValidTil(validTil);
		}

		contractpartner.setName(contractpartnerTransport.getName());
		contractpartner.setStreet(contractpartnerTransport.getStreet());
		contractpartner.setPostcode(contractpartnerTransport.getPostcode());
		contractpartner.setTown(contractpartnerTransport.getTown());
		contractpartner.setCountry(contractpartnerTransport.getCountry());

		contractpartner.setMoneyflowComment(contractpartnerTransport.getMoneyflowComment());

		final Long postingAccountId = contractpartnerTransport.getPostingAccountId();
		if (postingAccountId != null) {
			contractpartner.setPostingAccount(new PostingAccount(new PostingAccountID(postingAccountId)));
		}

		return contractpartner;
	}

	@Override
	public ContractpartnerTransport mapAToB(final Contractpartner contractpartner) {
		final ContractpartnerID contractpartnerId = contractpartner.getId();
		final User user = contractpartner.getUser();
		final Long id = contractpartnerId == null ? null : contractpartnerId.getId();
		final Long userId = user == null ? null : user.getId().getId();
		final Date validFrom = Date.valueOf(contractpartner.getValidFrom());
		final Date validTil = Date.valueOf(contractpartner.getValidTil());

		Long postingAccountId = null;
		String postingAccountName = null;
		final PostingAccount postingAccount = contractpartner.getPostingAccount();
		if (postingAccount != null) {
			postingAccountId = postingAccount.getId().getId();
			postingAccountName = postingAccount.getName();
		}

		return new ContractpartnerTransport(id, userId, contractpartner.getName(), contractpartner.getStreet(),
				contractpartner.getPostcode(), contractpartner.getTown(), validTil, validFrom,
				contractpartner.getCountry(), contractpartner.getMoneyflowComment(), postingAccountName,
				postingAccountId);

	}
}

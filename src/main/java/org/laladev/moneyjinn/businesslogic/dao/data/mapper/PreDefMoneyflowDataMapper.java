package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class PreDefMoneyflowDataMapper implements IMapper<PreDefMoneyflow, PreDefMoneyflowData> {

	@Override
	public PreDefMoneyflow mapBToA(final PreDefMoneyflowData preDefMoneyflowData) {
		final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
		preDefMoneyflow.setId(new PreDefMoneyflowID(preDefMoneyflowData.getId()));
		preDefMoneyflow.setAmount(preDefMoneyflowData.getAmount());

		final LocalDate creationDate = preDefMoneyflowData.getCreatedate().toLocalDate();

		preDefMoneyflow.setCreationDate(creationDate);
		preDefMoneyflow
				.setCapitalsource(new Capitalsource(new CapitalsourceID(preDefMoneyflowData.getMcsCapitalsourceId())));
		preDefMoneyflow.setContractpartner(
				new Contractpartner(new ContractpartnerID(preDefMoneyflowData.getMcpContractpartnerId())));
		preDefMoneyflow.setComment(preDefMoneyflowData.getComment());
		if (preDefMoneyflowData.getLastUsed() != null) {
			final LocalDate lastUsed = preDefMoneyflowData.getLastUsed().toLocalDate();
			preDefMoneyflow.setLastUsedDate(lastUsed);
		}
		preDefMoneyflow.setUser(new User(new UserID(preDefMoneyflowData.getMacId())));
		preDefMoneyflow.setOnceAMonth(preDefMoneyflowData.isOnceAMonth());
		preDefMoneyflow.setPostingAccount(
				new PostingAccount(new PostingAccountID(preDefMoneyflowData.getMpaPostingAccountId())));

		return preDefMoneyflow;
	}

	@Override
	public PreDefMoneyflowData mapAToB(final PreDefMoneyflow preDefMoneyflow) {
		final PreDefMoneyflowData preDefMoneyflowData = new PreDefMoneyflowData();
		// might be null for new PreDefMoneyflows
		if (preDefMoneyflow.getId() != null) {
			preDefMoneyflowData.setId(preDefMoneyflow.getId().getId());
		}
		preDefMoneyflowData.setAmount(preDefMoneyflow.getAmount());
		if (preDefMoneyflow.getCreationDate() != null) {
			final Date creationDate = Date.valueOf(preDefMoneyflow.getCreationDate());
			preDefMoneyflowData.setCreatedate(creationDate);
		}
		preDefMoneyflowData.setMcsCapitalsourceId(preDefMoneyflow.getCapitalsource().getId().getId());
		preDefMoneyflowData.setMcpContractpartnerId(preDefMoneyflow.getContractpartner().getId().getId());
		preDefMoneyflowData.setComment(preDefMoneyflow.getComment());
		if (preDefMoneyflow.getLastUsedDate() != null) {
			final Date lastUserDate = Date.valueOf(preDefMoneyflow.getLastUsedDate());
			preDefMoneyflowData.setLastUsed(lastUserDate);
		}
		if (preDefMoneyflow.getUser() != null) {
			preDefMoneyflowData.setMacId(preDefMoneyflow.getUser().getId().getId());
		}
		preDefMoneyflowData.setOnceAMonth(preDefMoneyflow.isOnceAMonth());
		preDefMoneyflowData.setMpaPostingAccountId(preDefMoneyflow.getPostingAccount().getId().getId());

		return preDefMoneyflowData;
	}
}

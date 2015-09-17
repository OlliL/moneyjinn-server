package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.dao.data.CapitalsourceData;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class CapitalsourceDataMapper implements IMapper<Capitalsource, CapitalsourceData> {

	@Override
	public Capitalsource mapBToA(final CapitalsourceData capitalsourceData) {
		final Capitalsource capitalsource = new Capitalsource(new CapitalsourceID(capitalsourceData.getId()));
		if (capitalsourceData.getAccountNumber() != null) {
			capitalsource.setBankAccount(
					new BankAccount(capitalsourceData.getAccountNumber(), capitalsourceData.getBankCode()));
		}
		capitalsource.setComment(capitalsourceData.getComment());
		capitalsource.setGroupUse(capitalsourceData.isAttGroupUse());
		capitalsource.setImportAllowed(capitalsourceData.isImportAllowed());

		capitalsource.setState(CapitalsourceStateMapper.map(capitalsourceData.getState()));
		capitalsource.setType(CapitalsourceTypeMapper.map(capitalsourceData.getType()));

		capitalsource.setUser(new User(new UserID(capitalsourceData.getMacIdCreator())));
		capitalsource.setAccess(new Group(new GroupID(capitalsourceData.getMacIdAccessor())));

		final LocalDate validFrom = capitalsourceData.getValidFrom().toLocalDate();
		final LocalDate validTil = capitalsourceData.getValidTil().toLocalDate();

		capitalsource.setValidFrom(validFrom);
		capitalsource.setValidTil(validTil);

		return capitalsource;
	}

	@Override
	public CapitalsourceData mapAToB(final Capitalsource capitalsource) {
		final CapitalsourceData capitalsourceData = new CapitalsourceData();

		if (capitalsource.getId() != null) {
			capitalsourceData.setId(capitalsource.getId().getId());
		}

		final BankAccount bankAccount = capitalsource.getBankAccount();
		if (bankAccount != null) {
			capitalsourceData.setAccountNumber(bankAccount.getAccountNumber());
			capitalsourceData.setBankCode(bankAccount.getBankCode());
		}
		capitalsourceData.setComment(capitalsource.getComment());
		capitalsourceData.setAttGroupUse(capitalsource.isGroupUse());
		capitalsourceData.setImportAllowed(capitalsource.isImportAllowed());

		capitalsourceData.setState(CapitalsourceStateMapper.map(capitalsource.getState()));
		capitalsourceData.setType(CapitalsourceTypeMapper.map(capitalsource.getType()));

		if (capitalsource.getUser() != null) {
			capitalsourceData.setMacIdCreator(capitalsource.getUser().getId().getId());
		}
		if (capitalsource.getAccess() != null) {
			capitalsourceData.setMacIdAccessor(capitalsource.getAccess().getId().getId());
		}
		final Date validFrom = Date.valueOf(capitalsource.getValidFrom());
		final Date validTil = Date.valueOf(capitalsource.getValidTil());

		capitalsourceData.setValidFrom(validFrom);
		capitalsourceData.setValidTil(validTil);

		return capitalsourceData;
	}
}

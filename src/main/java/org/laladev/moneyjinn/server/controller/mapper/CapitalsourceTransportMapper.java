package org.laladev.moneyjinn.server.controller.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

public class CapitalsourceTransportMapper implements IMapper<Capitalsource, CapitalsourceTransport> {
	private static final Short GROUP_USE_SHORT = Short.valueOf((short) 1);
	private static final Short IMPORT_ALLOWED_SHORT = Short.valueOf((short) 1);

	@Override
	public Capitalsource mapBToA(final CapitalsourceTransport capitalsourceTransport) {
		final Capitalsource capitalsource = new Capitalsource(new CapitalsourceID(capitalsourceTransport.getId()));
		if (capitalsourceTransport.getAccountNumber() != null) {
			capitalsource.setBankAccount(
					new BankAccount(capitalsourceTransport.getAccountNumber(), capitalsourceTransport.getBankCode()));
		}
		capitalsource.setComment(capitalsourceTransport.getComment());
		if (capitalsourceTransport.getGroupUse() != null
				&& GROUP_USE_SHORT.equals(capitalsourceTransport.getGroupUse())) {
			capitalsource.setGroupUse(true);
		}
		if (capitalsourceTransport.getImportAllowed() != null
				&& IMPORT_ALLOWED_SHORT.equals(capitalsourceTransport.getImportAllowed())) {
			capitalsource.setImportAllowed(true);
		}

		capitalsource.setState(CapitalsourceStateMapper.map(capitalsourceTransport.getState()));
		capitalsource.setType(CapitalsourceTypeMapper.map(capitalsourceTransport.getType()));

		capitalsource.setUser(new User(new UserID(capitalsourceTransport.getUserid())));

		if (capitalsourceTransport.getValidFrom() != null) {
			final LocalDate validFrom = capitalsourceTransport.getValidFrom().toLocalDate();
			capitalsource.setValidFrom(validFrom);
		}
		if (capitalsourceTransport.getValidTil() != null) {
			final LocalDate validTil = capitalsourceTransport.getValidTil().toLocalDate();
			capitalsource.setValidTil(validTil);
		}
		return capitalsource;
	}

	@Override
	public CapitalsourceTransport mapAToB(final Capitalsource capitalsource) {
		final CapitalsourceTransport capitalsourceTransport = new CapitalsourceTransport();

		capitalsourceTransport.setId(capitalsource.getId().getId());

		final BankAccount bankAccount = capitalsource.getBankAccount();
		if (bankAccount != null) {
			capitalsourceTransport.setAccountNumber(bankAccount.getAccountNumber());
			capitalsourceTransport.setBankCode(bankAccount.getBankCode());
		}
		capitalsourceTransport.setComment(capitalsource.getComment());
		if (capitalsource.isGroupUse()) {
			capitalsourceTransport.setGroupUse(GROUP_USE_SHORT);
		}
		if (capitalsource.isImportAllowed()) {
			capitalsourceTransport.setImportAllowed(IMPORT_ALLOWED_SHORT);
		}

		capitalsourceTransport.setState(CapitalsourceStateMapper.map(capitalsource.getState()));
		capitalsourceTransport.setType(CapitalsourceTypeMapper.map(capitalsource.getType()));

		final User user = capitalsource.getUser();
		if (user != null) {
			capitalsourceTransport.setUserid(user.getId().getId());
		}
		final Date validFrom = Date.valueOf(capitalsource.getValidFrom());
		final Date validTil = Date.valueOf(capitalsource.getValidTil());

		capitalsourceTransport.setValidFrom(validFrom);
		capitalsourceTransport.setValidTil(validTil);

		return capitalsourceTransport;
	}
}

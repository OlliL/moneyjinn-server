package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.fixes.IFixHasBankAccount;
import org.laladev.moneyjinn.converter.fixes.IFixHasCapitalsource;
import org.laladev.moneyjinn.converter.fixes.IFixHasContractpartner;
import org.laladev.moneyjinn.converter.fixes.IFixHasPostingAccount;
import org.laladev.moneyjinn.converter.fixes.IFixHasUser;
import org.laladev.moneyjinn.core.mapper.IMapper;

public interface IMapstructMapper<A, B> extends IMapper<A, B>, IFixHasContractpartner, IFixHasPostingAccount,
		IFixHasUser, IFixHasBankAccount, IFixHasCapitalsource {

}

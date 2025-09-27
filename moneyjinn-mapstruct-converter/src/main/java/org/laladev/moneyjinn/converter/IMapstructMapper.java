package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.fixes.*;
import org.laladev.moneyjinn.core.mapper.IMapper;

public interface IMapstructMapper<A, B> extends IMapper<A, B>, IFixHasContractpartner, IFixHasPostingAccount,
        IFixHasUser, IFixHasBankAccount, IFixHasCapitalsource, IFixHasGroup {

}

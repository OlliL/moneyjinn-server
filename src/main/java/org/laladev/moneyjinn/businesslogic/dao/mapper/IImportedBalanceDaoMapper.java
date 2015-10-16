package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.dao.data.ImportedBalanceData;

public interface IImportedBalanceDaoMapper {

	void upsertImportedBalance(ImportedBalanceData importedBalanceData);

	List<ImportedBalanceData> getAllImportedBalancesByCapitalsourceIds(List<Long> capitalsourceIds);

}

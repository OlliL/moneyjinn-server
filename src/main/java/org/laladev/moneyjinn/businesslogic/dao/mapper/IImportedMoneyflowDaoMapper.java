package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMoneyflowData;

public interface IImportedMoneyflowDaoMapper {

	Integer countImportedMoneyflows(List<Long> capitalsourceIds);

	List<ImportedMoneyflowData> getAllImportedMoneyflowsByCapitalsourceIds(List<Long> capitalsourceIds);

	void deleteImportedMoneyflowById(Long impMoneyflowId);

	void createImportedMoneyflow(ImportedMoneyflowData importedMoneyflowData);

}

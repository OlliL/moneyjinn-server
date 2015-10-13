package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMonthlySettlementData;

public interface IImportedMonthlySettlementDaoMapper {

	List<ImportedMonthlySettlementData> getImportedMonthlySettlementsByMonth(@Param("year") Short year,
			@Param("month") Short month);

	void createImportedMonthlySettlement(ImportedMonthlySettlementData importedMonthlySettlementData);

}

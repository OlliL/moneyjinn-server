package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MonthlySettlementData;

public interface IMonthlySettlementDaoMapper {
	public List<Short> getAllYears(Long userId);

	public List<Short> getAllMonth(@Param("userId") Long userId, @Param("year") Short year);

	public List<MonthlySettlementData> getAllMonthlySettlementsByYearMonth(@Param("userId") Long userId,
			@Param("year") Short year, @Param("month") Short month);

}

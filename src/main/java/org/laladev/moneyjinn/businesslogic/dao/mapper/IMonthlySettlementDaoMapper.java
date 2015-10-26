package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MonthlySettlementData;

public interface IMonthlySettlementDaoMapper {
	public List<Short> getAllYears(Long userId);

	public List<Short> getAllMonth(@Param("userId") Long userId, @Param("year") Short year);

	public List<MonthlySettlementData> getAllMonthlySettlementsByYearMonth(@Param("userId") Long userId,
			@Param("year") Short year, @Param("month") Short month);

	public Date getMaxSettlementDate(Long userId);

	public Short checkMonthlySettlementsExists(@Param("userId") Long userId, @Param("year") Short year,
			@Param("month") Short month);

	public void upsertMonthlySettlement(MonthlySettlementData monthlySettlementData);

	public void deleteMonthlySettlement(@Param("userId") Long userId, @Param("year") Short year,
			@Param("month") Short month);

	public List<MonthlySettlementData> getAllMonthlySettlementsByRangeAndCapitalsource(@Param("userId") Long user,
			@Param("startYear") int startYear, @Param("startMonth") int startMonth, @Param("endYear") int endYear,
			@Param("endMonth") int endMonth, @Param("mcsCapitalsourceIds") List<Long> capitalsourceIdLongs);

}

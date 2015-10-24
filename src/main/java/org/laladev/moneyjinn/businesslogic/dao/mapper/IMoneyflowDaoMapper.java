package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;

public interface IMoneyflowDaoMapper {
	public void createMoneyflow(MoneyflowData moneyflowData);

	public MoneyflowData getMoneyflowById(@Param("userId") Long userId, @Param("id") Long id);

	public void updateMoneyflow(MoneyflowData moneyflowData);

	public void deleteMoneyflow(@Param("userId") Long userId, @Param("id") Long id);

	public BigDecimal getSumAmountByDateRangeForCapitalsourceIds(@Param("userId") Long userId,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil,
			@Param("mcsCapitalsourceIds") List<Long> capitalsourceIds);

	public List<Short> getAllYears(Long userId);

	public List<Short> getAllMonth(@Param("userId") Long userId, @Param("beginOfYear") Date beginOfYear,
			@Param("endOfYear") Date endOfYear);

	public List<MoneyflowData> getAllMoneyflowsByDateRange(@Param("userId") Long userId,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil);

	public Boolean monthHasMoneyflows(@Param("userId") Long userId, @Param("date") Date date);

}

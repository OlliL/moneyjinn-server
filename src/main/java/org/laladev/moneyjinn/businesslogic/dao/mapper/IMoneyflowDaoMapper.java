package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.math.BigDecimal;
import java.sql.Date;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;

public interface IMoneyflowDaoMapper {
	public void createMoneyflow(MoneyflowData moneyflowData);

	public MoneyflowData getMoneyflowById(@Param("userId") Long userId, @Param("id") Long id);

	public void updateMoneyflow(MoneyflowData moneyflowData);

	public void deleteMoneyflow(@Param("userId") Long userId, @Param("id") Long id);

	public BigDecimal getSumAmountByDateRangeForCapitalsourceId(@Param("userId") Long userId,
			@Param("validFrom") Date validFrom, @Param("validTil") Date validTil,
			@Param("mcsCapitalsourceId") Long capitalsourceId);

}

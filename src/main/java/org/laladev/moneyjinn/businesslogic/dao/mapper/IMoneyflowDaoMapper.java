package org.laladev.moneyjinn.businesslogic.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;

public interface IMoneyflowDaoMapper {
	public void createMoneyflow(MoneyflowData moneyflowData);

	public MoneyflowData getMoneyflowById(@Param("userId") Long userId, @Param("id") Long id);

	public void updateMoneyflow(MoneyflowData moneyflowData);

	public void deleteMoneyflow(@Param("userId") Long userId, @Param("id") Long id);

}

package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.data.PostingAccountAmountData;

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
			@Param("dateFrom") Date dateFrom, @Param("dateTil") Date dateTil);

	public Boolean monthHasMoneyflows(@Param("userId") Long userId, @Param("dateFrom") Date dateFrom,
			@Param("dateTil") Date dateTil);

	public Date getMaxMoneyflowDate(Long userId);

	public Date getPreviousMoneyflowDate(@Param("userId") Long userId, @Param("date") Date date);

	public Date getNextMoneyflowDate(@Param("userId") Long userId, @Param("date") Date date);

	public List<PostingAccountAmountData> getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(
			@Param("userId") Long userId, @Param("postingAccountIds") List<Long> postingAccountIdLongs,
			@Param("dateFrom") Date dateFrom, @Param("dateTil") Date dateTil);

	public List<PostingAccountAmountData> getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(
			@Param("userId") Long userId, @Param("postingAccountIds") List<Long> postingAccountIdLongs,
			@Param("dateFrom") Date dateFrom, @Param("dateTil") Date dateTil);

}

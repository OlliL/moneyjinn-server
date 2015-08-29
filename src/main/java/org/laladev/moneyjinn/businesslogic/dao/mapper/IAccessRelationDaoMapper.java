package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessRelationData;

public interface IAccessRelationDaoMapper {

	public AccessRelationData getAccessRelationById(@Param("id") Long id, @Param("date") Date date);

	public List<AccessRelationData> getAllAccessRelationsById(Long id);

	public List<AccessRelationData> getAllAccessRelationsByIdDate(@Param("id") Long id, @Param("date") Date date);

	public void deleteAllAccessRelation(Long id);

	public void deleteAccessRelationByDate(@Param("id") Long id, @Param("date") Date date);

	public void updateAccessRelation(@Param("id") Long id, @Param("date") Date date,
			@Param("accessRelationData") AccessRelationData accessRelationData);

	public void createAccessRelation(AccessRelationData accessRelationData);

	public void deleteAllAccessFlattened(Long id);

	public void deleteAccessFlattenedAfter(@Param("id") Long id, @Param("date") Date date);

	public void createAccessFlattened(AccessFlattenedData accessFlattenedData);

}

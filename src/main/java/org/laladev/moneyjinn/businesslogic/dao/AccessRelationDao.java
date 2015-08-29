package org.laladev.moneyjinn.businesslogic.dao;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessRelationData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IAccessRelationDaoMapper;

@Named
public class AccessRelationDao {

	@Inject
	IAccessRelationDaoMapper mapper;

	public AccessRelationData getAccessRelationById(final Long id, final Date date) {
		return this.mapper.getAccessRelationById(id, date);
	}

	public List<AccessRelationData> getAllAccessRelationsById(final Long id) {
		return this.mapper.getAllAccessRelationsById(id);
	}

	public List<AccessRelationData> getAllAccessRelationsByIdDate(final Long id, final Date date) {
		return this.mapper.getAllAccessRelationsByIdDate(id, date);
	}

	public void deleteAllAccessRelation(final Long id) {
		this.mapper.deleteAllAccessRelation(id);
	}

	public void deleteAccessRelationByDate(final Long id, final Date date) {
		this.mapper.deleteAccessRelationByDate(id, date);
	};

	public void updateAccessRelation(final Long id, final Date date, final AccessRelationData accessRelationData) {
		this.mapper.updateAccessRelation(id, date, accessRelationData);
	};

	public void createAccessRelation(final AccessRelationData accessRelationData) {
		this.mapper.createAccessRelation(accessRelationData);
	};

	public void deleteAllAccessFlattened(final Long id) {
		this.mapper.deleteAllAccessFlattened(id);
	}

	public void deleteAccessFlattenedAfter(final Long id, final Date date) {
		this.mapper.deleteAccessFlattenedAfter(id, date);
	};

	public void createAccessFlattened(final AccessFlattenedData accessFlattenedData) {
		this.mapper.createAccessFlattened(accessFlattenedData);
	};

}

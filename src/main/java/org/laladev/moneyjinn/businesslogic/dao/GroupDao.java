package org.laladev.moneyjinn.businesslogic.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.GroupData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IGroupDaoMapper;

@Named
public class GroupDao {

	@Inject
	IGroupDaoMapper mapper;

	public List<GroupData> getAllGroups() {
		return this.mapper.getAllGroups();
	}

	public GroupData getGroupById(final Long groupId) {
		return this.mapper.getGroupById(groupId);
	}

}

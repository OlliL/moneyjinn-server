package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.dao.data.GroupData;

public interface IGroupDaoMapper {
	public List<GroupData> getAllGroups();

	public GroupData getGroupById(Long groupId);
}

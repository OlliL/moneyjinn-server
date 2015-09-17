package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.businesslogic.dao.data.GroupData;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class GroupDataMapper implements IMapper<Group, GroupData> {

	@Override
	public Group mapBToA(final GroupData groupData) {
		return new Group(new GroupID(groupData.getId()), groupData.getName());
	}

	@Override
	public GroupData mapAToB(final Group group) {
		final GroupData groupData = new GroupData();
		// might be null for new users
		if (group.getId() != null) {
			groupData.setId(group.getId().getId());
		}
		groupData.setName(group.getName());
		return groupData;
	}
}

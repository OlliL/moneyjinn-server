package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.GroupData;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;

public class GroupDataMapper implements IMapper<Group, GroupData> {

	@Override
	public Group mapBToA(final GroupData groupData) {
		if (groupData == null) {
			return null;
		}
		final Group group = new Group(new GroupID(groupData.getId()), groupData.getName());
		return group;
	}

	@Override
	public GroupData mapAToB(final Group group) {
		if (group == null) {
			return null;
		}

		final GroupData groupData = new GroupData();
		groupData.setId(group.getId().getId());
		groupData.setName(group.getName());
		return groupData;
	}
}

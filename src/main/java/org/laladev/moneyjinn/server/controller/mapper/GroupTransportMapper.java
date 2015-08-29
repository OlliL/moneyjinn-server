package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

public class GroupTransportMapper implements IMapper<Group, GroupTransport> {

	@Override
	public Group mapBToA(final GroupTransport groupTransport) {
		if (groupTransport == null) {
			return null;
		}

		final Group group = new Group(new GroupID(groupTransport.getId()), groupTransport.getName());
		return group;
	}

	@Override
	public GroupTransport mapAToB(final Group group) {
		if (group == null) {
			return null;
		}

		final GroupTransport groupTransport = new GroupTransport();
		groupTransport.setId(group.getId().getId());
		groupTransport.setName(group.getName());

		return groupTransport;
	}
}

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

public class GroupTransportMapper implements IMapper<Group, GroupTransport> {

	@Override
	public Group mapBToA(final GroupTransport groupTransport) {
		return new Group(new GroupID(groupTransport.getId()), groupTransport.getName());
	}

	@Override
	public GroupTransport mapAToB(final Group group) {
		final GroupTransport groupTransport = new GroupTransport();
		groupTransport.setId(group.getId().getId());
		groupTransport.setName(group.getName());

		return groupTransport;
	}
}

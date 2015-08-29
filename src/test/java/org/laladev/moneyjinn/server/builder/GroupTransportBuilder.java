package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

public class GroupTransportBuilder extends GroupTransport {

	public GroupTransportBuilder forAdminGroup() {
		super.setId(1l);
		super.setName("admingroup");
		return this;
	}

	public GroupTransportBuilder forGroup1() {
		super.setId(5l);
		super.setName("group1");
		return this;
	}

	public GroupTransportBuilder forGroup2() {
		super.setId(6l);
		super.setName("group2");
		return this;
	}

	public GroupTransport build() {
		final GroupTransport transport = new GroupTransport();

		transport.setId(super.getId());
		transport.setName(super.getName());

		return transport;
	}
}

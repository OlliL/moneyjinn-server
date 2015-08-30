package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

public class GroupTransportBuilder extends GroupTransport {

	public static final String ADMINGROUP_NAME = "admingroup";
	public static final String GROUP1_NAME = "group1";
	public static final String GROUP2_NAME = "group2";

	public static final Long ADMINGROUP_ID = 1l;
	public static final Long GROUP1_ID = 5l;
	public static final Long GROUP2_ID = 6l;

	public GroupTransportBuilder forAdminGroup() {
		super.setId(ADMINGROUP_ID);
		super.setName(ADMINGROUP_NAME);
		return this;
	}

	public GroupTransportBuilder forGroup1() {
		super.setId(GROUP1_ID);
		super.setName(GROUP1_NAME);
		return this;
	}

	public GroupTransportBuilder forGroup2() {
		super.setId(GROUP2_ID);
		super.setName(GROUP2_NAME);
		return this;
	}

	public GroupTransport build() {
		final GroupTransport transport = new GroupTransport();

		transport.setId(super.getId());
		transport.setName(super.getName());

		return transport;
	}
}

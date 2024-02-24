
package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.GroupTransport;

public class GroupTransportBuilder extends GroupTransport {
	public static final String ADMINGROUP_NAME = "admingroup";
	public static final String GROUP1_NAME = "group1";
	public static final String GROUP2_NAME = "group2";
	public static final String GROUP3_NAME = "group3";
	public static final String NEWGROUP_NAME = "students";
	public static final Long ADMINGROUP_ID = 0l;
	public static final Long GROUP1_ID = 6l;
	public static final Long GROUP2_ID = 7l;
	public static final Long GROUP3_ID = 8l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 9l;

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

	public GroupTransportBuilder forGroup3() {
		super.setId(GROUP3_ID);
		super.setName(GROUP3_NAME);
		return this;
	}

	public GroupTransportBuilder forNewGroup() {
		super.setId(null);
		super.setName(NEWGROUP_NAME);
		return this;
	}

	public GroupTransport build() {
		final GroupTransport transport = new GroupTransport();
		transport.setId(super.getId());
		transport.setName(super.getName());
		return transport;
	}
}

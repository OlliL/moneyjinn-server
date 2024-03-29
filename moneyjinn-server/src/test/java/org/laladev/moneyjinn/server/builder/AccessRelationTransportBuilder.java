
package org.laladev.moneyjinn.server.builder;

import java.time.LocalDate;

import org.laladev.moneyjinn.server.model.AccessRelationTransport;

public class AccessRelationTransportBuilder extends AccessRelationTransport {
	public AccessRelationTransportBuilder forAdminUser() {
		super.setId(UserTransportBuilder.ADMIN_ID);
		super.setRefId(GroupTransportBuilder.ADMINGROUP_ID);
		super.setValidfrom(LocalDate.parse("2000-01-01"));
		super.setValidtil(LocalDate.parse("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2000_01_01() {
		super.setId(UserTransportBuilder.USER1_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(LocalDate.parse("2000-01-01"));
		super.setValidtil(LocalDate.parse("2599-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2600_01_01() {
		super.setId(UserTransportBuilder.USER1_ID);
		super.setRefId(GroupTransportBuilder.GROUP2_ID);
		super.setValidfrom(LocalDate.parse("2600-01-01"));
		super.setValidtil(LocalDate.parse("2699-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2700_01_01() {
		super.setId(UserTransportBuilder.USER1_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(LocalDate.parse("2700-01-01"));
		super.setValidtil(LocalDate.parse("2799-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2800_01_01() {
		super.setId(UserTransportBuilder.USER1_ID);
		super.setRefId(GroupTransportBuilder.GROUP2_ID);
		super.setValidfrom(LocalDate.parse("2800-01-01"));
		super.setValidtil(LocalDate.parse("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser2_2000_01_01() {
		super.setId(UserTransportBuilder.USER2_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(LocalDate.parse("2000-01-01"));
		super.setValidtil(LocalDate.parse("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser3_2000_01_01() {
		super.setId(UserTransportBuilder.USER3_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(LocalDate.parse("2000-01-01"));
		super.setValidtil(LocalDate.parse("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forImportUser_2000_01_01() {
		super.setId(UserTransportBuilder.IMPORTUSER_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(LocalDate.parse("2000-01-01"));
		super.setValidtil(LocalDate.parse("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forNewUser_2000_01_01() {
		super.setId(UserTransportBuilder.NON_EXISTING_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(LocalDate.parse("2000-01-01"));
		return this;
	}

	public AccessRelationTransport build() {
		final AccessRelationTransport transport = new AccessRelationTransport();
		transport.setId(super.getId());
		transport.setRefId(super.getRefId());
		transport.setValidfrom(super.getValidfrom());
		transport.setValidtil(super.getValidtil());
		return transport;
	}
}

package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;

public class AccessRelationTransportBuilder extends AccessRelationTransport {

	public AccessRelationTransportBuilder forUser1_2000_01_01() {
		super.setId(3l);
		super.setRefId(5l);
		super.setValidfrom(DateUtil.getGMTDate("2000-01-01"));
		super.setValidtil(DateUtil.getGMTDate("2500-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2600_01_01() {
		super.setId(3l);
		super.setRefId(6l);
		super.setValidfrom(DateUtil.getGMTDate("2600-01-01"));
		super.setValidtil(DateUtil.getGMTDate("2600-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2700_01_01() {
		super.setId(3l);
		super.setRefId(5l);
		super.setValidfrom(DateUtil.getGMTDate("2700-01-01"));
		super.setValidtil(DateUtil.getGMTDate("2700-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser1_2800_01_01() {
		super.setId(3l);
		super.setRefId(6l);
		super.setValidfrom(DateUtil.getGMTDate("2800-01-01"));
		super.setValidtil(DateUtil.getGMTDate("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forUser2_2000_01_01() {
		super.setId(4l);
		super.setRefId(5l);
		super.setValidfrom(DateUtil.getGMTDate("2000-01-01"));
		super.setValidtil(DateUtil.getGMTDate("2999-12-31"));
		return this;
	}

	public AccessRelationTransportBuilder forNewUser_2000_01_01() {
		super.setId(UserTransportBuilder.NEWUSER_ID);
		super.setRefId(GroupTransportBuilder.GROUP1_ID);
		super.setValidfrom(DateUtil.getGMTDate("2000-01-01"));
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

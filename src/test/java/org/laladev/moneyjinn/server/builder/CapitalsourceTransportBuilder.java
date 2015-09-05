package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

public class CapitalsourceTransportBuilder extends CapitalsourceTransport {

	public static final String CAPITALSOURCE1_COMMENT = "Source1";
	public static final String CAPITALSOURCE2_COMMENT = "Source2";
	public static final String CAPITALSOURCE3_COMMENT = "Source3";
	public static final String CAPITALSOURCE4_COMMENT = "Source4";
	public static final String NEWCAPITALSOURCE_COMMENT = "SourceNew";

	public static final Long CAPITALSOURCE1_ID = 1l;
	public static final Long CAPITALSOURCE2_ID = 2l;
	public static final Long CAPITALSOURCE3_ID = 3l;
	public static final Long CAPITALSOURCE4_ID = 4l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 5l;

	public CapitalsourceTransportBuilder forCapitalsource1() {
		super.setId(CAPITALSOURCE1_ID);
		super.setUserid(UserTransportBuilder.USER2_ID);
		super.setType((short) 1);
		super.setState((short) 1);
		super.setComment(CAPITALSOURCE1_COMMENT);
		super.setAccountNumber("1234567");
		super.setBankCode("765432");
		super.setValidFrom(DateUtil.getGMTDate("1980-01-01"));
		super.setValidTil(DateUtil.getGMTDate("2999-12-31"));
		super.setGroupUse((short) 0);
		super.setImportAllowed((short) 1);
		return this;
	}

	public CapitalsourceTransportBuilder forCapitalsource2() {
		super.setId(CAPITALSOURCE2_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setType((short) 2);
		super.setState((short) 2);
		super.setComment(CAPITALSOURCE2_COMMENT);
		super.setAccountNumber("1234567");
		super.setBankCode("ABCDEFG");
		super.setValidFrom(DateUtil.getGMTDate("1981-01-01"));
		super.setValidTil(DateUtil.getGMTDate("2799-12-31"));
		super.setGroupUse((short) 1);
		super.setImportAllowed((short) 0);
		return this;
	}

	public CapitalsourceTransportBuilder forCapitalsource3() {
		super.setId(CAPITALSOURCE3_ID);
		super.setUserid(UserTransportBuilder.USER2_ID);
		super.setType((short) 1);
		super.setState((short) 3);
		super.setComment(CAPITALSOURCE3_COMMENT);
		super.setAccountNumber("ZUTVEGT");
		super.setBankCode("765432");
		super.setValidFrom(DateUtil.getGMTDate("1982-01-01"));
		super.setValidTil(DateUtil.getGMTDate("2000-12-31"));
		super.setGroupUse((short) 1);
		super.setImportAllowed((short) 1);
		return this;
	}

	public CapitalsourceTransportBuilder forCapitalsource4() {
		super.setId(CAPITALSOURCE4_ID);
		super.setUserid(UserTransportBuilder.USER2_ID);
		super.setType((short) 1);
		super.setState((short) 4);
		super.setComment(CAPITALSOURCE4_COMMENT);
		super.setAccountNumber("ZUTVEGT");
		super.setBankCode("765432");
		super.setValidFrom(DateUtil.getGMTDate("1983-01-01"));
		super.setValidTil(DateUtil.getGMTDate("2010-12-31"));
		super.setGroupUse((short) 1);
		super.setImportAllowed((short) 1);
		return this;
	}

	public CapitalsourceTransportBuilder forNewCapitalsource() {
		super.setId(NON_EXISTING_ID);
		super.setUserid(UserTransportBuilder.USER2_ID);
		super.setType((short) 1);
		super.setState((short) 3);
		super.setComment(NEWCAPITALSOURCE_COMMENT);
		super.setAccountNumber("1234567");
		super.setBankCode("765432");
		super.setValidFrom(DateUtil.getGMTDate("1980-01-01"));
		super.setValidTil(DateUtil.getGMTDate("2999-12-31"));
		super.setGroupUse((short) 0);
		super.setImportAllowed((short) 1);
		return this;
	}

	public CapitalsourceTransport build() {
		final CapitalsourceTransport transport = new CapitalsourceTransport();

		transport.setId(super.getId());
		transport.setComment(super.getComment());
		transport.setAccountNumber(super.getAccountNumber());
		transport.setBankCode(super.getBankCode());
		transport.setValidFrom(super.getValidFrom());
		transport.setValidTil(super.getValidTil());
		transport.setGroupUse(super.getGroupUse());
		transport.setImportAllowed(super.getImportAllowed());

		return transport;
	}
}

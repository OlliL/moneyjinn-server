
package org.laladev.moneyjinn.server.builder;

import java.time.LocalDate;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

public class CapitalsourceTransportBuilder extends CapitalsourceTransport {
  public static final String CAPITALSOURCE1_ACCOUNTNUMBER = "1234567";
  public static final String CAPITALSOURCE1_BANKCODE = "765432";
  public static final String CAPITALSOURCE2_ACCOUNTNUMBER = "1234567";
  public static final String CAPITALSOURCE2_BANKCODE = "ABCDEFG";
  public static final String CAPITALSOURCE3_ACCOUNTNUMBER = "ZUTVEGT1";
  public static final String CAPITALSOURCE3_BANKCODE = "765432";
  public static final String CAPITALSOURCE4_ACCOUNTNUMBER = "ZUTVEGT";
  public static final String CAPITALSOURCE4_BANKCODE = "765432";
  public static final String CAPITALSOURCE5_ACCOUNTNUMBER = "ZRTVEGT";
  public static final String CAPITALSOURCE5_BANKCODE = "765433";
  public static final String CAPITALSOURCE6_ACCOUNTNUMBER = "ZRTVEGT3";
  public static final String CAPITALSOURCE6_BANKCODE = "765433";
  public static final String CAPITALSOURCE1_COMMENT = "Aource1";
  public static final String CAPITALSOURCE2_COMMENT = "Source2";
  public static final String CAPITALSOURCE3_COMMENT = "Source3";
  public static final String CAPITALSOURCE4_COMMENT = "Xource4";
  public static final String CAPITALSOURCE5_COMMENT = "Xource5";
  public static final String CAPITALSOURCE6_COMMENT = "Xource6";
  public static final String NEWCAPITALSOURCE_COMMENT = "SourceNew";
  public static final Long CAPITALSOURCE1_ID = 1l;
  public static final Long CAPITALSOURCE2_ID = 2l;
  public static final Long CAPITALSOURCE3_ID = 3l;
  public static final Long CAPITALSOURCE4_ID = 4l;
  public static final Long CAPITALSOURCE5_ID = 5l;
  public static final Long CAPITALSOURCE6_ID = 6l;
  public static final Long NON_EXISTING_ID = 666l;
  public static final Long NEXT_ID = 7l;
  public static final Short CAPITALSOURCE1_GROUP_USE = null;
  public static final Short CAPITALSOURCE2_GROUP_USE = (short) 1;
  public static final Short CAPITALSOURCE3_GROUP_USE = (short) 1;
  public static final Short CAPITALSOURCE4_GROUP_USE = (short) 1;
  public static final Short CAPITALSOURCE5_GROUP_USE = (short) 1;
  public static final Short CAPITALSOURCE6_GROUP_USE = null;
  public static final Short CAPITALSOURCE1_TYPE = (short) 1;
  public static final Short CAPITALSOURCE2_TYPE = (short) 2;
  public static final Short CAPITALSOURCE3_TYPE = (short) 3;
  public static final Short CAPITALSOURCE4_TYPE = (short) 4;
  public static final Short CAPITALSOURCE5_TYPE = (short) 5;
  public static final Short CAPITALSOURCE6_TYPE = (short) 1;
  public static final Short CAPITALSOURCE1_STATE = (short) 1;
  public static final Short CAPITALSOURCE2_STATE = (short) 2;
  public static final Short CAPITALSOURCE3_STATE = (short) 1;
  public static final Short CAPITALSOURCE4_STATE = (short) 1;
  public static final Short CAPITALSOURCE5_STATE = (short) 1;
  public static final Short CAPITALSOURCE6_STATE = (short) 1;

  public CapitalsourceTransportBuilder withUserId(final Long userId) {
    super.setUserid(userId);
    return this;
  }

  public CapitalsourceTransportBuilder withId(final Long id) {
    super.setId(id);
    return this;
  }

  public CapitalsourceTransportBuilder forCapitalsource1() {
    super.setId(CAPITALSOURCE1_ID);
    super.setUserid(UserTransportBuilder.USER1_ID);
    super.setType(CAPITALSOURCE1_TYPE);
    super.setState(CAPITALSOURCE1_STATE);
    super.setComment(CAPITALSOURCE1_COMMENT);
    super.setAccountNumber(CAPITALSOURCE1_ACCOUNTNUMBER);
    super.setBankCode(CAPITALSOURCE1_BANKCODE);
    super.setValidFrom(LocalDate.parse("1980-01-01"));
    super.setValidTil(LocalDate.parse("2999-12-31"));
    super.setGroupUse(CAPITALSOURCE1_GROUP_USE);
    super.setImportAllowed((short) 1);
    return this;
  }

  public CapitalsourceTransportBuilder forCapitalsource2() {
    super.setId(CAPITALSOURCE2_ID);
    super.setUserid(UserTransportBuilder.USER1_ID);
    super.setType(CAPITALSOURCE2_TYPE);
    super.setState(CAPITALSOURCE2_STATE);
    super.setComment(CAPITALSOURCE2_COMMENT);
    super.setAccountNumber(CAPITALSOURCE2_ACCOUNTNUMBER);
    super.setBankCode(CAPITALSOURCE2_BANKCODE);
    super.setValidFrom(LocalDate.parse("1981-01-01"));
    super.setValidTil(LocalDate.parse("2799-12-31"));
    super.setGroupUse(CAPITALSOURCE2_GROUP_USE);
    super.setImportAllowed(null);
    return this;
  }

  public CapitalsourceTransportBuilder forCapitalsource3() {
    super.setId(CAPITALSOURCE3_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setType(CAPITALSOURCE3_TYPE);
    super.setState(CAPITALSOURCE3_STATE);
    super.setComment(CAPITALSOURCE3_COMMENT);
    super.setAccountNumber(CAPITALSOURCE3_ACCOUNTNUMBER);
    super.setBankCode(CAPITALSOURCE3_BANKCODE);
    super.setValidFrom(LocalDate.parse("1982-01-01"));
    super.setValidTil(LocalDate.parse("2000-12-31"));
    super.setGroupUse(CAPITALSOURCE3_GROUP_USE);
    super.setImportAllowed((short) 2);
    return this;
  }

  public CapitalsourceTransportBuilder forCapitalsource4() {
    super.setId(CAPITALSOURCE4_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setType(CAPITALSOURCE4_TYPE);
    super.setState(CAPITALSOURCE4_STATE);
    super.setComment(CAPITALSOURCE4_COMMENT);
    super.setAccountNumber(CAPITALSOURCE4_ACCOUNTNUMBER);
    super.setBankCode(CAPITALSOURCE4_BANKCODE);
    super.setValidFrom(LocalDate.parse("2000-01-02"));
    super.setValidTil(LocalDate.parse("2010-12-31"));
    super.setGroupUse(CAPITALSOURCE4_GROUP_USE);
    super.setImportAllowed((short) 1);
    return this;
  }

  public CapitalsourceTransportBuilder forCapitalsource5() {
    super.setId(CAPITALSOURCE5_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setType(CAPITALSOURCE5_TYPE);
    super.setState(CAPITALSOURCE5_STATE);
    super.setComment(CAPITALSOURCE5_COMMENT);
    super.setAccountNumber(CAPITALSOURCE5_ACCOUNTNUMBER);
    super.setBankCode(CAPITALSOURCE5_BANKCODE);
    super.setValidFrom(LocalDate.parse("2014-01-01"));
    super.setValidTil(LocalDate.parse("2799-12-31"));
    super.setGroupUse(CAPITALSOURCE5_GROUP_USE);
    super.setImportAllowed((short) 2);
    return this;
  }

  public CapitalsourceTransportBuilder forCapitalsource6() {
    super.setId(CAPITALSOURCE6_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setType(CAPITALSOURCE6_TYPE);
    super.setState(CAPITALSOURCE6_STATE);
    super.setComment(CAPITALSOURCE6_COMMENT);
    super.setAccountNumber(CAPITALSOURCE6_ACCOUNTNUMBER);
    super.setBankCode(CAPITALSOURCE6_BANKCODE);
    super.setValidFrom(LocalDate.parse("2000-01-01"));
    super.setValidTil(LocalDate.parse("2799-12-31"));
    super.setGroupUse(CAPITALSOURCE6_GROUP_USE);
    super.setImportAllowed((short) 2);
    return this;
  }

  public CapitalsourceTransportBuilder forNewCapitalsource() {
    super.setId(NON_EXISTING_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setType((short) 3);
    super.setState((short) 1);
    super.setComment(NEWCAPITALSOURCE_COMMENT);
    super.setAccountNumber("1234567");
    super.setBankCode(CAPITALSOURCE4_BANKCODE);
    super.setValidFrom(LocalDate.parse("1980-01-01"));
    super.setValidTil(LocalDate.parse("2999-12-31"));
    super.setGroupUse(null);
    super.setImportAllowed((short) 1);
    return this;
  }

  public CapitalsourceTransport build() {
    final CapitalsourceTransport transport = new CapitalsourceTransport();
    transport.setId(super.getId());
    transport.setUserid(super.getUserid());
    transport.setType(super.getType());
    transport.setState(super.getState());
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

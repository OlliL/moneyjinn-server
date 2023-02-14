
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.laladev.moneyjinn.server.model.PreDefMoneyflowTransport;

public class PreDefMoneyflowTransportBuilder extends PreDefMoneyflowTransport {
  private static final Integer SHORT_1 = Integer.valueOf("1");
  public static final String PRE_DEF_MONEYFLOW1_COMMENT = "Pre1";
  public static final String PRE_DEF_MONEYFLOW2_COMMENT = "Qre2";
  public static final String PRE_DEF_MONEYFLOW3_COMMENT = "Rre3";
  public static final String NEWPRE_DEF_MONEYFLOW_COMMENT = "PreNew";
  public static final Long PRE_DEF_MONEYFLOW1_ID = 1l;
  public static final Long PRE_DEF_MONEYFLOW2_ID = 2l;
  public static final Long PRE_DEF_MONEYFLOW3_ID = 3l;
  public static final Long NON_EXISTING_ID = 666l;
  public static final Long NEXT_ID = 4l;

  public PreDefMoneyflowTransportBuilder forPreDefMoneyflow1() {
    super.setId(PRE_DEF_MONEYFLOW1_ID);
    super.setUserid(UserTransportBuilder.USER1_ID);
    super.setAmount(new BigDecimal("10.10"));
    super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
    super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    super.setComment(PRE_DEF_MONEYFLOW1_COMMENT);
    super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
    super.setCreatedate(LocalDate.parse("2000-10-10"));
    super.setLastUsed(null);
    super.setOnceAMonth(SHORT_1);
    super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
    super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
    return this;
  }

  public PreDefMoneyflowTransportBuilder forPreDefMoneyflow2() {
    super.setId(PRE_DEF_MONEYFLOW2_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setAmount(new BigDecimal("11.00"));
    super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
    super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
    super.setComment(PRE_DEF_MONEYFLOW2_COMMENT);
    super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
    super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER3_NAME);
    super.setCreatedate(LocalDate.parse("2000-10-10"));
    super.setLastUsed(null);
    super.setOnceAMonth(SHORT_1);
    super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
    super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
    return this;
  }

  public PreDefMoneyflowTransportBuilder forPreDefMoneyflow3() {
    super.setId(PRE_DEF_MONEYFLOW3_ID);
    super.setUserid(UserTransportBuilder.USER1_ID);
    super.setAmount(new BigDecimal("-10.00"));
    super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
    super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    super.setComment(PRE_DEF_MONEYFLOW3_COMMENT);
    super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
    super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME);
    super.setCreatedate(LocalDate.parse("2000-10-10"));
    super.setLastUsed(null);
    super.setOnceAMonth(null);
    super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
    super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
    return this;
  }

  public PreDefMoneyflowTransportBuilder forNewPreDefMoneyflow() {
    super.setId(NON_EXISTING_ID);
    super.setUserid(UserTransportBuilder.USER3_ID);
    super.setAmount(new BigDecimal("10.10"));
    super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
    super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    super.setComment(NEWPRE_DEF_MONEYFLOW_COMMENT);
    super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
    super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME);
    super.setCreatedate(LocalDate.parse("2000-10-10"));
    super.setLastUsed(null);
    super.setOnceAMonth(null);
    super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
    super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
    return this;
  }

  public PreDefMoneyflowTransport build() {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransport();
    transport.setId(super.getId());
    transport.setUserid(super.getUserid());
    transport.setComment(super.getComment());
    transport.setAmount(super.getAmount());
    transport.setCapitalsourcecomment(super.getCapitalsourcecomment());
    transport.setCapitalsourceid(super.getCapitalsourceid());
    transport.setContractpartnerid(super.getContractpartnerid());
    transport.setContractpartnername(super.getContractpartnername());
    transport.setCreatedate(super.getCreatedate());
    transport.setLastUsed(super.getLastUsed());
    transport.setOnceAMonth(super.getOnceAMonth());
    transport.setPostingaccountid(super.getPostingaccountid());
    transport.setPostingaccountname(super.getPostingaccountname());
    return transport;
  }
}

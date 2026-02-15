package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.ContractpartnerMatchingTransport;

public class ContractpartnerMatchingTransportBuilder extends ContractpartnerMatchingTransport {
    public static final String CONTRACTPARTNER_MATCHING1_MATCHING_TEXT = "TEST1";
    public static final String CONTRACTPARTNER_MATCHING2_MATCHING_TEXT = "TEST2";
    public static final String CONTRACTPARTNER_MATCHING3_MATCHING_TEXT = "TEST3";
    public static final Long CONTRACTPARTNER_MATCHING1_ID = 1L;
    public static final Long CONTRACTPARTNER_MATCHING2_ID = 2L;
    public static final Long CONTRACTPARTNER_MATCHING3_ID = 3L;
    public static final Long NON_EXISTING_ID = 666L;
    public static final Long NEXT_ID = 4L;

    public ContractpartnerMatchingTransportBuilder forContractpartnerMatching1() {
        super.setId(CONTRACTPARTNER_MATCHING1_ID);
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
        super.setMatchingText(CONTRACTPARTNER_MATCHING1_MATCHING_TEXT);
        return this;
    }

    public ContractpartnerMatchingTransportBuilder forContractpartnerMatching2() {
        super.setId(CONTRACTPARTNER_MATCHING2_ID);
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
        super.setMatchingText(CONTRACTPARTNER_MATCHING2_MATCHING_TEXT);
        return this;
    }

    public ContractpartnerMatchingTransportBuilder forContractpartnerMatching3() {
        super.setId(CONTRACTPARTNER_MATCHING3_ID);
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER4_NAME);
        super.setMatchingText(CONTRACTPARTNER_MATCHING3_MATCHING_TEXT);
        super.setMoneyflowComment("mmf-comment");
        super.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID);
        super.setPostingAccountName(PostingAccountTransportBuilder.POSTING_ACCOUNT3_NAME);
        return this;
    }

    public ContractpartnerMatchingTransportBuilder forNewContractpartnerMatching() {
        super.setId(NON_EXISTING_ID);
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        super.setMatchingText("ABC789");
        return this;
    }

    public ContractpartnerMatchingTransportBuilder withContractpartnerid(final Long contractpartnerId) {
        super.setContractpartnerid(contractpartnerId);
        return this;
    }

    public ContractpartnerMatchingTransportBuilder withMatchingText(final String matchingText) {
        super.setMatchingText(matchingText);
        return this;
    }

    public ContractpartnerMatchingTransport build() {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransport();
        transport.setId(super.getId());
        transport.setContractpartnerid(super.getContractpartnerid());
        transport.setContractpartnername(super.getContractpartnername());
        transport.setMatchingText(super.getMatchingText());
        transport.setMoneyflowComment(super.getMoneyflowComment());
        transport.setPostingAccountId(super.getPostingAccountId());
        transport.setPostingAccountName(super.getPostingAccountName());
        return transport;
    }
}

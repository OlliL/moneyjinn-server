package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.PostingAccountTransport;

public class PostingAccountTransportBuilder extends PostingAccountTransport {
    public static final String POSTING_ACCOUNT1_NAME = "postingaccount1";
    public static final String POSTING_ACCOUNT2_NAME = "postingaccount2";
    public static final String POSTING_ACCOUNT3_NAME = "xostingaccount3";
    public static final String NEWPOSTING_ACCOUNT_NAME = "postingAccountNew";
    public static final Long POSTING_ACCOUNT1_ID = 1L;
    public static final Long POSTING_ACCOUNT2_ID = 2L;
    public static final Long POSTING_ACCOUNT3_ID = 3L;
    public static final Long NON_EXISTING_ID = 666L;
    public static final Long NEXT_ID = 4L;

    public PostingAccountTransportBuilder forPostingAccount1() {
        super.setId(POSTING_ACCOUNT1_ID);
        super.setName(POSTING_ACCOUNT1_NAME);
        return this;
    }

    public PostingAccountTransportBuilder forPostingAccount2() {
        super.setId(POSTING_ACCOUNT2_ID);
        super.setName(POSTING_ACCOUNT2_NAME);
        return this;
    }

    public PostingAccountTransportBuilder forPostingAccount3() {
        super.setId(POSTING_ACCOUNT3_ID);
        super.setName(POSTING_ACCOUNT3_NAME);
        return this;
    }

    public PostingAccountTransportBuilder forNewPostingAccount() {
        super.setId(NON_EXISTING_ID);
        super.setName(NEWPOSTING_ACCOUNT_NAME);
        return this;
    }

    public PostingAccountTransport build() {
        final PostingAccountTransport transport = new PostingAccountTransport();
        transport.setId(super.getId());
        transport.setName(super.getName());
        return transport;
    }
}

package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.MoneyflowSplitEntryTransport;

import java.math.BigDecimal;

public class MoneyflowSplitEntryTransportBuilder extends MoneyflowSplitEntryTransport {
    public static final String MONEYFLOW_SPLIT_ENTRY2_COMMENT = "split2";
    public static final String MONEYFLOW_SPLIT_ENTRY1_COMMENT = "split1";
    public static final BigDecimal MONEYFLOW_SPLIT_ENTRY1_AMOUNT = new BigDecimal("-1.00");
    public static final BigDecimal MONEYFLOW_SPLIT_ENTRY2_AMOUNT = new BigDecimal("-0.10");
    public static final Long MONEYFLOW1_ID = MoneyflowTransportBuilder.MONEYFLOW1_ID;
    public static final Long MONEYFLOW2_ID = MoneyflowTransportBuilder.MONEYFLOW2_ID;
    public static final BigDecimal MONEYFLOW2_AMOUNT = MoneyflowTransportBuilder.MONEYFLOW2_AMOUNT;
    public static final Long MONEYFLOW_SPLIT_ENTRY1_ID = 1L;
    public static final Long MONEYFLOW_SPLIT_ENTRY2_ID = 2L;
    public static final Long NEXT_ID = 3L;

    public MoneyflowSplitEntryTransportBuilder forMoneyflowSplitEntry1() {
        super.setId(MONEYFLOW_SPLIT_ENTRY1_ID);
        super.setMoneyflowid(MONEYFLOW1_ID);
        super.setAmount(MONEYFLOW_SPLIT_ENTRY1_AMOUNT);
        super.setComment(MONEYFLOW_SPLIT_ENTRY1_COMMENT);
        super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
        super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
        return this;
    }

    public MoneyflowSplitEntryTransportBuilder forMoneyflowSplitEntry2() {
        super.setId(MONEYFLOW_SPLIT_ENTRY2_ID);
        super.setMoneyflowid(MONEYFLOW1_ID);
        super.setAmount(MONEYFLOW_SPLIT_ENTRY2_AMOUNT);
        super.setComment(MONEYFLOW_SPLIT_ENTRY2_COMMENT);
        super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
        super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
        return this;
    }

    public MoneyflowSplitEntryTransportBuilder forNewMoneyflowSplitEntry() {
        super.setId(NEXT_ID);
        super.setMoneyflowid(MONEYFLOW2_ID);
        super.setAmount(MONEYFLOW2_AMOUNT);
        super.setComment("split");
        super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
        super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
        return this;
    }

    public MoneyflowSplitEntryTransportBuilder withComment(final String comment) {
        super.setComment(comment);
        return this;
    }

    public MoneyflowSplitEntryTransportBuilder withAmount(final BigDecimal amount) {
        super.setAmount(amount);
        return this;
    }

    public MoneyflowSplitEntryTransportBuilder withPostingaccountid(final Long postingaccountid) {
        super.setPostingaccountid(postingaccountid);
        return this;
    }

    public MoneyflowSplitEntryTransport build() {
        final MoneyflowSplitEntryTransport transport = new MoneyflowSplitEntryTransport();
        transport.setId(super.getId());
        transport.setMoneyflowid(super.getMoneyflowid());
        transport.setComment(super.getComment());
        transport.setAmount(super.getAmount());
        transport.setPostingaccountid(super.getPostingaccountid());
        transport.setPostingaccountname(super.getPostingaccountname());
        return transport;
    }
}

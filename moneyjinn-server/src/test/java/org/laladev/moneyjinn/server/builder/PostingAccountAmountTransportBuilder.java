
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import org.laladev.moneyjinn.core.rest.model.report.transport.PostingAccountAmountTransport;

public class PostingAccountAmountTransportBuilder extends PostingAccountAmountTransport {
  public PostingAccountAmountTransportBuilder withAmount(final String amount) {
    super.setAmount(new BigDecimal(amount));
    return this;
  }

  public PostingAccountAmountTransportBuilder withDate(final String date) {
    super.setDate(DateUtil.getGmtDate(date));
    return this;
  }

  public PostingAccountAmountTransportBuilder forPostingAccount1() {
    super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
    super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
    return this;
  }

  public PostingAccountAmountTransportBuilder forPostingAccount2() {
    super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
    super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
    return this;
  }

  public PostingAccountAmountTransport build() {
    final PostingAccountAmountTransport transport = new PostingAccountAmountTransport();
    transport.setPostingaccountid(super.getPostingaccountid());
    transport.setPostingaccountname(super.getPostingaccountname());
    transport.setAmount(super.getAmount());
    transport.setDate(super.getDate());
    return transport;
  }
}

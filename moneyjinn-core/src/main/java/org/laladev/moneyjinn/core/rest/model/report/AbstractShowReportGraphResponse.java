
package org.laladev.moneyjinn.core.rest.model.report;

import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.report.transport.PostingAccountAmountTransport;

public class AbstractShowReportGraphResponse extends AbstractResponse {
  @XmlElement(name = "postingAccountAmountTransport")
  private List<PostingAccountAmountTransport> postingAccountAmountTransports;

  public List<PostingAccountAmountTransport> getPostingAccountAmountTransports() {
    return this.postingAccountAmountTransports;
  }

  public void setPostingAccountAmountTransports(
      final List<PostingAccountAmountTransport> postingAccountAmountTransports) {
    this.postingAccountAmountTransports = postingAccountAmountTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.postingAccountAmountTransports);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final AbstractShowReportGraphResponse other = (AbstractShowReportGraphResponse) obj;
    return Objects.equals(this.postingAccountAmountTransports,
        other.postingAccountAmountTransports);
  }

  @Override
  public String toString() {
    return "AbstractShowReportGraphResponse [postingAccountAmountTransports="
        + this.postingAccountAmountTransports + "]";
  }

}
package org.laladev.moneyjinn.core.rest.model.report;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountAmountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AbstractShowReportGraphResponse extends AbstractResponse {
	@JsonProperty("postingAccountAmountTransport")
	private List<PostingAccountAmountTransport> postingAccountAmountTransports;
	@JsonProperty("postingAccountTransport")
	private List<PostingAccountTransport> postingAccountTransports;

	public final List<PostingAccountAmountTransport> getPostingAccountAmountTransports() {
		return this.postingAccountAmountTransports;
	}

	public final void setPostingAccountAmountTransports(
			final List<PostingAccountAmountTransport> postingAccountAmountTransports) {
		this.postingAccountAmountTransports = postingAccountAmountTransports;
	}

	public final List<PostingAccountTransport> getPostingAccountTransports() {
		return this.postingAccountTransports;
	}

	public final void setPostingAccountTransports(final List<PostingAccountTransport> postingAccountTransports) {
		this.postingAccountTransports = postingAccountTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.postingAccountAmountTransports == null) ? 0 : this.postingAccountAmountTransports.hashCode());
		result = prime * result
				+ ((this.postingAccountTransports == null) ? 0 : this.postingAccountTransports.hashCode());
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
		if (this.postingAccountAmountTransports == null) {
			if (other.postingAccountAmountTransports != null) {
				return false;
			}
		} else if (!this.postingAccountAmountTransports.equals(other.postingAccountAmountTransports)) {
			return false;
		}
		if (this.postingAccountTransports == null) {
			if (other.postingAccountTransports != null) {
				return false;
			}
		} else if (!this.postingAccountTransports.equals(other.postingAccountTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractShowReportGraphResponse [postingAccountAmountTransports=");
		builder.append(this.postingAccountAmountTransports);
		builder.append(", postingAccountTransports=");
		builder.append(this.postingAccountTransports);
		builder.append("]");
		return builder.toString();
	}

}
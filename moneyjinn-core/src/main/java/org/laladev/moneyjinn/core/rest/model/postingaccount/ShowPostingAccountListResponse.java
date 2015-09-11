package org.laladev.moneyjinn.core.rest.model.postingaccount;

import java.util.List;
import java.util.Set;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showPostingAccountListResponse")
public class ShowPostingAccountListResponse extends AbstractResponse {
	private Set<Character> initials;
	@JsonProperty("postingAccountTransport")
	private List<PostingAccountTransport> postingAccountTransports;

	public final Set<Character> getInitials() {
		return this.initials;
	}

	public final void setInitials(final Set<Character> initials) {
		this.initials = initials;
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
				+ ((this.postingAccountTransports == null) ? 0 : this.postingAccountTransports.hashCode());
		result = prime * result + ((this.initials == null) ? 0 : this.initials.hashCode());
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
		final ShowPostingAccountListResponse other = (ShowPostingAccountListResponse) obj;
		if (this.postingAccountTransports == null) {
			if (other.postingAccountTransports != null) {
				return false;
			}
		} else if (!this.postingAccountTransports.equals(other.postingAccountTransports)) {
			return false;
		}
		if (this.initials == null) {
			if (other.initials != null) {
				return false;
			}
		} else if (!this.initials.equals(other.initials)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowPostingAccountListResponse [initials=");
		builder.append(this.initials);
		builder.append(", postingAccountTransports=");
		builder.append(this.postingAccountTransports);
		builder.append("]");
		return builder.toString();
	}
}

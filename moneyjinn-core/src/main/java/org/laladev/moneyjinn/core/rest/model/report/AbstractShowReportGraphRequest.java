package org.laladev.moneyjinn.core.rest.model.report;

import java.sql.Date;
import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;

public class AbstractShowReportGraphRequest extends AbstractRequest {
	private List<Long> postingAccountIdsYes;
	private List<Long> postingAccountIdsNo;
	private Date startDate;
	private Date endDate;

	public final List<Long> getPostingAccountIdsYes() {
		return this.postingAccountIdsYes;
	}

	public final void setPostingAccountIdsYes(final List<Long> postingAccountIdsYes) {
		this.postingAccountIdsYes = postingAccountIdsYes;
	}

	public final List<Long> getPostingAccountIdsNo() {
		return this.postingAccountIdsNo;
	}

	public final void setPostingAccountIdsNo(final List<Long> postingAccountIdsNo) {
		this.postingAccountIdsNo = postingAccountIdsNo;
	}

	public final Date getStartDate() {
		return this.startDate;
	}

	public final void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public final Date getEndDate() {
		return this.endDate;
	}

	public final void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
		result = prime * result + ((this.postingAccountIdsNo == null) ? 0 : this.postingAccountIdsNo.hashCode());
		result = prime * result + ((this.postingAccountIdsYes == null) ? 0 : this.postingAccountIdsYes.hashCode());
		result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final AbstractShowReportGraphRequest other = (AbstractShowReportGraphRequest) obj;
		if (this.endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!this.endDate.equals(other.endDate)) {
			return false;
		}
		if (this.postingAccountIdsNo == null) {
			if (other.postingAccountIdsNo != null) {
				return false;
			}
		} else if (!this.postingAccountIdsNo.equals(other.postingAccountIdsNo)) {
			return false;
		}
		if (this.postingAccountIdsYes == null) {
			if (other.postingAccountIdsYes != null) {
				return false;
			}
		} else if (!this.postingAccountIdsYes.equals(other.postingAccountIdsYes)) {
			return false;
		}
		if (this.startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!this.startDate.equals(other.startDate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractShowReportGraphRequest [postingAccountIdsYes=");
		builder.append(this.postingAccountIdsYes);
		builder.append(", postingAccountIdsNo=");
		builder.append(this.postingAccountIdsNo);
		builder.append(", startDate=");
		builder.append(this.startDate);
		builder.append(", endDate=");
		builder.append(this.endDate);
		builder.append("]");
		return builder.toString();
	}

}

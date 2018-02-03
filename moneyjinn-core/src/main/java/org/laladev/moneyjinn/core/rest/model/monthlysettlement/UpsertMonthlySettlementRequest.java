package org.laladev.moneyjinn.core.rest.model.monthlysettlement;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "upsertMonthlySettlementRequest")
public class UpsertMonthlySettlementRequest extends AbstractRequest {
	@XmlElement(name = "monthlySettlementTransport")
	private List<MonthlySettlementTransport> monthlySettlementTransports;

	public final List<MonthlySettlementTransport> getMonthlySettlementTransports() {
		return this.monthlySettlementTransports;
	}

	public final void setMonthlySettlementTransports(
			final List<MonthlySettlementTransport> monthlySettlementTransports) {
		this.monthlySettlementTransports = monthlySettlementTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.monthlySettlementTransports == null) ? 0 : this.monthlySettlementTransports.hashCode());
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
		final UpsertMonthlySettlementRequest other = (UpsertMonthlySettlementRequest) obj;
		if (this.monthlySettlementTransports == null) {
			if (other.monthlySettlementTransports != null) {
				return false;
			}
		} else if (!this.monthlySettlementTransports.equals(other.monthlySettlementTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UpsertMonthlySettlementRequest [monthlySettlementTransports=");
		builder.append(this.monthlySettlementTransports);
		builder.append("]");
		return builder.toString();
	}

}

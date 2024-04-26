package org.laladev.moneyjinn.model.etf;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class EtfFlowWithTaxInfo extends EtfFlow {
	private static final long serialVersionUID = 1L;
	private BigDecimal accumulatedPreliminaryLumpSum;

	public EtfFlowWithTaxInfo(final EtfFlow etfFlow) {
		super(etfFlow);
	}
}

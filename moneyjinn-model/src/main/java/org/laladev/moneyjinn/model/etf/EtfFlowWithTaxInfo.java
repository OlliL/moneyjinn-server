package org.laladev.moneyjinn.model.etf;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class EtfFlowWithTaxInfo extends EtfFlow {
    @Serial
    private static final long serialVersionUID = 1L;
    private BigDecimal accumulatedPreliminaryLumpSum;
    private final Map<Year, BigDecimal> preliminaryLumpSumPerYear = new HashMap<>();

    public EtfFlowWithTaxInfo(final EtfFlow etfFlow) {
        super(etfFlow);
    }
}

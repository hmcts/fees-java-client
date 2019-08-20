package uk.gov.hmcts.reform.fees.client.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FeeOutcome {
    private String code;
    private String description;
    private BigDecimal feeAmount;
    private String version;
}

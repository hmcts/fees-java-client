package uk.gov.hmcts.reform.fees.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeOutcome {
    private String code;
    private String description;
    private BigDecimal feeAmount;
    private String version;
}

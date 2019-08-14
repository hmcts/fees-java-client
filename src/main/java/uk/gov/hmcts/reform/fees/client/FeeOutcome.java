package uk.gov.hmcts.reform.fees.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeOutcome {
    private String code;
    private String description;
    @JsonProperty("fee_amount")
    private BigDecimal amount;
    private String version;
}

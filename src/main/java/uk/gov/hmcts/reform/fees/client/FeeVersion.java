package uk.gov.hmcts.reform.fees.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeVersion {
    private String description;
    private String status;
    private Integer version;
    private OffsetDateTime validFrom;
    private BigDecimal amount;
    private String memoLine;
    private String naturalAccountCode;
    private String direction;
}

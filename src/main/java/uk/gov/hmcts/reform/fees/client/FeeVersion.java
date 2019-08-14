package uk.gov.hmcts.reform.fees.client;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
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

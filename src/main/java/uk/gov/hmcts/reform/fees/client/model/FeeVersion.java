package uk.gov.hmcts.reform.fees.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeVersion {
    private String description;
    private String status;
    private Integer version;
    private OffsetDateTime validFrom;
    private FlatAmount flatAmount;
    private String memoLine;
    private String statutoryInstrument;
    private String naturalAccountCode;
    private String direction;
}

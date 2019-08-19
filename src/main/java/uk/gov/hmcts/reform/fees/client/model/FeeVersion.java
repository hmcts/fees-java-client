package uk.gov.hmcts.reform.fees.client.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
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

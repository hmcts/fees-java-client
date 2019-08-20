package uk.gov.hmcts.reform.fees.client.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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

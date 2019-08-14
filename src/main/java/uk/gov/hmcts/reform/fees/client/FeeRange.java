package uk.gov.hmcts.reform.fees.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import uk.gov.hmcts.reform.fees.client.deserialization.FeeRangeDeserializer;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonDeserialize(using = FeeRangeDeserializer.class)
public class FeeRange {
    private String code;
    private String feeType;
    private String channelType;
    private String eventType;
    private String jurisdiction1;
    private String jurisdiction2;
    private String serviceType;
    private String applicantType;
    private List<FeeVersion> feeVersions;
    private FeeVersion currentVersion;
    private BigDecimal minimum;
    private BigDecimal maximum;
    private String unit;
    private Boolean unspecifiedClaimAmount;
    private FeeVersion matchingVersion;
}

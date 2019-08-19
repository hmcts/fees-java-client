package uk.gov.hmcts.reform.fees.client.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FeeRange {
    private String code;
    private String feeType;
    private ChannelType channelType;
    private EventType eventType;
    private Jurisdiction1 jurisdiction1;
    private Jurisdiction2 jurisdiction2;
    private ServiceType serviceType;
    private ApplicantType applicantType;
    private List<FeeVersion> feeVersions;
    private FeeVersion currentVersion;
    private BigDecimal minRange;
    private BigDecimal maxRange;
    private String rangeUnit;
    private Boolean unspecifiedClaimAmount;
    private FeeVersion matchingVersion;
}

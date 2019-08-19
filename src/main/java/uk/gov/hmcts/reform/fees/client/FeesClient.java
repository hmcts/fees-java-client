package uk.gov.hmcts.reform.fees.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.fees.client.model.FeeOutcome;
import uk.gov.hmcts.reform.fees.client.model.FeeRange;

import java.math.BigDecimal;

@Service
public class FeesClient {
    private final FeesApi feesApi;
    private final String service;
    private final String jurisdiction1;
    private final String jurisdiction2;

    @Autowired
    public FeesClient(
            FeesApi feesApi,
            @Value("${fees.api.service:}") String service,
            @Value("${fees.api.jurisdiction1:}") String jurisdiction1,
            @Value("${fees.api.jurisdiction2:}") String jurisdiction2
    ) {
        this.feesApi = feesApi;
        this.service = service;
        this.jurisdiction1 = jurisdiction1;
        this.jurisdiction2 = jurisdiction2;
    }

    public FeeOutcome lookupFee(String channel, String event, BigDecimal amount) {
        return this.feesApi.lookupFee(service, jurisdiction1, jurisdiction2, channel, event, amount);
    }

    public FeeRange[] findRangeGroup(String channel, String event) {
        return this.feesApi.findRangeGroup(service, jurisdiction1, jurisdiction2, channel, event);
    }
}

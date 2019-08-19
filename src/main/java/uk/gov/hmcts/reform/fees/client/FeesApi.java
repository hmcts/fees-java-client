package uk.gov.hmcts.reform.fees.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.hmcts.reform.fees.client.model.FeeOutcome;
import uk.gov.hmcts.reform.fees.client.model.FeeRange;

import java.math.BigDecimal;

@FeignClient(name = "fees-api", url = "${fees.api.url}", configuration = CoreFeignConfiguration.class)
public interface FeesApi {
    @GetMapping("/fees-register/fees/lookup"
            + "?service={service}"
            + "&jurisdiction1={jurisdiction1}"
            + "&jurisdiction2={jurisdiction2}"
            + "&channel={channel}"
            + "&event={eventType}"
            + "&amount_or_volume={amount}"
    )
    FeeOutcome lookupFee(
            @PathVariable("service") String service,
            @PathVariable("jurisdiction1") String jurisdiction1,
            @PathVariable("jurisdiction2") String jurisdiction2,
            @PathVariable("channel") String channel,
            @PathVariable("eventType") String eventType,
            @PathVariable("amount") BigDecimal amount
    );

    @GetMapping("/fees-register/fees"
            + "?service={service}"
            + "&jurisdiction1={jurisdiction1}"
            + "&jurisdiction2={jurisdiction2}"
            + "&channel={channel}"
            + "&event={eventType}"
            + "&feeVersionStatus=approved"
    )
    FeeRange[] findRangeGroup(
            @PathVariable("service") String service,
            @PathVariable("jurisdiction1") String jurisdiction1,
            @PathVariable("jurisdiction2") String jurisdiction2,
            @PathVariable("channel") String channel,
            @PathVariable("eventType") String eventType
    );
}

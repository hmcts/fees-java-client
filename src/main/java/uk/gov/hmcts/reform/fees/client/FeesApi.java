package uk.gov.hmcts.reform.fees.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.fees.client.health.InternalHealth;
import uk.gov.hmcts.reform.fees.client.model.Fee2Dto;
import uk.gov.hmcts.reform.fees.client.model.FeeLookupResponseDto;

import java.math.BigDecimal;

@FeignClient(name = "fees-api", url = "${fees.api.url}")
public interface FeesApi {
    @GetMapping("/health")
    InternalHealth health();

    @GetMapping("/fees-register/fees/lookup")
    FeeLookupResponseDto lookupFee(
        @RequestParam(value = "service") String service,
        @RequestParam(value = "jurisdiction1") String jurisdiction1,
        @RequestParam(value = "jurisdiction2") String jurisdiction2,
        @RequestParam(value = "channel") String channel,
        @RequestParam(value = "event") String event,
        @RequestParam(value = "applicant_type", required = false) String applicantType,
        @RequestParam(value = "amount_or_volume", required = false) BigDecimal amountOrVolume,
        @RequestParam(value = "keyword", required = false) String keyword
    );

    @GetMapping("/fees-register/fees?feeVersionStatus=approved")
    Fee2Dto[] findRangeGroup(
        @RequestParam("service") String service,
        @RequestParam("jurisdiction1") String jurisdiction1,
        @RequestParam("jurisdiction2") String jurisdiction2,
        @RequestParam("channel") String channel,
        @RequestParam("event") String eventType);
}

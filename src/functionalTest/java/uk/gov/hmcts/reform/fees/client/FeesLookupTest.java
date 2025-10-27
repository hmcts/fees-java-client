package uk.gov.hmcts.reform.fees.client;

import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.fees.client.model.FeeLookupResponseDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableAutoConfiguration
@DisplayName("Fee lookup API")
class FeesLookupTest extends BaseTest {

    @Test
    @DisplayName("should retrieve the correct code for an amount")
    void testValidRequest() {
        FeeLookupResponseDto feeOutcome = feesApi.lookupFee(
                "civil",
                "civil",
                "county court",
                "online",
                "issue",
                null,
                BigDecimal.valueOf(100),
                null
        );
        assertEquals("FEE0211", feeOutcome.getCode());
    }

    @Test
    @DisplayName("should return a 400 Bad Request for an invalid request")
    void testInvalidRequest() {
        FeignException exception = assertThrows(
                FeignException.class,
                () -> feesApi.lookupFee(
                        "civil",
                        "civil",
                        "county court",
                        "invalid channel",
                        "invalid event",
                        null,
                        BigDecimal.valueOf(-999.99),
                        null
                )
        );
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.status());
    }
}

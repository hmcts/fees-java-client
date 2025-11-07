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
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoConfiguration
@DisplayName("Fee lookup API")
class FeesLookupTest extends BaseTest {

    @Test
    @DisplayName("should retrieve the correct code for an amount")
    void testValidProbateRequestWithKeyword() {
        FeeLookupResponseDto feeOutcome = feesApi.lookupFee(
                "probate",
                "family",
                "probate registry",
                "default",
                "issue",
                "all",
                BigDecimal.valueOf(238135.00),
                "SA"
        );
        assertEquals("FEE0219", feeOutcome.getCode());
    }

    @Test
    @DisplayName("should retrieve the correct code for an amount with keyword")
    void testValidCivilRequestWithKeyword() {
        FeeLookupResponseDto feeOutcome = feesApi.lookupFee(
                "civil money claims",
                "civil",
                "county court",
                "default",
                "issue",
                null,
                BigDecimal.valueOf(50000),
                "MoneyClaim"
        );
        assertEquals("FEE0209", feeOutcome.getCode());
    }

    @Test
    @DisplayName("should return 404 Not Found when fee is missing")
    void testCivilRequestWithMissingKeyword() {
        FeignException.NotFound exception = assertThrows(
                FeignException.NotFound.class,
                () -> feesApi.lookupFee(
                        "civil money claims",
                        "civil",
                        "county court",
                        "default",
                        "issue",
                        null,
                        BigDecimal.valueOf(50000),
                        null
                )
        );
        assertTrue(exception.getMessage().contains("fee for code=LookupFeeDto"));
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
                        "missingKeyword"
                )
        );
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.status());
    }
}

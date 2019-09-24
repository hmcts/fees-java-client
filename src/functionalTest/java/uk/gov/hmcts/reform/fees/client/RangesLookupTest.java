package uk.gov.hmcts.reform.fees.client;

import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.fees.client.model.Fee2Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoConfiguration
@DisplayName("Fee range lookup API")
class RangesLookupTest extends BaseTest {

    @Test
    @DisplayName("should retrieve a range of fees for a valid request")
    void testValidRequest() {
        Fee2Dto[] ranges = feesClient.findRangeGroup("default", "issue");
        assertNotNull(ranges);
        assertTrue(ranges.length > 0);
        for (Fee2Dto range : ranges) {
            assertEquals("default", range.getChannelType().getName());
            assertEquals("issue", range.getEventType().getName());
        }
    }

    @Test
    @DisplayName("should return a 400 Bad Request for an invalid request")
    void testInvalidRequest() {
        FeignException exception = assertThrows(
            FeignException.class,
            () -> feesClient.findRangeGroup("invalid channel", "invalid event")
        );

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.status());
    }
}

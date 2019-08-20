package uk.gov.hmcts.reform.fees.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.fees.client.model.FeeOutcome;
import uk.gov.hmcts.reform.fees.client.model.FeeRange;
import uk.gov.hmcts.reform.fees.client.model.FeeVersion;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = {FeesClient.class, FeesApi.class})
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@AutoConfigureWireMock(port = 8091)
class FeesClientTest {
    @Autowired
    private FeesClient feesClient;

    @Test
    void successfullyRetrieveFee() {
        FeeOutcome fee = feesClient.lookupFee("test channel", "test event", BigDecimal.valueOf(1000.0));
        assertAll(
            () -> assertThat(fee.getFeeAmount()).isEqualTo("60.00"),
            () -> assertThat(fee.getCode()).isEqualTo("FEE123"),
            () -> assertThat(fee.getDescription()).isEqualTo("Test service fees - Amount - 500.01 upto 1000 GBP"),
            () -> assertThat(fee.getVersion()).isEqualTo("3")
        );
    }

    @Test
    void successfullyFindRangeGroups() {
        FeeRange[] ranges = feesClient.findRangeGroup("test channel", "test event");
        assertAll(
            () -> assertThat(ranges).hasSize(2),

            () -> assertThat(ranges[0].getApplicantType().getName()).isEqualTo("all"),
            () -> assertThat(ranges[0].getChannelType().getName()).isEqualTo("test channel"),
            () -> assertThat(ranges[0].getCode()).isEqualTo("FEE0123"),
            () -> assertThat(ranges[0].getEventType().getName()).isEqualTo("test event"),
            () -> assertThat(ranges[0].getFeeType()).isEqualTo("extortionate"),
            () -> assertThat(ranges[0].getJurisdiction1().getName()).isEqualTo("test jurisdiction1"),
            () -> assertThat(ranges[0].getJurisdiction2().getName()).isEqualTo("test jurisdiction2"),
            () -> assertThat(ranges[0].getMaxRange()).isEqualTo("5000.00"),
            () -> assertThat(ranges[0].getMinRange()).isEqualTo("3000.01"),
            () -> assertThat(ranges[0].getRangeUnit()).isEqualTo("GBP"),
            () -> assertThat(ranges[0].getServiceType().getName()).isEqualTo("test service"),
            () -> assertThat(ranges[0].getUnspecifiedClaimAmount()).isFalse(),
            () -> assertFeeVersionMatches(ranges[0].getCurrentVersion(),
                    "Test service fees - Amount - 3000.01 up to 5000 GBP",
                    "approved",
                    3,
                    "2015-03-09T00:00Z",
                    205.0,
                    "Test fees £3,000-5,000"),
            () -> assertThat(ranges[0].getMatchingVersion()).isEqualTo(ranges[0].getCurrentVersion()),

            () -> assertThat(ranges[1].getApplicantType().getName()).isEqualTo("all"),
            () -> assertThat(ranges[1].getChannelType().getName()).isEqualTo("test channel"),
            () -> assertThat(ranges[1].getCode()).isEqualTo("FEE1234"),
            () -> assertThat(ranges[1].getEventType().getName()).isEqualTo("test event"),
            () -> assertThat(ranges[1].getFeeType()).isEqualTo("ranged"),
            () -> assertThat(ranges[1].getJurisdiction1().getName()).isEqualTo("test jurisdiction1"),
            () -> assertThat(ranges[1].getJurisdiction2().getName()).isEqualTo("test jurisdiction2"),
            () -> assertThat(ranges[1].getMaxRange()).isEqualTo("200.00"),
            () -> assertThat(ranges[1].getMinRange()).isEqualTo("0.01"),
            () -> assertThat(ranges[1].getRangeUnit()).isEqualTo("GBP"),
            () -> assertThat(ranges[1].getServiceType().getName()).isEqualTo("test service"),
            () -> assertThat(ranges[1].getUnspecifiedClaimAmount()).isFalse(),
            () -> assertFeeVersionMatches(ranges[1].getCurrentVersion(),
                    "Test service fees - Amount - 0.01 up to 200 GBP.",
                    "approved",
                    1,
                    "2015-03-09T00:00Z",
                    5.0,
                    "Test fees £0.01-200"),
            () -> assertThat(ranges[1].getMatchingVersion()).isEqualTo(ranges[1].getCurrentVersion())
        );
    }

    private void assertFeeVersionMatches(
            FeeVersion result,
            String description,
            String status,
            int version,
            String validFrom,
            double amount,
            String memoLine
    ) {
        assertAll(
            () -> assertThat(result.getDescription()).isEqualTo(description),
            () -> assertThat(result.getStatus()).isEqualTo(status),
            () -> assertThat(result.getVersion()).isEqualTo(version),
            () -> assertThat(result.getValidFrom()).isEqualTo(validFrom),
            () -> assertThat(result.getFlatAmount().getAmount().doubleValue()).isEqualTo(amount),
            () -> assertThat(result.getMemoLine()).isEqualTo(memoLine)
        );
    }
}

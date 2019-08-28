package uk.gov.hmcts.reform.fees.client.health;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.fees.client.CoreFeignConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {FeesHealthIndicator.class, CoreFeignConfiguration.class})
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@AutoConfigureWireMock(port = 8091)
class FeesHealthIndicatorTest {
    @Autowired
    private FeesHealthIndicator healthIndicator;

    @Test
    void testHealthCheck() {
        Health health = healthIndicator.health();
        assertNotNull(health);
        assertEquals(Status.UP, health.getStatus());
    }
}

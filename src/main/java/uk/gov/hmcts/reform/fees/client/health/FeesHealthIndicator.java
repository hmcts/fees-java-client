package uk.gov.hmcts.reform.fees.client.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@ConditionalOnProperty(prefix = "fees.api", name = "url")
public class FeesHealthIndicator implements HealthIndicator {
    private String healthEndpoint;

    @Autowired
    public FeesHealthIndicator(@Value("${fees.api.url}") String feesDomain) {
        healthEndpoint = feesDomain + "/health/liveness";
    }

    @Override
    public Health health() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

            HttpEntity<?> entity = new HttpEntity<Object>("", httpHeaders);

            ResponseEntity<InternalHealth> exchange = new RestTemplate().exchange(
                    healthEndpoint,
                    HttpMethod.GET,
                    entity,
                    InternalHealth.class);

            InternalHealth body = exchange.getBody();

            return new Health.Builder(body.getStatus()).build();
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}

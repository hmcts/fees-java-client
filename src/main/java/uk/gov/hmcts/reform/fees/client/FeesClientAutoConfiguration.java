package uk.gov.hmcts.reform.fees.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "fees", name = "api.url")
@EnableFeignClients(basePackages = "uk.gov.hmcts.reform.fees.client")
public class FeesClientAutoConfiguration {

    @Bean
    Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}

package uk.gov.hmcts.reform.fees.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TestConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public FeesApi defaultFeesApi(
            @Value("${fees.api.url}") String domain,
            ObjectMapper objectMapper
    ) {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new Slf4jLogger(FeesApi.class))
                .logLevel(Logger.Level.FULL)
                .target(FeesApi.class, domain);
    }
}
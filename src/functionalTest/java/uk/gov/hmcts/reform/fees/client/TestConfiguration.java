package uk.gov.hmcts.reform.fees.client;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
class TestConfiguration {
    @Bean
    public FeesApi defaultFeesApi(@Value("${fees.api.url}") String domain) {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder(Collections.singleton(new JavaTimeModule())))
                .target(FeesApi.class, domain);
    }
}

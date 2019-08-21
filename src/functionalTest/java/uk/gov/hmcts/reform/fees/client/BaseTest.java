package uk.gov.hmcts.reform.fees.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = {TestConfiguration.class, FeesClient.class, FeesApi.class})
public class BaseTest {

    @Autowired
    protected FeesClient feesClient;

    @Autowired
    protected FeesApi feesApi;
}

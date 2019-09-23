import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import uk.gov.hmcts.reform.fees.client.FeesApi;
import uk.gov.hmcts.reform.fees.client.health.FeesHealthIndicator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@EnableFeignClients
public class Main {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            printUsage();
            System.exit(0);
        }
        if ("health".equalsIgnoreCase(args[0])) {
            checkHealth(args);
        } else if ("fee".equalsIgnoreCase(args[0])) {
            requestFee(args);
        } else if ("ranges".equalsIgnoreCase(args[0])) {
            requestRanges(args);
        } else {
            printUsage();
            System.exit(1);
        }
    }

    private static void checkHealth(String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }

        FeesHealthIndicator client = new FeesHealthIndicator(args[1]);
        Health health = client.health();
        System.out.println(health);
    }

    private static void requestFee(String[] args) {
        if (args.length != 8) {
            printUsage();
            System.exit(1);
        }

        FeesApi feesApi = createFeesFeignClient(args[1]);
        System.out.println(feesApi.lookupFee(args[2], args[3], args[4], args[5], args[6], new BigDecimal(args[7])));
    }

    private static void requestRanges(String[] args) {
        if (args.length != 7) {
            printUsage();
            System.exit(1);
        }

        FeesApi feesApi = createFeesFeignClient(args[1]);
        System.out.println(Arrays.toString(feesApi.findRangeGroup(args[2], args[3], args[4], args[5], args[6])));
    }

    private static FeesApi createFeesFeignClient(String domain) {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder(Collections.singleton(new JavaTimeModule())))
                .target(FeesApi.class, domain);
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("\thealth {domain}");
        System.err.println("\tfee {domain} {service} {jurisdiction1} {jurisdiction2} {channel} {eventType} {amount}");
        System.err.println("\tranges {domain} {service} {jurisdiction1} {jurisdiction2} {channel} {eventType}");
    }

    private Main() {
        // no op
    }
}

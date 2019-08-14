import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import uk.gov.hmcts.reform.fees.client.FeesApi;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
public class Main {
    public static void main(String[] args) {
        if (args.length < 7) {
            printUsage();
            System.exit(1);
        }

        if ("fee".equalsIgnoreCase(args[0])) {
            requestFee(args);
        } else if ("ranges".equalsIgnoreCase(args[0])) {
            requestRanges(args);
        } else {
            printUsage();
            System.exit(1);
        }
    }

    private static void requestFee(String[] args) {
        if (args.length != 8) {
            printUsage();
            System.exit(1);
        }

        FeesApi feesApi = createFeignClient(args[1]);
        System.out.println(feesApi.lookupFee(args[2], args[3], args[4], args[5], args[6], new BigDecimal(args[7])));
    }

    private static void requestRanges(String[] args) {
        if (args.length != 7) {
            printUsage();
            System.exit(1);
        }

        FeesApi feesApi = createFeignClient(args[1]);
        System.out.println(Arrays.toString(feesApi.findRangeGroup(args[2], args[3], args[4], args[5], args[6])));
    }

    private static FeesApi createFeignClient(String domain) {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder())
                .target(FeesApi.class, domain);
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("\tfee {domain} {service} {jurisdiction1} {jurisdiction2} {channel} {eventType} {amount}");
        System.err.println("\tranges {domain} {service} {jurisdiction1} {jurisdiction2} {channel} {eventType}");
    }
}

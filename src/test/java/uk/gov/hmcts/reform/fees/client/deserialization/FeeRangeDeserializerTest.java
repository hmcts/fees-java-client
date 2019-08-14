package uk.gov.hmcts.reform.fees.client.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.hmcts.reform.fees.client.FeeRange;
import uk.gov.hmcts.reform.fees.client.FeeVersion;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class FeeRangeDeserializerTest {
    private ObjectMapper mapper;
    private FeeRangeDeserializer deserializer;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
        this.deserializer = new FeeRangeDeserializer();
    }

    @Test
    void noContentShouldDeserializeToNull() throws IOException {
        FeeRange range = parseAndDeserialize("");
        assertNull(range);
    }

    @Test
    void emptyObjectShouldDeserializeToEmptyFeeRange() throws IOException {
        FeeRange range = parseAndDeserialize("{}");
        assertNotNull(range);
    }

    @Test
    void shouldDeserializeCode() throws IOException {
        FeeRange range = parseAndDeserialize("{\"code\":\"123456\"}");
        assertEquals("123456", range.getCode());
    }

    @Test
    void shouldDeserializeFeeType() throws IOException {
        FeeRange range = parseAndDeserialize("{\"fee_type\":\"ranged\"}");
        assertEquals("ranged", range.getFeeType());
    }

    @Test
    void shouldDeserializeChannelType() throws IOException {
        FeeRange range = parseAndDeserialize("{\"channel_type\":{\"name\":\"online\"}}");
        assertEquals("online", range.getChannelType());
    }

    @Test
    void shouldDeserializeEventType() throws IOException {
        FeeRange range = parseAndDeserialize("{\"event_type\":{\"name\":\"issue\"}}");
        assertEquals("issue", range.getEventType());
    }

    @Test
    void shouldDeserializeJurisdiction1() throws IOException {
        FeeRange range = parseAndDeserialize("{\"jurisdiction1\":{\"name\":\"civil\"}}");
        assertEquals("civil", range.getJurisdiction1());
    }

    @Test
    void shouldDeserializeJurisdiction2() throws IOException {
        FeeRange range = parseAndDeserialize("{\"jurisdiction2\":{\"name\":\"county court\"}}");
        assertEquals("county court", range.getJurisdiction2());
    }

    @Test
    void shouldDeserializeServiceType() throws IOException {
        FeeRange range = parseAndDeserialize("{\"service_type\":{\"name\":\"civil money claims\"}}");
        assertEquals("civil money claims", range.getServiceType());
    }

    @Test
    void shouldDeserializeApplicantType() throws IOException {
        FeeRange range = parseAndDeserialize("{\"applicant_type\":{\"name\":\"all\"}}");
        assertEquals("all", range.getApplicantType());
    }

    @Test
    void shouldDeserializeUnit() throws IOException {
        FeeRange range = parseAndDeserialize("{\"range_unit\":\"GBP\"}");
        assertEquals("GBP", range.getUnit());
    }

    @Test
    void shouldDeserializeMinimum() throws IOException {
        FeeRange range = parseAndDeserialize("{\"min_range\":300.01}");
        assertNotNull(range.getMinimum());
        assertEquals(300.01, range.getMinimum().doubleValue());
    }

    @Test
    void shouldDeserializeMaximum() throws IOException {
        FeeRange range = parseAndDeserialize("{\"max_range\":500.00}");
        assertNotNull(range.getMaximum());
        assertEquals(500.00, range.getMaximum().doubleValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void shouldDeserializeUnspecifiedClaimAmount(String value) throws IOException {
        FeeRange range = parseAndDeserialize("{\"unspecified_claim_amount\":" + value + "}");
        assertEquals(Boolean.parseBoolean(value), range.getUnspecifiedClaimAmount());
    }

    @Test
    void shouldDeserializeFeeVersions() throws IOException {
        FeeRange range = parseAndDeserialize("{\"fee_versions\":[{},{}]}");
        List<FeeVersion> feeVersions = range.getFeeVersions();
        assertNotNull(feeVersions);
        assertEquals(2, feeVersions.size());
    }

    @Test
    void shouldDeserializeCurrentVersion() throws IOException {
        FeeRange range = parseAndDeserialize("{\"current_version\":{}}");
        assertNotNull(range.getCurrentVersion());
    }

    @Test
    void shouldDeserializeMatchingVersion() throws IOException {
        FeeRange range = parseAndDeserialize("{\"matching_version\":{}}");
        assertNotNull(range.getMatchingVersion());
    }

    private FeeRange parseAndDeserialize(String content) throws IOException {
        JsonParser parser = mapper.getFactory().createParser(content);
        DeserializationContext context = mapper.getDeserializationContext();
        return deserializer.deserialize(parser, context);
    }

}

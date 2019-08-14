package uk.gov.hmcts.reform.fees.client.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.fees.client.FeeVersion;

import java.io.IOException;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FeeVersionSubDeserializerTest {
    private ObjectMapper mapper;
    private FeeVersionSubDeserializer deserializer;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
        this.deserializer = new FeeVersionSubDeserializer();
    }

    @Test
    void shouldDeserializeDescription() throws IOException {
        FeeVersion version = parseAndDeserialize(
                "{\"description\":\"Civil Court fees - Money Claims Online - Claim Amount - 300.01 upto 500 GBP\"}");
        assertEquals("Civil Court fees - Money Claims Online - Claim Amount - 300.01 upto 500 GBP",
                version.getDescription());
    }

    @Test
    void shouldDeserializeStatus() throws IOException {
        FeeVersion version = parseAndDeserialize("{\"status\":\"approved\"}");
        assertEquals("approved", version.getStatus());
    }

    @Test
    void shouldDeserializeVersion() throws IOException {
        FeeVersion version = parseAndDeserialize("{\"version\":3}");
        assertEquals(3, version.getVersion().intValue());
    }

    @Test
    void shouldDeserializeValidFrom() throws IOException {
        FeeVersion version = parseAndDeserialize("{\"valid_from\":\"2014-04-22T00:00:00.000+0000\"}");
        OffsetDateTime actual = version.getValidFrom();
        assertAll(
                () -> assertEquals(2014, actual.getYear()),
                () -> assertEquals(Month.APRIL, actual.getMonth()),
                () -> assertEquals(22, actual.getDayOfMonth()),
                () -> assertEquals(0, actual.getHour()),
                () -> assertEquals(0, actual.getMinute()),
                () -> assertEquals(0, actual.getSecond()),
                () -> assertEquals(ZoneOffset.UTC, actual.getOffset())
        );
    }

    @Test
    void shouldDeserializeAmount() throws IOException {
        FeeVersion version = parseAndDeserialize("{\"flat_amount\":{\"amount\":35.00}}");
        assertEquals(35.0, version.getAmount().doubleValue());
    }

    @Test
    void shouldDeserializeNaturalAccountCode() throws IOException {
        FeeVersion version = parseAndDeserialize("{\"natural_account_code\":\"4481102133\"}");
        assertEquals("4481102133", version.getNaturalAccountCode());
    }

    @Test
    void shouldDeserializeDirection() throws IOException {
        FeeVersion version = parseAndDeserialize("{\"direction\":\"enhanced\"}");
        assertEquals("enhanced", version.getDirection());
    }

    @Test
    void shouldDeserializeMemoLine() throws IOException {
        FeeVersion version =
                parseAndDeserialize("{\"memo_line\":\"GOV.UK Pay online claims - Money Claim £300-500\"}");
        assertEquals("GOV.UK Pay online claims - Money Claim £300-500", version.getMemoLine());
    }

    private FeeVersion parseAndDeserialize(String content) throws IOException {
        JsonParser parser = mapper.getFactory().createParser(content);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonNode node = parser.getCodec().readTree(parser);
        return deserializer.deserialize(node);
    }
}

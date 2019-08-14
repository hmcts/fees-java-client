package uk.gov.hmcts.reform.fees.client.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import uk.gov.hmcts.reform.fees.client.FeeVersion;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static uk.gov.hmcts.reform.fees.client.deserialization.DeserializationUtils.extractNode;

public class FeeVersionSubDeserializer {
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX", Locale.ROOT);

    public FeeVersion deserialize(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }

        FeeVersion version = new FeeVersion();

        extractNode(version::setDescription, JsonNode::asText, node, "description");
        extractNode(version::setStatus, JsonNode::asText, node, "status");
        extractNode(version::setVersion, JsonNode::asInt, node, "version");
        extractNode(version::setValidFrom, n -> OffsetDateTime.parse(n.asText(), formatter), node, "valid_from");
        extractNode(version::setAmount, n -> new BigDecimal(n.asDouble()), node, "flat_amount", "amount");
        extractNode(version::setMemoLine, JsonNode::asText, node, "memo_line");
        extractNode(version::setNaturalAccountCode, JsonNode::asText, node, "natural_account_code");
        extractNode(version::setDirection, JsonNode::asText, node, "direction");

        return version;
    }
}

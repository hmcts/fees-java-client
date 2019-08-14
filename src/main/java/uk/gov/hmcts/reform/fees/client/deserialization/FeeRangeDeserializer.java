package uk.gov.hmcts.reform.fees.client.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.gov.hmcts.reform.fees.client.FeeRange;
import uk.gov.hmcts.reform.fees.client.FeeVersion;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static uk.gov.hmcts.reform.fees.client.deserialization.DeserializationUtils.extractNode;

public class FeeRangeDeserializer extends StdDeserializer<FeeRange> {
    private final FeeVersionSubDeserializer feeVersionDeserializer = new FeeVersionSubDeserializer();

    public FeeRangeDeserializer() {
        super(FeeRange.class);
    }

    @Override
    public FeeRange deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }

        FeeRange range = new FeeRange();

        extractNode(range::setCode, JsonNode::asText, node, "code");
        extractNode(range::setFeeType, JsonNode::asText, node, "fee_type");
        extractNode(range::setChannelType, JsonNode::asText, node, "channel_type", "name");
        extractNode(range::setEventType, JsonNode::asText, node, "event_type", "name");
        extractNode(range::setJurisdiction1, JsonNode::asText, node, "jurisdiction1", "name");
        extractNode(range::setJurisdiction2, JsonNode::asText, node, "jurisdiction2", "name");
        extractNode(range::setServiceType, JsonNode::asText, node, "service_type", "name");
        extractNode(range::setApplicantType, JsonNode::asText, node, "applicant_type", "name");
        extractNode(range::setUnit, JsonNode::asText, node, "range_unit");
        extractNode(range::setMinimum, n -> new BigDecimal(n.asDouble()), node, "min_range");
        extractNode(range::setMaximum, n -> new BigDecimal(n.asDouble()), node, "max_range");
        extractNode(range::setUnspecifiedClaimAmount, JsonNode::asBoolean, node, "unspecified_claim_amount");

        range.setFeeVersions(extractFeeVersions(node.get("fee_versions")));
        range.setCurrentVersion(feeVersionDeserializer.deserialize(node.get("current_version")));
        range.setMatchingVersion(feeVersionDeserializer.deserialize(node.get("matching_version")));

        return range;
    }

    private List<FeeVersion> extractFeeVersions(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull() || !node.isArray()) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(node.spliterator(), false)
                .map(feeVersionDeserializer::deserialize)
                .collect(Collectors.toList());
    }
}

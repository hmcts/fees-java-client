package uk.gov.hmcts.reform.fees.client.deserialization;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DeserializationUtils {
    private DeserializationUtils() {
        // no op
    }

    static Optional<JsonNode> getNested(JsonNode node, String root, String... children) {
        Optional<JsonNode> focus = Optional.ofNullable(node.get(root));
        for (String child : children) {
            focus = focus.map(n -> n.get(child));
        }
        return focus;
    }

    static Optional<String> getNestedText(JsonNode node, String root, String... children) {
        return getNested(node, root, children).map(JsonNode::asText);
    }

    static <DTO, FIELD> void extractNode(
            Consumer<FIELD> setter,
            Function<JsonNode, FIELD> nodeToValue,
            FIELD fallback,
            JsonNode node,
            String... children
    ) {
        Optional<JsonNode> focus = Optional.ofNullable(node);
        for (String child : children) {
            focus = focus.map(n -> n.get(child));
        }
        setter.accept(focus.map(nodeToValue).orElse(fallback));
    }

    static <DTO, FIELD> void extractNode(
            Consumer<FIELD> setter,
            Function<JsonNode, FIELD> nodeToValue,
            JsonNode node,
            String... children
    ) {
        extractNode(setter, nodeToValue, null, node, children);
    }
}

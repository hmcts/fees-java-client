package uk.gov.hmcts.reform.fees.client.deserialization;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

class DeserializationUtils {
    private DeserializationUtils() {
        // no op
    }

    static <T> void extractNode(
            Consumer<T> setter,
            Function<JsonNode, T> nodeToValue,
            JsonNode node,
            String... children
    ) {
        Optional<JsonNode> focus = Optional.ofNullable(node);
        for (String child : children) {
            focus = focus.map(n -> n.get(child));
        }
        setter.accept(focus.map(nodeToValue).orElse(null));
    }
}

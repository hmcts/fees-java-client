package uk.gov.hmcts.reform.fees.client.health;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

class InternalHealthTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateInternalHealthWithStatusAndComponents() {

        Status status = UP;
        Map<String, Object> components = Map.of("database", "healthy");

        InternalHealth internalHealth = new InternalHealth(status, components);

        assertThat(internalHealth)
                .isNotNull()
                .extracting(InternalHealth::getStatus, InternalHealth::getComponents)
                .containsExactly(status, components);
    }

    @Test
    void shouldCreateInternalHealthWithNullComponents() {

        Status status = UP;

        InternalHealth internalHealth = new InternalHealth(status, null);

        assertThat(internalHealth.getStatus()).isEqualTo(status);
        assertThat(internalHealth.getComponents()).isNull();
    }

    @Test
    void shouldCreateInternalHealthWithNullStatus() {

        Map<String, Object> components = Map.of("database", "healthy");

        InternalHealth internalHealth = new InternalHealth(null, components);

        assertThat(internalHealth.getStatus()).isNull();
        assertThat(internalHealth.getComponents()).isEqualTo(components);
    }

    @Test
    void shouldCreateInternalHealthWithEmptyComponents() {

        Status status = UP;
        Map<String, Object> components = new HashMap<>();

        InternalHealth internalHealth = new InternalHealth(status, components);

        assertThat(internalHealth.getStatus()).isEqualTo(status);
        assertThat(internalHealth.getComponents())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldDeserializeJsonWithStatusAndComponents() throws Exception {

        String json = """
            {
                "status": "UP",
                "components": {
                    "database": {
                        "status": "UP"
                    },
                    "diskSpace": {
                        "status": "UP",
                        "free": "10GB"
                    }
                }
            }
            """;

        InternalHealth internalHealth = objectMapper.readValue(json, InternalHealth.class);

        assertThat(internalHealth).isNotNull();
        assertThat(internalHealth.getStatus()).isEqualTo(UP);
        assertThat(internalHealth.getComponents())
                .isNotNull()
                .containsKeys("database", "diskSpace");
    }

    @Test
    void shouldDeserializeJsonWithOnlyStatus() throws Exception {

        String json = """
            {
                "status": "DOWN"
            }
            """;

        InternalHealth internalHealth = objectMapper.readValue(json, InternalHealth.class);

        assertThat(internalHealth).isNotNull();
        assertThat(internalHealth.getStatus()).isEqualTo(DOWN);
        assertThat(internalHealth.getComponents()).isNull();
    }

    @Test
    void shouldIgnoreUnknownJsonProperties() throws Exception {

        String json = """
            {
                "status": "UP",
                "components": {},
                "unknownProperty": "someValue",
                "anotherUnknown": 123
            }
            """;

        InternalHealth internalHealth = objectMapper.readValue(json, InternalHealth.class);

        assertThat(internalHealth).isNotNull();
        assertThat(internalHealth.getStatus()).isEqualTo(UP);
        assertThat(internalHealth.getComponents()).isEmpty();
    }

    @Test
    void shouldHandleComplexComponentStructure() {

        Status status = UP;
        Map<String, Object> dbDetails = Map.of(
                "status", "UP",
                "database", "PostgreSQL",
                "validationQuery", "isValid()"
        );
        Map<String, Object> components = Map.of(
                "db", dbDetails,
                "ping", Map.of("status", "UP")
        );

        InternalHealth internalHealth = new InternalHealth(status, components);

        assertThat(internalHealth.getStatus()).isEqualTo(status);
        assertThat(internalHealth.getComponents())
                .containsKeys("db", "ping")
                .hasSize(2);

        assertThat(internalHealth.getComponents().get("db"))
                .isInstanceOf(Map.class);
    }

    @Test
    void shouldHandleCustomStatus() throws Exception {

        String json = """
            {
                "status": "CUSTOM_STATUS",
                "components": {}
            }
            """;

        InternalHealth internalHealth = objectMapper.readValue(json, InternalHealth.class);

        assertThat(internalHealth).isNotNull();
        assertThat(internalHealth.getStatus().getCode()).isEqualTo("CUSTOM_STATUS");
    }

    @Test
    void shouldDeserializeDownStatusWithErrorDetails() throws Exception {

        String json = """
            {
                "status": "DOWN",
                "components": {
                    "database": {
                        "status": "DOWN",
                        "error": "Connection refused",
                        "details": "Could not connect to database"
                    }
                }
            }
            """;

        InternalHealth internalHealth = objectMapper.readValue(json, InternalHealth.class);

        assertThat(internalHealth).isNotNull();
        assertThat(internalHealth.getStatus()).isEqualTo(DOWN);
        assertThat(internalHealth.getComponents())
                .containsKey("database");

        @SuppressWarnings("unchecked")
        Map<String, Object> dbComponent = (Map<String, Object>) internalHealth.getComponents().get("database");
        assertThat(dbComponent)
                .containsEntry("error", "Connection refused")
                .containsEntry("details", "Could not connect to database");
    }

    @Test
    void shouldSerializeToJson() throws Exception {

        Map<String, Object> components = Map.of("database", "healthy");
        InternalHealth internalHealth = new InternalHealth(UP, components);

        String json = objectMapper.writeValueAsString(internalHealth);

        assertThat(json)
                .contains("\"status\"")
                .contains("\"components\"");
    }
}
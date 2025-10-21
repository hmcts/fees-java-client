package uk.gov.hmcts.reform.fees.client.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import uk.gov.hmcts.reform.fees.client.FeesApi;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

@ExtendWith(MockitoExtension.class)
class FeesHealthIndicatorTest {

    @Mock
    private FeesApi feesApi;

    private FeesHealthIndicator feesHealthIndicator;

    @BeforeEach
    void setUp() {
        feesHealthIndicator = new FeesHealthIndicator(feesApi);
    }

    @Test
    void shouldCreateFeesHealthIndicatorWithCorrectParameters() {
        FeesHealthIndicator indicator = new FeesHealthIndicator(feesApi);

        assertThat(indicator).isNotNull();
    }

    @Test
    void shouldReturnHealthUpWhenFeesApiHealthCheckIsSuccessful() {

        InternalHealth internalHealth = new InternalHealth(UP, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(UP);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthDownWhenFeesApiHealthCheckReturnsDown() {
        
        InternalHealth internalHealth = new InternalHealth(DOWN, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthOutOfServiceWhenFeesApiHealthCheckReturnsOutOfService() {
        
        InternalHealth internalHealth = new InternalHealth(Status.OUT_OF_SERVICE, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(Status.OUT_OF_SERVICE);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthUnknownWhenFeesApiHealthCheckReturnsUnknown() {
        
        InternalHealth internalHealth = new InternalHealth(Status.UNKNOWN, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(Status.UNKNOWN);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldHandleInternalHealthWithComponents() {
        
        Map<String, Object> components = new HashMap<>();
        components.put("database", Map.of("status", "UP"));
        components.put("diskSpace", Map.of("status", "UP", "free", "10GB"));

        InternalHealth internalHealth = new InternalHealth(UP, components);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(UP);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldHandleInternalHealthWithEmptyComponents() {
        
        Map<String, Object> components = new HashMap<>();
        InternalHealth internalHealth = new InternalHealth(UP, components);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(UP);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthDownWhenFeesApiThrowsRuntimeException() {
        
        RuntimeException exception = new RuntimeException("Connection timeout");
        when(feesApi.health()).thenThrow(exception);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        assertThat(result.getDetails())
                .containsEntry("error", "java.lang.RuntimeException: Connection timeout");

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthDownWhenFeesApiThrowsRuntimeExceptionInsteadOfCheckedException() {
        
        RuntimeException exception = new RuntimeException("Service unavailable");
        when(feesApi.health()).thenThrow(exception);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        assertThat(result.getDetails())
                .containsEntry("error", "java.lang.RuntimeException: Service unavailable");

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthDownWhenFeesApiThrowsNullPointerException() {
        
        NullPointerException exception = new NullPointerException("Null response");
        when(feesApi.health()).thenThrow(exception);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        assertThat(result.getDetails())
                .isNotEmpty()
                .containsKey("error");

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnHealthDownWhenFeesApiThrowsIllegalStateException() {
        
        IllegalStateException exception = new IllegalStateException("Invalid state");
        when(feesApi.health()).thenThrow(exception);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        assertThat(result.getDetails())
                .containsEntry("error", "java.lang.IllegalStateException: Invalid state");

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldIncludeExceptionDetailsInHealthDownResponse() {
        
        String errorMessage = "Database connection failed";
        RuntimeException exception = new RuntimeException(errorMessage);
        when(feesApi.health()).thenThrow(exception);

        Health result = feesHealthIndicator.health();

        assertThat(result.getStatus()).isEqualTo(DOWN);
        assertThat(result.getDetails())
                .containsKey("error")
                .extractingByKey("error")
                .asString()
                .contains(errorMessage);
    }

    @Test
    void shouldHandleCustomStatusFromInternalHealth() {
        
        Status customStatus = new Status("CUSTOM");
        InternalHealth internalHealth = new InternalHealth(customStatus, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(customStatus);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldCallFeesApiHealthOnlyOnce() {
        
        InternalHealth internalHealth = new InternalHealth(UP, null);
        when(feesApi.health()).thenReturn(internalHealth);

        feesHealthIndicator.health();

        verify(feesApi, times(1)).health();
        verifyNoMoreInteractions(feesApi);
    }

    @Test
    void shouldReturnHealthDownWhenExceptionHasNoMessage() {
        
        RuntimeException exception = new RuntimeException();
        when(feesApi.health()).thenThrow(exception);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        assertThat(result.getDetails())
                .containsKey("error");

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldBuildHealthWithOnlyStatusWhenNoExceptionOccurs() {
        
        InternalHealth internalHealth = new InternalHealth(UP, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(UP);

        assertThat(result.getDetails()).isEmpty();

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldHandleInternalHealthWithComplexComponentStructure() {
        
        Map<String, Object> dbDetails = new HashMap<>();
        dbDetails.put("status", "UP");
        dbDetails.put("database", "PostgreSQL");
        dbDetails.put("validationQuery", "isValid()");

        Map<String, Object> components = new HashMap<>();
        components.put("db", dbDetails);
        components.put("ping", Map.of("status", "UP"));

        InternalHealth internalHealth = new InternalHealth(UP, components);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(UP);

        assertThat(result.getDetails()).isEmpty();

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldReturnDownStatusWhenInternalHealthStatusIsDown() {
        
        Map<String, Object> components = new HashMap<>();
        components.put("database", Map.of("status", "DOWN", "error", "Connection refused"));

        InternalHealth internalHealth = new InternalHealth(DOWN, components);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        verify(feesApi, times(1)).health();
    }

    @Test
    void shouldHandleNullStatusInInternalHealth() {
        
        InternalHealth internalHealth = new InternalHealth(null, null);
        when(feesApi.health()).thenReturn(internalHealth);

        Health result = feesHealthIndicator.health();

        assertThat(result)
                .isNotNull()
                .extracting(Health::getStatus)
                .isEqualTo(DOWN);

        verify(feesApi, times(1)).health();
    }
}
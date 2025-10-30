package uk.gov.hmcts.reform.fees.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.fees.client.model.Fee2Dto;
import uk.gov.hmcts.reform.fees.client.model.FeeLookupResponseDto;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeesClientTest {

    @Mock
    private FeesApi feesApi;

    private FeesClient feesClient;

    private static final String SERVICE = "test-service";
    private static final String JURISDICTION_1 = "jurisdiction-1";
    private static final String JURISDICTION_2 = "jurisdiction-2";
    private static final String CHANNEL = "online";
    private static final String EVENT = "issue";
    private static final BigDecimal AMOUNT = new BigDecimal("1000.00");

    @BeforeEach
    void setUp() {
        feesClient = new FeesClient(feesApi, SERVICE, JURISDICTION_1, JURISDICTION_2);
    }

    @Test
    void shouldCreateFeesClientWithCorrectParameters() {
        FeesClient client = new FeesClient(feesApi, SERVICE, JURISDICTION_1, JURISDICTION_2);
        assertThat(client).isNotNull();
    }

    @Test
    void shouldLookupFeeSuccessfully() {
        FeeLookupResponseDto expectedResponse = FeeLookupResponseDto.builder()
                .code("FEE001")
                .description("Test fee")
                .feeAmount(new BigDecimal("50.00"))
                .version(1)
                .build();

        when(feesApi.lookupFee(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT, null, AMOUNT, null))
                .thenReturn(expectedResponse);

        FeeLookupResponseDto result = feesClient.lookupFee(CHANNEL, EVENT, AMOUNT);

        assertThat(result)
                .isNotNull()
                .extracting(
                        FeeLookupResponseDto::getCode,
                        FeeLookupResponseDto::getDescription,
                        FeeLookupResponseDto::getFeeAmount,
                        FeeLookupResponseDto::getVersion
                )
                .containsExactly("FEE001", "Test fee", new BigDecimal("50.00"), 1);

        verify(feesApi, times(1))
                .lookupFee(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT, null, AMOUNT, null);
    }

    @Test
    void shouldPassCorrectParametersToFeesApiForLookupFee() {
        FeeLookupResponseDto response = FeeLookupResponseDto.builder().build();

        when(feesApi.lookupFee(
                anyString(), anyString(), anyString(), anyString(), anyString(),
                any(),
                any(BigDecimal.class),
                any()
        )).thenReturn(response);

        feesClient.lookupFee(CHANNEL, EVENT, AMOUNT);

        verify(feesApi).lookupFee(
                SERVICE,
                JURISDICTION_1,
                JURISDICTION_2,
                CHANNEL,
                EVENT,
                null,
                AMOUNT,
                null
        );
    }

    @Test
    void shouldLookupFeeWithKeywordOverload() {
        String keyword = "urgent";
        FeeLookupResponseDto expectedResponse = FeeLookupResponseDto.builder()
                .code("FEE-KEYWORD")
                .build();

        when(feesApi.lookupFee(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT, null, AMOUNT, keyword))
                .thenReturn(expectedResponse);

        FeeLookupResponseDto result = feesClient.lookupFee(CHANNEL, EVENT, AMOUNT, keyword);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("FEE-KEYWORD");

        verify(feesApi).lookupFee(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT, null, AMOUNT, keyword);
    }

    @Test
    void shouldFindRangeGroupSuccessfully() {
        Fee2Dto fee1 = Fee2Dto.builder()
                .code("FEE001")
                .feeType("range")
                .minRange(new BigDecimal("0"))
                .maxRange(new BigDecimal("500"))
                .build();

        Fee2Dto fee2 = Fee2Dto.builder()
                .code("FEE002")
                .feeType("range")
                .minRange(new BigDecimal("500.01"))
                .maxRange(new BigDecimal("1000"))
                .build();

        Fee2Dto[] expectedResponse = {fee1, fee2};

        when(feesApi.findRangeGroup(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT))
                .thenReturn(expectedResponse);

        Fee2Dto[] result = feesClient.findRangeGroup(CHANNEL, EVENT);

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(Fee2Dto::getCode)
                .containsExactly("FEE001", "FEE002");

        assertThat(result[0])
                .extracting(
                        Fee2Dto::getCode,
                        Fee2Dto::getFeeType,
                        Fee2Dto::getMinRange,
                        Fee2Dto::getMaxRange
                )
                .containsExactly("FEE001", "range", new BigDecimal("0"), new BigDecimal("500"));

        verify(feesApi, times(1))
                .findRangeGroup(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT);
    }

    @Test
    void shouldPassCorrectParametersToFeesApiForFindRangeGroup() {
        Fee2Dto[] response = new Fee2Dto[0];
        when(feesApi.findRangeGroup(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(response);

        feesClient.findRangeGroup(CHANNEL, EVENT);

        verify(feesApi).findRangeGroup(
                SERVICE,
                JURISDICTION_1,
                JURISDICTION_2,
                CHANNEL,
                EVENT
        );
    }

    @Test
    void shouldReturnEmptyArrayWhenNoRangeGroupsFound() {
        Fee2Dto[] expectedResponse = new Fee2Dto[0];
        when(feesApi.findRangeGroup(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT))
                .thenReturn(expectedResponse);

        Fee2Dto[] result = feesClient.findRangeGroup(CHANNEL, EVENT);

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldHandleNullResponseFromLookupFee() {
        when(feesApi.lookupFee(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT, null, AMOUNT, null))
                .thenReturn(null);

        FeeLookupResponseDto result = feesClient.lookupFee(CHANNEL, EVENT, AMOUNT);

        assertThat(result).isNull();
    }

    @Test
    void shouldHandleNullResponseFromFindRangeGroup() {
        when(feesApi.findRangeGroup(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT))
                .thenReturn(null);

        Fee2Dto[] result = feesClient.findRangeGroup(CHANNEL, EVENT);

        assertThat(result).isNull();
    }

    @Test
    void shouldWorkWithEmptyStringConfigurationValues() {
        FeesClient clientWithEmptyStrings = new FeesClient(feesApi, "", "", "");
        FeeLookupResponseDto expectedResponse = FeeLookupResponseDto.builder().build();

        when(feesApi.lookupFee("", "", "", CHANNEL, EVENT, null, AMOUNT, null))
                .thenReturn(expectedResponse);

        FeeLookupResponseDto result = clientWithEmptyStrings.lookupFee(CHANNEL, EVENT, AMOUNT);

        assertThat(result).isNotNull();
        verify(feesApi).lookupFee("", "", "", CHANNEL, EVENT, null, AMOUNT, null);
    }

    @Test
    void shouldLookupFeeWithDifferentAmounts() {
        BigDecimal smallAmount = new BigDecimal("10.50");
        FeeLookupResponseDto expectedResponse = FeeLookupResponseDto.builder()
                .feeAmount(new BigDecimal("5.00"))
                .build();

        when(feesApi.lookupFee(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT, null, smallAmount, null))
                .thenReturn(expectedResponse);

        FeeLookupResponseDto result = feesClient.lookupFee(CHANNEL, EVENT, smallAmount);

        assertThat(result)
                .isNotNull()
                .extracting(FeeLookupResponseDto::getFeeAmount)
                .isEqualTo(new BigDecimal("5.00"));
    }

    @Test
    void shouldVerifyFeesApiIsCalledOnlyOnce() {
        Fee2Dto[] expectedResponse = new Fee2Dto[1];
        when(feesApi.findRangeGroup(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT))
                .thenReturn(expectedResponse);

        feesClient.findRangeGroup(CHANNEL, EVENT);

        verify(feesApi, times(1))
                .findRangeGroup(SERVICE, JURISDICTION_1, JURISDICTION_2, CHANNEL, EVENT);
        verifyNoMoreInteractions(feesApi);
    }
}
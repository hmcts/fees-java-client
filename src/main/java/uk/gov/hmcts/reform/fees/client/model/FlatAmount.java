package uk.gov.hmcts.reform.fees.client.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FlatAmount {
    private BigDecimal amount;
}

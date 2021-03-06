package com.nordcomet.portfolio.service.transaction.imports.fidelity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@ToString
class FidelityTransaction {

    private final LocalDate orderDate;
    private final String transactionType;
    private final String investment;
    private final BigDecimal amount;
    private final BigDecimal quantity;
    private final BigDecimal pricePerUnit;

}

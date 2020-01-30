package com.nordcomet.portfolio.service.transaction.imports.halifax;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@ToString
class HalifaxTransaction {

    private final LocalDate date;
    private final String companyCode;
    private final BigDecimal quantity;
    private final BigDecimal executedPrice;
    private final BigDecimal netConsideration;
    private final String reference;

}

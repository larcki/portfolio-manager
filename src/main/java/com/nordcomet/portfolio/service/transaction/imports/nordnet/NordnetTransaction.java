package com.nordcomet.portfolio.service.transaction.imports.nordnet;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@ToString
class NordnetTransaction {

    private final LocalDate date;
    private final String assetName;
    private final String isin;
    private final String currency;
    private final BigDecimal quantity;
    private final BigDecimal purchasePriceInCurrency;
    private final BigDecimal purchasePriceInEur;
    private final BigDecimal purchaseValueInCurrency;
    private final BigDecimal purchaseValueInEur;
    private final BigDecimal marketValueInCurrency;
    private final BigDecimal marketValueInEur;

}

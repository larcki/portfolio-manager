package com.nordcomet.pflio.asset.service.importer.halifax;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class HalifaxTransaction {

    private final LocalDate date;
    private final String companyCode;
    private final BigDecimal quantity;
    private final BigDecimal executedPrice;
    private final BigDecimal netConsideration;
    private final String reference;

}

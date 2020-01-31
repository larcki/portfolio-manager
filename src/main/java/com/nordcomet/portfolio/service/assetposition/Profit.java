package com.nordcomet.portfolio.service.assetposition;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class Profit {

    private BigDecimal value;
    private BigDecimal purchaseAmount;
    private BigDecimal profit;
    private BigDecimal profitPercentage;

}

package com.nordcomet.pflio.asset.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class PortfolioSummary {

    private String totalValue;
    private String totalPurchaseAmount;
    private String totalProfit;
    private String totalProfitPercentage;

}

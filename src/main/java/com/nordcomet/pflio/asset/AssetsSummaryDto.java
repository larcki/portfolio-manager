package com.nordcomet.pflio.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AssetsSummaryDto {

    private String marketValue;
    private String amountSpent;
    private String performance;

}

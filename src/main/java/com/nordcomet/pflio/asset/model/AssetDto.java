package com.nordcomet.pflio.asset.model;

import lombok.Data;

@Data
public class AssetDto {

    private final String name;
    private final String account;
    private final String totalValue;
    private final String performance;
    private final String performancePercentage;
    private final Integer id;

}

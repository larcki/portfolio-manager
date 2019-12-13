package com.nordcomet.pflio.asset.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class AssetDto {

    private final String name;
    private final String account;
    private final String totalValue;
    private final String performance;
    private final String performancePercentage;
    private final Integer id;

}

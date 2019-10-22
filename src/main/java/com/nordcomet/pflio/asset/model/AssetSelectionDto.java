package com.nordcomet.pflio.asset.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class AssetSelectionDto {

    private final Integer id;
    private final String name;
    private final String account;
    private final String currency;

}

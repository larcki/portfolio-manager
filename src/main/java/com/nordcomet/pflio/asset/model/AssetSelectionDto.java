package com.nordcomet.pflio.asset.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AssetSelectionDto {

    private final Integer id;
    private final String name;
    private final String account;
    private final String currency;

}

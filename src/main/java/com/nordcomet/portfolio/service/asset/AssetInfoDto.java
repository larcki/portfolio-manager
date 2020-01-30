package com.nordcomet.portfolio.service.asset;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class AssetInfoDto {

    private String name;
    private String link;
    private String account;
    private BigDecimal totalValue;

}

package com.nordcomet.portfolio.data.asset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AssetClass {

    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal proportion;

    @Enumerated(EnumType.STRING)
    private AssetClassType assetClassType;

    @Enumerated(EnumType.STRING)
    private Region region;

    public AssetClass(Region region, AssetClassType assetClassType, BigDecimal proportion) {
        this.proportion = proportion;
        this.assetClassType = assetClassType;
        this.region = region;
    }
}

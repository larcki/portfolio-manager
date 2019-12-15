package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AssetClass2 {

    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal proportion;

    @Enumerated(EnumType.STRING)
    private AssetClassType assetClassType;

    @Enumerated(EnumType.STRING)
    private Region region;

    public AssetClass2(Region region, AssetClassType assetClassType, BigDecimal proportion) {
        this.proportion = proportion;
        this.assetClassType = assetClassType;
        this.region = region;
    }
}

package com.nordcomet.pflio.asset.model;

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
    private AssetClassType name;

    public AssetClass(AssetClassType name, BigDecimal proportion) {
        this.proportion = proportion;
        this.name = name;
    }

}

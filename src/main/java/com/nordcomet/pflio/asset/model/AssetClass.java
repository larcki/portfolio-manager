package com.nordcomet.pflio.asset.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class AssetClass {

    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal proportion;

    @Enumerated(EnumType.STRING)
    private AssetClassType name;

    public AssetClass() {
    }

    public AssetClass(AssetClassType name, BigDecimal proportion) {
        this.proportion = proportion;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public AssetClassType getName() {
        return name;
    }

    public void setName(AssetClassType name) {
        this.name = name;
    }
}

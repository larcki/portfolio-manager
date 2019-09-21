package com.nordcomet.pflio.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private BigDecimal proportion;

    private Tags name;

    @ManyToMany(mappedBy = "tags")
    private List<Asset> assets;

    public Tag(BigDecimal proportion, Tags name) {
        this.proportion = proportion;
        this.name = name;
    }

    public Tag() {
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public Tags getName() {
        return name;
    }

    public List<Asset> getAssets() {
        return assets;
    }
}

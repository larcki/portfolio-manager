package com.nordcomet.pflio.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private ParserOptions parserOptions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "asset_class_mapping",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "asset_class_id")
    )
    private Set<AssetClass> assetClasses;

    @Enumerated(EnumType.STRING)
    private Region region;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account account;

    private String isin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProportionOfAssetClass(AssetClassType assetClassType) {
        return getAssetClasses().stream()
                .filter(assetClass -> assetClass.getName() == assetClassType)
                .findFirst().map(AssetClass::getProportion)
                .orElse(BigDecimal.ZERO);
    }

    public ParserOptions getParserOptions() {
        return parserOptions;
    }

    public void setParserOptions(ParserOptions parserOptions) {
        this.parserOptions = parserOptions;
    }

    public Set<AssetClass> getAssetClasses() {
        return assetClasses;
    }

    public void setAssetClasses(Set<AssetClass> assetClasses) {
        this.assetClasses = assetClasses;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(id, asset.id) &&
                Objects.equals(name, asset.name) &&
                Objects.equals(parserOptions, asset.parserOptions) &&
                Objects.equals(assetClasses, asset.assetClasses) &&
                region == asset.region;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parserOptions, assetClasses, region);
    }
}

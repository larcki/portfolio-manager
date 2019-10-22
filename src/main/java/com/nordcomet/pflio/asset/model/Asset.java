package com.nordcomet.pflio.asset.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
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

    public BigDecimal getProportionOfAssetClass(AssetClassType assetClassType) {
        return getAssetClasses().stream()
                .filter(assetClass -> assetClass.getName() == assetClassType)
                .findFirst().map(AssetClass::getProportion)
                .orElse(BigDecimal.ZERO);
    }

}

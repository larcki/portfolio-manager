package com.nordcomet.pflio.asset.model;

import com.nordcomet.pflio.asset.classification.AssetClass2;
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
            name = "asset_class_mapping_2",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "asset_class_id")
    )
    private Set<AssetClass2> assetClasses2;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account account;

    private String isin;

    @Enumerated(EnumType.STRING)
    private Currency baseCurrency;

    @Enumerated(EnumType.STRING)
    private Currency quoteCurrency;

}

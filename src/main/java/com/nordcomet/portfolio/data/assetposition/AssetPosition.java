package com.nordcomet.portfolio.data.assetposition;

import com.nordcomet.portfolio.data.asset.Asset;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class AssetPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    // Amounts in asset's base currency

    @Column(precision = 12, scale = 4)
    private BigDecimal quantity;

    @Column(precision = 12, scale = 4)
    private BigDecimal unitPrice;

    @Column(precision = 12, scale = 4)
    private BigDecimal totalValue;

    @Column(precision = 12, scale = 4)
    private BigDecimal totalPurchaseAmount;

    private LocalDateTime timestamp;

    public AssetPosition(Asset asset, BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalValue, BigDecimal totalPurchaseAmount, LocalDateTime timestamp) {
        this.asset = asset;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalValue = totalValue;
        this.totalPurchaseAmount = totalPurchaseAmount;
        this.timestamp = timestamp;
    }

}

package com.nordcomet.pflio.asset.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AssetPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(precision = 12, scale = 4)
    private BigDecimal quantity;

    @Column(precision = 12, scale = 4)
    private BigDecimal price;

    @Column(precision = 12, scale = 4)
    private BigDecimal totalPrice;

    @Column(precision = 12, scale = 4)
    private BigDecimal totalPurchaseAmount;

    private LocalDateTime timestamp;

    public AssetPosition(Asset asset, BigDecimal quantity, BigDecimal price, BigDecimal totalPrice, BigDecimal totalPurchaseAmount, LocalDateTime timestamp) {
        this.asset = asset;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.totalPurchaseAmount = totalPurchaseAmount;
        this.timestamp = timestamp;
    }

}

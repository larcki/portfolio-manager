package com.nordcomet.pflio.model.snapshot;

import com.nordcomet.pflio.model.Asset;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class AssetPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    private LocalDateTime timestamp;

    public AssetPosition(Asset asset, BigDecimal quantity, BigDecimal price, BigDecimal totalPrice, LocalDateTime timestamp) {
        this.asset = asset;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }


    public AssetPosition() {
    }

    public Integer getId() {
        return id;
    }

    public Asset getAsset() {
        return asset;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

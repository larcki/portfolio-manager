package com.nordcomet.pflio.asset.model.snapshot;

import com.nordcomet.pflio.asset.model.Asset;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
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

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetPosition that = (AssetPosition) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(price, that.price) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, asset, quantity, price, totalPrice, timestamp);
    }

    @Override
    public String toString() {
        return "AssetPosition{" +
                "id=" + id +
                ", asset=" + asset +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", timestamp=" + timestamp +
                '}';
    }
}

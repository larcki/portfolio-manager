package com.nordcomet.pflio.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private LocalDateTime timestamp;

    private BigDecimal price;

    private BigDecimal quantityChange;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(BigDecimal quantityChange) {
        this.quantityChange = quantityChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(price, that.price) &&
                Objects.equals(quantityChange, that.quantityChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, asset, timestamp, price, quantityChange);
    }
}

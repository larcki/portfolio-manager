package com.nordcomet.pflio.asset.model;

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

    @Column(precision = 12, scale = 4)
    private BigDecimal price;

    @Column(precision = 12, scale = 4)
    private BigDecimal quantityChange;

    private String currency;

    @OneToOne(cascade = CascadeType.ALL)
    private Fee fee;

    public Transaction() {

    }

    public Transaction(Asset asset, LocalDateTime timestamp, BigDecimal price, BigDecimal quantityChange, String currency, Fee fee) {
        this.asset = asset;
        this.timestamp = timestamp;
        this.price = price;
        this.quantityChange = quantityChange;
        this.currency = currency;
        this.fee = fee;
    }

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
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
                Objects.equals(quantityChange, that.quantityChange) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, asset, timestamp, price, quantityChange, currency);
    }
}

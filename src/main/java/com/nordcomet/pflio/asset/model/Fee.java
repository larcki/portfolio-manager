package com.nordcomet.pflio.asset.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Fee {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(precision = 12, scale = 4)
    private BigDecimal amount;

    private String currency;

    @ManyToOne
    private Asset asset;

    private LocalDateTime timestamp;

    public Fee() {

    }

    public Fee(BigDecimal amount, String currency, Asset asset, LocalDateTime timestamp) {
        this.amount = amount;
        this.currency = currency;
        this.asset = asset;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Asset getAsset() {
        return asset;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

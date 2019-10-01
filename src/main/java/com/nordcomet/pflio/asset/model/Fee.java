package com.nordcomet.pflio.asset.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Fee {

    @Id
    @GeneratedValue
    private UUID id;

    private final BigDecimal amount;

    private final String currency;

    @ManyToOne
    private final Asset asset;

    @ManyToOne
    private final Account account;

    private final LocalDateTime timestamp;

    public Fee(BigDecimal amount, String currency, Asset asset, Account account, LocalDateTime timestamp) {
        this.amount = amount;
        this.currency = currency;
        this.asset = asset;
        this.account = account;
        this.timestamp = timestamp;
    }

    public UUID getId() {
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

    public Account getAccount() {
        return account;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

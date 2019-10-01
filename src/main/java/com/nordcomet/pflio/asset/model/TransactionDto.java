package com.nordcomet.pflio.asset.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionDto {

    private final Integer assetId;
    private final BigDecimal quantityChange;
    private final BigDecimal unitPrice;
    private final BigDecimal totalPrice;
    private final String currency;

    public TransactionDto(Integer assetId, BigDecimal quantityChange, BigDecimal unitPrice, BigDecimal totalPrice, String currency) {
        this.assetId = assetId;
        this.quantityChange = quantityChange;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.currency = currency;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public BigDecimal getQuantityChange() {
        return quantityChange;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDto that = (TransactionDto) o;
        return Objects.equals(assetId, that.assetId) &&
                Objects.equals(quantityChange, that.quantityChange) &&
                Objects.equals(unitPrice, that.unitPrice) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetId, quantityChange, unitPrice, totalPrice, currency);
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "assetId=" + assetId +
                ", quantityChange=" + quantityChange +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", currency='" + currency + '\'' +
                '}';
    }

}

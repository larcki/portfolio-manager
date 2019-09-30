package com.nordcomet.pflio.asset.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionDto {

    private final Integer assetId;
    private final BigDecimal quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal totalPrice;
    private final String currency;


    public TransactionDto(Integer assetId, BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalPrice, String currency) {
        this.assetId = assetId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.currency = currency;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public BigDecimal getQuantity() {
        return quantity;
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
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(unitPrice, that.unitPrice) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetId, quantity, unitPrice, totalPrice, currency);
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "assetId=" + assetId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", currency='" + currency + '\'' +
                '}';
    }
}

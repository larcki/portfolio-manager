package com.nordcomet.pflio.asset.model;

import java.math.BigDecimal;

public class AssetPrice {

    private BigDecimal price;

    private String currency;

    public AssetPrice(BigDecimal price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "AssetPrice{" +
                "price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}

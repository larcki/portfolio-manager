package com.nordcomet.pflio.asset.model;

import java.util.Arrays;

public enum Currency {

    EUR,
    GBP,
    DKK,
    SEK,
    NOK,
    CAD,
    USD,
    BTC,
    ETH;

    public static Currency toCurrency(String value) {
        return Arrays.stream(values())
                .filter(currency -> currency.name().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }

}

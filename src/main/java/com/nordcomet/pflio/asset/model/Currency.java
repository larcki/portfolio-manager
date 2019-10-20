package com.nordcomet.pflio.asset.model;

import javax.persistence.Embeddable;

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

    @Override
    public String toString() {
        return this.name();
    }

}

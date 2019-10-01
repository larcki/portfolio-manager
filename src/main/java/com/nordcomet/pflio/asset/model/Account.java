package com.nordcomet.pflio.asset.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private UUID id;

    private final String name;

    private final String defaultCurrency;

    public Account(String name, String defaultCurrency) {
        this.name = name;
        this.defaultCurrency = defaultCurrency;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }
}

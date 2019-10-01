package com.nordcomet.pflio.asset.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String defaultCurrency;

    public Account(String name, String defaultCurrency) {
        this.name = name;
        this.defaultCurrency = defaultCurrency;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }
}

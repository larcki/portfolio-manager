package com.nordcomet.portfolio.data.account;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String defaultCurrency;

}

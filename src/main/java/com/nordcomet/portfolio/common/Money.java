package com.nordcomet.portfolio.common;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Embeddable
@ToString
public class Money {

    @Column(precision = 12, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

}

package com.nordcomet.portfolio.data.transaction;

import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.fee.Fee;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(name = "unit")
    private Money unitPrice;

    private Money totalAmount;

    @Column(precision = 12, scale = 4)
    private BigDecimal quantityChange;

    @OneToOne(cascade = CascadeType.ALL)
    private Fee fee;

    private LocalDateTime timestamp;

    private BigDecimal exchangeRate;

    public Transaction(Asset asset, Money unitPrice, Money totalAmount, BigDecimal quantityChange, Fee fee, LocalDateTime timestamp, BigDecimal exchangeRate) {
        this.asset = asset;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.quantityChange = quantityChange;
        this.fee = fee;
        this.timestamp = timestamp;
        this.exchangeRate = exchangeRate;
    }

}

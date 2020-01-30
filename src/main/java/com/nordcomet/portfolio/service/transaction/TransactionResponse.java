package com.nordcomet.portfolio.service.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TransactionResponse {

    private LocalDateTime time;
    private String asset;
    private String account;
    private BigDecimal totalAmount;
    private BigDecimal fee;
    private String currency;

}

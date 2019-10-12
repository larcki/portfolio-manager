package com.nordcomet.pflio.asset.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSaveRequest {

    private Integer assetId;
    private BigDecimal quantityChange;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String currency;
    private LocalDateTime timestamp;

}

package com.nordcomet.pflio.asset.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionSaveRequest {

    private Integer assetId;
    private BigDecimal quantityChange;
    private Money unitPrice;
    private Money totalAmount;
    private BigDecimal exchangeRate;
    private LocalDateTime timestamp;

}

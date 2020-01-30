package com.nordcomet.portfolio.service.transaction.imports.halifax;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class HalifaxTransactionMapper {

    private final AssetRepo assetRepo;

    @Autowired
    public HalifaxTransactionMapper(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public TransactionSaveRequest toTransactionSaveRequest(HalifaxTransaction halifaxTransaction) {
        return TransactionSaveRequest.builder()
                .assetId(findAssetId(halifaxTransaction.getCompanyCode()))
                .unitPrice(Money.of(penceToPounds(halifaxTransaction.getExecutedPrice()), Currency.GBP))
                .exchangeRate(BigDecimal.ONE)
                .quantityChange(halifaxTransaction.getQuantity())
                .timestamp(halifaxTransaction.getDate().atStartOfDay())
                .totalAmount(Money.of(halifaxTransaction.getNetConsideration(), Currency.GBP))
                .build();
    }

    private BigDecimal penceToPounds(BigDecimal pennyPrice) {
        return pennyPrice.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
    }

    private Integer findAssetId(String isin) {
        return assetRepo.findAssetByIsin(isin).orElseThrow(() -> new IllegalArgumentException("Unable to find asset with isin " + isin)).getId();
    }

}

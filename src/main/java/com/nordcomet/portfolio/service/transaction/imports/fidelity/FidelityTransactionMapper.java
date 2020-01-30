package com.nordcomet.portfolio.service.transaction.imports.fidelity;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FidelityTransactionMapper {

    private final AssetRepo assetRepo;

    @Autowired
    public FidelityTransactionMapper(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public TransactionSaveRequest toTransactionSaveRequest(FidelityTransaction fidelityTransaction) {
        return TransactionSaveRequest.builder()
                .assetId(findAssetIsin(fidelityTransaction.getInvestment()))
                .unitPrice(Money.of(fidelityTransaction.getPricePerUnit(), Currency.GBP))
                .exchangeRate(BigDecimal.ONE)
                .quantityChange(fidelityTransaction.getQuantity())
                .timestamp(fidelityTransaction.getOrderDate().atStartOfDay())
                .totalAmount(Money.of(fidelityTransaction.getAmount(), Currency.GBP))
                .build();
    }

    private Integer findAssetIsin(String isin) {
        return assetRepo.findAssetByIsin(isin).orElseThrow(() -> new IllegalArgumentException("Unable to find asset with isin " + isin)).getId();
    }

}

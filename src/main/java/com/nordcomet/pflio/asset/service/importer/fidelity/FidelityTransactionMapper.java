package com.nordcomet.pflio.asset.service.importer.fidelity;

import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
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

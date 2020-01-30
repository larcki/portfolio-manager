package com.nordcomet.portfolio.service.transaction.imports.nordnet;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
public class NordnetTransactionMapper {

    private final AssetRepo assetRepo;

    @Autowired
    public NordnetTransactionMapper(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public TransactionSaveRequest toTransactionSaveRequest(NordnetTransaction nordnetTransaction) {
        return TransactionSaveRequest.builder()
                .assetId(findAssetId(nordnetTransaction.getIsin()))
                .unitPrice(Money.of(nordnetTransaction.getPurchasePriceInCurrency(), Currency.toCurrency(nordnetTransaction.getCurrency())))
                .exchangeRate(nordnetTransaction.getPurchasePriceInEur().divide(nordnetTransaction.getPurchasePriceInCurrency(), 12, RoundingMode.HALF_UP))
                .quantityChange(nordnetTransaction.getQuantity())
                .timestamp(nordnetTransaction.getDate().atStartOfDay())
                .totalAmount(Money.of(nordnetTransaction.getPurchaseValueInEur(), Currency.EUR))
                .build();
    }

    private Integer findAssetId(String isin) {
        return assetRepo.findAssetByIsin(isin).orElseThrow(() -> new IllegalArgumentException("Unable to find asset with isin " + isin)).getId();
    }
}

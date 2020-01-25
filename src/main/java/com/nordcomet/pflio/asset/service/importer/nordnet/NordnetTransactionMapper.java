package com.nordcomet.pflio.asset.service.importer.nordnet;

import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
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

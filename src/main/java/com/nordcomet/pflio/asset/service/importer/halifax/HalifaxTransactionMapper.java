package com.nordcomet.pflio.asset.service.importer.halifax;

import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        return pennyPrice.multiply(new BigDecimal("100"));
    }

    private Integer findAssetId(String isin) {
        return assetRepo.findAssetByIsin(isin).orElseThrow(() -> new IllegalArgumentException("Unable to find asset with isin " + isin)).getId();
    }

}

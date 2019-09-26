package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AssetPositionService {

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    public BigDecimal resolveTotalQuantityForAsset(Integer assetId) {
        return assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(assetId)
                .map(AssetPosition::getQuantity)
                .orElse(calculateQuantityFromPreviousTransactions());
    }

    public AssetPosition save(AssetPosition assetPosition) {
        return assetPositionRepo.save(assetPosition);
    }

    private BigDecimal calculateQuantityFromPreviousTransactions() {
        BigDecimal previousQuantity = BigDecimal.ZERO;
        for (Transaction previousTransaction : transactionRepo.findAll()) {
            previousQuantity = previousQuantity.add(previousTransaction.getQuantityChange());
        }
        return previousQuantity;
    }
}

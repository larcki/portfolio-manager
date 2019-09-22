package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Transaction;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.AssetPositionRepo;
import com.nordcomet.pflio.repo.TransactionRepo;
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
        Optional<AssetPosition> previousAssetPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(assetId);
        if (previousAssetPosition.isPresent()) {
            return previousAssetPosition.get().getQuantity();
        } else {
            return calculateQuantityFromPreviousTransactions();
        }
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

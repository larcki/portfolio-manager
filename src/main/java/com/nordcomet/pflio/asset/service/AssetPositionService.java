package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AssetPositionService {

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    public AssetPosition createBasedOn(Transaction transaction) {
        BigDecimal previousQuantity = resolveTotalQuantityForAsset(transaction.getAsset().getId());
        AssetPosition assetPosition = toAssetPosition(transaction, previousQuantity);
        return save(assetPosition);
    }

    public BigDecimal resolveTotalQuantityForAsset(Integer assetId) {
        return assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(assetId)
                .map(AssetPosition::getQuantity)
                .orElse(calculateQuantityFromPreviousTransactions(assetId));
    }

    public AssetPosition save(AssetPosition assetPosition) {
        return assetPositionRepo.save(assetPosition);
    }

    private AssetPosition toAssetPosition(Transaction transaction, BigDecimal previousQuantity) {
        BigDecimal newQuantity = previousQuantity.add(transaction.getQuantityChange());
        BigDecimal newTotalPrice = newQuantity.multiply(transaction.getPrice());
        return new AssetPosition(transaction.getAsset(), newQuantity, transaction.getPrice(), newTotalPrice, transaction.getTimestamp());
    }

    private BigDecimal calculateQuantityFromPreviousTransactions(Integer assetId) {
        BigDecimal previousQuantity = BigDecimal.ZERO;
        for (Transaction previousTransaction : transactionRepo.findAllByAssetId(assetId)) {
            previousQuantity = previousQuantity.add(previousTransaction.getQuantityChange());
        }
        return previousQuantity;
    }
}

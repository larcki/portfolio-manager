package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AssetPositionService {

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    public AssetPosition createBasedOn(Transaction transaction) {
        AssetPosition latestAssetPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(transaction.getAsset().getId())
                .orElse(new AssetPosition(transaction.getAsset(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, LocalDateTime.now()));

        BigDecimal newQuantity = latestAssetPosition.getQuantity().add(transaction.getQuantityChange());
        BigDecimal newTotalValue = newQuantity.multiply(transaction.getUnitPrice().getAmount());
        BigDecimal purchaseAmount = transaction.getUnitPrice().getAmount().multiply(transaction.getQuantityChange());
        BigDecimal newTotalPurchaseAmount = latestAssetPosition.getTotalPurchaseAmount().add(purchaseAmount);

        AssetPosition assetPosition = new AssetPosition(transaction.getAsset(), newQuantity, transaction.getUnitPrice().getAmount(), newTotalValue, newTotalPurchaseAmount, transaction.getTimestamp());
        return save(assetPosition);
    }

    public AssetPosition save(AssetPosition assetPosition) {
        return assetPositionRepo.save(assetPosition);
    }

}

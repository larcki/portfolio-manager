package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.PriceUpdate;
import com.nordcomet.pflio.asset.model.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.PriceUpdateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceUpdateService {

    @Autowired
    private PriceUpdateRepo priceUpdateRepo;

    @Autowired
    private AssetPositionService assetPositionService;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    public void save(PriceUpdate priceUpdate) {
        priceUpdateRepo.save(priceUpdate);
        assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(priceUpdate.getAsset().getId())
                .ifPresent(previousAssetPosition -> saveNewAssetPosition(priceUpdate, previousAssetPosition));
    }

    private void saveNewAssetPosition(PriceUpdate priceUpdate, AssetPosition previousAssetPosition) {
        BigDecimal newTotalPrice = previousAssetPosition.getQuantity().multiply(priceUpdate.getPrice());
        assetPositionService.save(new AssetPosition(priceUpdate.getAsset(),
                previousAssetPosition.getQuantity(),
                priceUpdate.getPrice(),
                newTotalPrice,
                previousAssetPosition.getTotalPurchaseAmount(),
                priceUpdate.getTimestamp()));
    }


}

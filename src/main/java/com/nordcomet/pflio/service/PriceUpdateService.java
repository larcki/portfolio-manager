package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.PriceUpdate;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.PriceUpdateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceUpdateService {

    @Autowired
    private PriceUpdateRepo priceUpdateRepo;

    @Autowired
    private AssetPositionService assetPositionService;

    public PriceUpdate save(PriceUpdate priceUpdate) {
        BigDecimal totalQuantity = assetPositionService.resolveTotalQuantityForAsset(priceUpdate.getAsset().getId());
        PriceUpdate newPriceUpdate = priceUpdateRepo.save(priceUpdate);
        if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
            AssetPosition assetPosition = createAssetPosition(priceUpdate, totalQuantity);
            assetPositionService.save(assetPosition);
        }
        return newPriceUpdate;
    }

    private AssetPosition createAssetPosition(PriceUpdate priceUpdate, BigDecimal totalQuantity) {
        BigDecimal newTotalPrice = totalQuantity.multiply(priceUpdate.getPrice());
        return new AssetPosition(priceUpdate.getAsset(), totalQuantity, priceUpdate.getPrice(), newTotalPrice, priceUpdate.getTimestamp());
    }

}

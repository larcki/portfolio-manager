package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.PriceUpdate;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
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

    public void save(PriceUpdate priceUpdate) {
        priceUpdateRepo.save(priceUpdate);
        BigDecimal totalQuantity = assetPositionService.resolveTotalQuantityForAsset(priceUpdate.getAsset().getId());
        if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
            assetPositionService.save(createAssetPosition(priceUpdate, totalQuantity));
        }
    }

    private AssetPosition createAssetPosition(PriceUpdate priceUpdate, BigDecimal totalQuantity) {
        BigDecimal newTotalPrice = totalQuantity.multiply(priceUpdate.getPrice());
        return new AssetPosition(priceUpdate.getAsset(), totalQuantity, priceUpdate.getPrice(), newTotalPrice, priceUpdate.getTimestamp());
    }

}

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
        PriceUpdate result = priceUpdateRepo.save(priceUpdate);
        BigDecimal totalQuantity = assetPositionService.resolveTotalQuantityForAsset(priceUpdate.getAsset().getId());
        BigDecimal newTotalPrice = totalQuantity.multiply(priceUpdate.getPrice());
        AssetPosition assetPosition = new AssetPosition(priceUpdate.getAsset(), totalQuantity, priceUpdate.getPrice(), newTotalPrice, priceUpdate.getTimestamp());
        assetPositionService.save(assetPosition);
        return result;
    }

}

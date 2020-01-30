package com.nordcomet.portfolio.service.priceupdate;

import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import com.nordcomet.portfolio.data.assetposition.AssetPositionRepo;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdate;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdateRepo;
import com.nordcomet.portfolio.service.assetposition.AssetPositionService;
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

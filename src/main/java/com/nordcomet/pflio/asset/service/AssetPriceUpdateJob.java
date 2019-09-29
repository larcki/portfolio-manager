package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetPrice;
import com.nordcomet.pflio.asset.model.PriceUpdate;
import com.nordcomet.pflio.asset.parser.PriceParserService;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AssetPriceUpdateJob {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private PriceUpdateService priceUpdateService;

    @Autowired
    private PriceParserService priceParserService;

    @Scheduled(fixedRate = 60000)
    public void updateAssetPrices() {
        assetRepo.findAssetsByParserOptionsNotNull()
                .forEach(asset -> priceParserService.getPrice(asset.getParserOptions())
                        .ifPresent(assetPrice -> savePriceUpdate(asset, assetPrice)));


    }

    private void savePriceUpdate(Asset asset, AssetPrice assetPrice) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setAsset(asset);
        priceUpdate.setPrice(assetPrice.getPrice());
        priceUpdate.setTimestamp(LocalDateTime.now());
        priceUpdateService.save(priceUpdate);
    }

}

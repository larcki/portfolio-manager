package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.PriceUpdate;
import com.nordcomet.pflio.asset.parser.PriceParserService;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AssetPriceUpdateJob {

    private static final String EVERY_NOON = "0 0 12 * * *";

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private PriceUpdateService priceUpdateService;

    @Autowired
    private PriceParserService priceParserService;

    @Scheduled(cron = EVERY_NOON)
    public void updateAssetPrices() {

        log.info("Update asset prices job started...");
        long startTime = System.currentTimeMillis();

        assetRepo.findAssetsByParserOptionsNotNull()
                .forEach(asset -> priceParserService.getPrice(asset.getParserOptions())
                        .ifPresent(assetPrice -> savePriceUpdate(asset, assetPrice)));

        log.info("Update asset prices job finished, duration {} s", (System.currentTimeMillis() - startTime));

    }

    private void savePriceUpdate(Asset asset, Money assetPrice) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setAsset(asset);
        priceUpdate.setPrice(assetPrice.getAmount());
        priceUpdate.setTimestamp(LocalDateTime.now());
        priceUpdateService.save(priceUpdate);
    }

}

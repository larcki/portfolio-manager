package com.nordcomet.portfolio.scheduled;

import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdate;
import com.nordcomet.portfolio.service.priceupdate.PriceParserService;
import com.nordcomet.portfolio.service.priceupdate.PriceUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AssetPriceUpdateJob {

    private static final String EVERY_NOON = "0 0 12 * * *";

    private final AssetRepo assetRepo;
    private final PriceUpdateService priceUpdateService;
    private final PriceParserService priceParserService;

    @Autowired
    public AssetPriceUpdateJob(AssetRepo assetRepo, PriceUpdateService priceUpdateService, PriceParserService priceParserService) {
        this.assetRepo = assetRepo;
        this.priceUpdateService = priceUpdateService;
        this.priceParserService = priceParserService;
    }

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

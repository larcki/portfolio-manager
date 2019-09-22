package com.nordcomet.pflio;

import com.nordcomet.pflio.model.*;
import com.nordcomet.pflio.model.snapshot.AssetPosition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ModelCreator {

    public static Asset randomAsset() {
        Asset asset = new Asset();
        asset.setName(randomString());
        asset.setCode(randomString());
        return asset;
    }

    public static PriceUpdate randomPriceUpdate(Asset asset) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setPrice(randomPrice());
        priceUpdate.setTimestamp(LocalDateTime.now());
        priceUpdate.setAsset(asset);
        return priceUpdate;
    }

    public static AssetPosition randomAssetPosition(Asset asset) {
        return new AssetPosition(
                asset,
                randomQuantity(),
                randomPrice(),
                randomPrice(),
                LocalDateTime.now()
        );
    }

    public static Transaction randomTransaction(Asset asset) {
        Transaction transaction = new Transaction();
        transaction.setAsset(asset);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setPrice(randomPrice());
        transaction.setQuantityChange(randomQuantity());
        return transaction;
    }

    private static BigDecimal randomPrice() {
        return adjust(new BigDecimal("10"), -5, 10).setScale(4, RoundingMode.HALF_UP);
    }

    private static BigDecimal randomQuantity() {
        return new BigDecimal(randomInt(0, 10)).setScale(4, RoundingMode.HALF_UP);
    }

    private static String randomString() {
        return UUID.randomUUID().toString();
    }


    public static Asset asset(String name, Tags tag) {
        Asset asset = new Asset();
        asset.setName(name);
        asset.setCode(name);
        asset.setTags(List.of(new Tag(BigDecimal.ONE, tag)));
        return asset;
    }

    public static int randomInt(int from, int to) {
        return from + new Random().nextInt(to);
    }

    public static Transaction transaction(Asset google, int daysOfData, int value) {
        Transaction transaction = new Transaction();
        transaction.setAsset(google);
        transaction.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(value));
        transaction.setPrice(adjust(new BigDecimal("10"), -0.3, 0.4));
        transaction.setQuantityChange(new BigDecimal(randomInt(5, 10)));
        return transaction;
    }


    public static double random(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();
    }

    public static BigDecimal adjust(BigDecimal price, double from, double to) {
        return price.add(BigDecimal.valueOf(random(from, to)));
    }

    public static PriceUpdate priceUpdate(Asset google, int daysOfData, int value) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setAsset(google);
        priceUpdate.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(value));
        priceUpdate.setPrice(adjust(new BigDecimal("10"), -0.3, 0.4));
        return priceUpdate;
    }
}

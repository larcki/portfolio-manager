package com.nordcomet.pflio;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataRandomiser {

    public static Asset randomAsset() {
        Asset asset = new Asset();
        asset.setName(randomString());
        asset.setTags(randomTags());
        return asset;
    }

    private static List<Tag> randomTags() {
        if (probabilityOf(0.5)) {
            return List.of(new Tag(BigDecimal.ONE, Tags.STOCK));
        } else {
            return List.of(new Tag(BigDecimal.ONE, Tags.BOND));
        }
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

    public static TransactionDto randomTransactionDto(Asset asset) {
        BigDecimal quantityChange = randomQuantity();
        BigDecimal unitPrice = randomAmount();
        BigDecimal totalPrice = quantityChange.multiply(unitPrice).add(randomAmount(0, 1)).setScale(4, RoundingMode.HALF_UP);
        return new TransactionDto(asset.getId(), quantityChange, unitPrice, totalPrice, "GBP");
    }

    private static BigDecimal randomPrice() {
        return adjust(new BigDecimal("10"), -1, 3).setScale(4, RoundingMode.HALF_UP);
    }

    private static BigDecimal randomQuantity() {
        return new BigDecimal(randomInt(1, 3)).setScale(4, RoundingMode.HALF_UP);
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt(int from, int to) {
        return from + new Random().nextInt(to);
    }

    public static int randomInt() {
        return new Random().nextInt(100000);
    }

    public static Transaction transaction(Asset asset, int daysOfData, int daysOffset) {
        Transaction transaction = randomTransaction(asset);
        transaction.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(daysOffset));
        return transaction;
    }

    public static double random(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();
    }

    public static BigDecimal adjust(BigDecimal price, double from, double to) {
        return price.add(randomAmount(from, to));
    }

    private static BigDecimal randomAmount(double from, double to) {
        return BigDecimal.valueOf(random(from, to)).setScale(4, RoundingMode.HALF_UP);
    }

    private static BigDecimal randomAmount() {
        return BigDecimal.valueOf(random(0, 100)).setScale(4, RoundingMode.HALF_UP);
    }

    public static PriceUpdate priceUpdate(Asset asset, int daysOfData, int daysOffset) {
        PriceUpdate priceUpdate = randomPriceUpdate(asset);
        priceUpdate.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(daysOffset));
        return priceUpdate;
    }

    public static Account randomAccount() {
        return new Account(randomString(), "EUR");
    }

    public static Fee randomFee(Asset asset) {
        return new Fee(randomAmount(0.1, 1), "EUR", asset, LocalDateTime.now());
    }

    static boolean probabilityOf(Double value) {
        return Math.random() <= value;
    }

}

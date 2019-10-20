package com.nordcomet.pflio;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.nordcomet.pflio.asset.model.AssetClassType.*;

public class DataRandomiser {

    public static Asset randomAsset(Account account) {
        Asset asset = new Asset();
        asset.setName(randomString());
        asset.setAssetClasses(randomAssetClasses());
        asset.setRegion(randomRegion());
        asset.setAccount(account);
        return asset;
    }

    public static Asset randomAsset() {
        return randomAsset(randomAccount());
    }

    private static Region randomRegion() {
        return Region.values()[randomInt(0, Region.values().length - 1)];
    }

    private static Set<AssetClass> randomAssetClasses() {
        return Set.of(
                new AssetClass(BOND, new BigDecimal("0.4")),
                new AssetClass(STOCK, new BigDecimal("0.1")),
                new AssetClass(PROPERTY, new BigDecimal("0.3")),
                new AssetClass(CASH_EUR, new BigDecimal("0.2"))
        );
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
                randomPrice(), LocalDateTime.now()
        );
    }

    public static Transaction randomTransaction(Asset asset) {
        return randomBaseTransaction(asset).build();
    }

    public static Transaction.TransactionBuilder randomBaseTransaction(Asset asset) {
        Money unitPrice = Money.of(randomPrice(), Currency.EUR);
        BigDecimal quantityChange = randomQuantity();
        return Transaction.builder()
                .asset(asset)
                .unitPrice(unitPrice)
                .quantityChange(quantityChange)
                .totalAmount(Money.of(unitPrice.getAmount().multiply(quantityChange), Currency.EUR))
                .timestamp(LocalDateTime.now());
    }

    public static TransactionSaveRequest randomTransactionDto(Asset asset, LocalDateTime timestamp) {
        BigDecimal quantityChange = randomQuantity();
        BigDecimal unitPrice = randomAmount();
        BigDecimal totalPrice = quantityChange.multiply(unitPrice).add(randomAmount(0, 1)).setScale(4, RoundingMode.HALF_UP);
        return TransactionSaveRequest.builder()
                .assetId(asset.getId())
                .quantityChange(quantityChange)
                .unitPrice(Money.of(unitPrice, Currency.GBP))
                .totalAmount(Money.of(totalPrice, Currency.GBP))
                .exchangeRate(BigDecimal.ONE)
                .timestamp(timestamp)
                .build();
    }

    public static TransactionSaveRequest randomTransactionDto(Asset asset) {
        return randomTransactionDto(asset, LocalDateTime.now());
    }

    public static BigDecimal randomPrice() {
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
        return randomBaseTransaction(asset)
                .timestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(daysOffset))
                .build();
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
        return Account.builder()
                .name(randomString())
                .defaultCurrency("EUR")
                .build();
    }

    public static Fee randomFee(Asset asset) {
        return Fee.builder()
                .amount(Money.of(randomAmount(0.1, 1), Currency.EUR))
                .asset(asset)
                .timestamp(LocalDateTime.now())
                .build();
    }

    static boolean probabilityOf(Double value) {
        return Math.random() <= value;
    }

}
